#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include <string.h>
#include <openssl/ssl.h>
#include <openssl/err.h>
#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>
#include <opencv2/opencv.hpp>

#define BUFF_SIZE 1024
#define GOOGLE_VISION "172.217.24.238:443"
#define DESCRIPTION_SIZE 16
#define LABEL_NAME "0.jpg"
using namespace std;
using namespace cv;

Mat fetchTable(string file, int scale);
Mat inputFile(string file);
double findAngle(Mat src);
Mat rotateMatrix(Mat src, double angle);
Mat changeGray(Mat src);
Mat workHorizontal(Mat horizontal, int scale);
Mat workVertical(Mat vertical, int scale);
vector<short> getVerticalCoord(Mat table, int scale);
vector<short> getHorizontalCoord(Mat table, int scale);
vector<float> getLabel(char *name, int size);
void divideTable(Mat table, vector<short> lines, short offset);
char *base64(char *input, int length);
char *useOCR(char *name);

int main()
{
	Mat table;
	int scale = 15;
	vector<short> lines, marks;
	vector<float> labels;

	/* input	file  : name of image					*/
	/*			scale : the bigger the more detailed	*/
	table = fetchTable("untitled7.jpg", scale);

	imshow("roi", table);
	cout << endl << "size : " << table.cols << "x" << table.rows << endl;

	lines = getVerticalCoord(table, scale);
	printf("coordinate of vertical lines : ");
	for (int i = 0; i < lines.size(); printf("%d ", lines[i]), i++);

	cout << endl;

	marks = getHorizontalCoord(table, scale);
	printf("coordinate of horizontal lines : ");
	for (int i = 0; i < marks.size(); printf("%d ", marks[i]), i++);

	divideTable(table, lines, marks[0]);

	labels = getLabel(LABEL_NAME, marks.size());
	printf("\n");
	for (int i = 0; i < labels.size(); printf("%.1f ", labels[i]), i++);

	Mat empty = inputFile("1.jpg");
	empty = changeGray(empty);

	erode(empty, empty, Mat(), Point(-1, -1), 10); // should be improved
	dilate(empty, empty, Mat(), Point(-1, -1), 5); // should be improved

	threshold(empty, empty, 0, 255, THRESH_OTSU);

	bool manymarks = (labels[1] - labels[0] < 1.0f)?true:false;
	for (int i = 0, length = marks.size(), delta; i < length - 1; i++) {
		if (manymarks) {
			delta = (marks[i + 1] - marks[i]) / 2;
			if (empty.at<uchar>(marks[i] - marks[0] + delta, 80) == 0) {	//나중에 80 수정
				printf("%.1f ", labels[i]);
			}
		}
		else {
			delta = (marks[i + 1] - marks[i]) / 4;
			if (empty.at<uchar>(marks[i] - marks[0] + delta, 100) == 0) {	//나중에 100 수정
				printf("%d.0 ", (int)(labels[i]));
			}if (empty.at<uchar>(marks[i] - marks[0] + 3*delta, 100) == 0) {	//나중에 100 수정
				printf("%d.5 ", (int)(labels[i]));
			}
		}
	}

	imshow("6", empty);
	
	waitKey();
	return 0;
}

Mat fetchTable(string file, int scale) {
	Mat src, edge, gray, bw, horizontal, vertical, mask, joints, rsz;
	int width, height;
	double angle;

	src = inputFile(file);

	Canny(src, edge, 50, 200, 3);
	angle = findAngle(edge);
	if (angle != 0) {
		cout << "rotate : " << angle << endl;
		src = rotateMatrix(src, angle);
	}
	width = src.cols;
	height = src.rows;

	cout << "name : " << file << endl;
	cout << "size : " << width << "x" << height << endl;

	gray = changeGray(src);
	adaptiveThreshold(~gray, bw, 255, CV_ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);

	horizontal = bw.clone();
	vertical = bw.clone();


	horizontal = workHorizontal(horizontal, scale);
	vertical = workVertical(vertical, scale);
	mask = horizontal + vertical;
	bitwise_and(horizontal, vertical, joints);

	vector<Vec4i> hierarchy;
	std::vector<std::vector<cv::Point> > contours;
	cv::findContours(mask, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));

	vector<vector<Point> > contours_poly(contours.size());
	vector<Rect> boundRect(contours.size());
	vector<Mat> rois;

	for (size_t i = 0; i < contours.size(); i++)
	{
		// find the area of each contour
		double area = contourArea(contours[i]);

		//        // filter individual lines of blobs that might exist and they do not represent a table
		if (area < 100) // value is randomly chosen, you will need to find that by yourself with trial and error procedure
			continue;

		approxPolyDP(Mat(contours[i]), contours_poly[i], 3, true);
		boundRect[i] = boundingRect(Mat(contours_poly[i]));

		// find the number of joints that each table has
		Mat roi = joints(boundRect[i]);

		vector<vector<Point> > joints_contours;
		findContours(roi, joints_contours, RETR_CCOMP, CHAIN_APPROX_SIMPLE);

		// if the number is not more than 5 then most likely it not a table
		if (joints_contours.size() <= 4)
			continue;

		rois.push_back(src(boundRect[i]).clone());
		if (rois.size() != 1) {
			printf("error), Cannot find time table!\n");
			waitKey();
			exit(1);
		}
		rectangle(src, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 255, 0), 3, 8, 0);
	}
	while (width > 1000 || height > 1000) {
		width /= 3;
		height /= 3;
	}

	resize(src, rsz, Size(width, height));
	imshow("contours", rsz);

	return rois[0];
}

Mat inputFile(string file) {

	Mat src = imread(file);
	if (!src.data) {
		printf("error), Please check your input!\n");
		waitKey();
		exit(1);
	}

	return src;
}

/* reference : https://github.com/jsford/SightReader */
float median(vector<float> &v)
{
	size_t n = v.size() / 2;
	nth_element(v.begin(), v.begin() + n, v.end());
	return v[n];
}
/* reference : https://github.com/jsford/SightReader */

/* reference : https://github.com/jsford/SightReader */
double findAngle(Mat src) {
	vector<Vec4i> lines;
	vector<float> slopes;

	HoughLinesP(src, lines, 1, CV_PI / 180, 100, 50, 10);

	if (lines.size() == 0) {
		printf("error), There are no lines!\n");
		waitKey();
		exit(1);
	}

	for (size_t i = 0; i < lines.size(); i++) {
		Vec4i l = lines[i];
		//line(c_src , Point(l[0], l[1]), Point(l[2], l[3]), Scalar(0,0,255), 3, CV_AA);
		slopes.push_back((abs(l[0] - l[2]) < 0.0000001) ? 1000000 : (l[1] - l[3]) / (float)(l[0] - l[2]));
	}

	double rotation = atan(median(slopes)) * 180.0 / CV_PI;

	return rotation;
}
/* reference : https://github.com/jsford/SightReader */

/* reference : https://github.com/jsford/SightReader */
Mat rotateMatrix(Mat src, double angle) {

	Mat rotatedMatrix;

	Point2f pt(src.cols / 2.0, src.rows / 2.0);
	Mat r = getRotationMatrix2D(pt, angle, 1.0);
	Rect bbox = RotatedRect(pt, src.size(), angle).boundingRect();
	r.at<double>(0, 2) += bbox.width / 2.0 - pt.x;
	r.at<double>(1, 2) += bbox.height / 2.0 - pt.y;

	warpAffine(src, rotatedMatrix, r, bbox.size(), cv::INTER_LANCZOS4, cv::BORDER_CONSTANT, Scalar(255, 255, 255));

	return rotatedMatrix;
}
/* reference : https://github.com/jsford/SightReader */

Mat changeGray(Mat src) {
	Mat gray;
	if (src.channels() == 3)
		cvtColor(src, gray, CV_BGR2GRAY);
	else
		gray = src;

	return gray;
}

Mat workHorizontal(Mat horizontal, int scale) {
	int size = horizontal.cols / scale;
	Mat horizontalStructure = getStructuringElement(MORPH_RECT, Size(size, 1));

	erode(horizontal, horizontal, horizontalStructure, Point(-1, -1));
	dilate(horizontal, horizontal, horizontalStructure, Point(-1, -1));

	return horizontal;
}

Mat workVertical(Mat vertical, int scale) {
	int size = vertical.rows / scale;
	Mat verticalStructure = getStructuringElement(MORPH_RECT, Size(1, size));

	erode(vertical, vertical, verticalStructure, Point(-1, -1));
	dilate(vertical, vertical, verticalStructure, Point(-1, -1));

	return vertical;
}

vector<short> getVerticalCoord(Mat table, int scale) {
	Mat vertical;
	int width = table.cols, height = table.rows;
	int delta = width*0.02f;
	vector<short> lines;

	vertical = changeGray(table);
	adaptiveThreshold(~vertical, vertical, 255, CV_ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);
	vertical = workVertical(vertical, scale);

	for (int i = delta; i < height && lines.size() == 0; i++) {
		for (int j = delta; j < width; j++) {
			if (vertical.at<uchar>(i, j) != 0) {
				lines.push_back(j);
				j += delta;
			}
		}
	}
	if (lines.size() < 5) {
		printf("error), There are few colums!\n");
		waitKey();
		exit(1);
	}
	return lines;
}

vector<short> getHorizontalCoord(Mat table, int scale) {
	Mat horizontal;
	int width = table.cols, height = table.rows;
	int delta = width*0.02f;
	vector<short> marks;

	horizontal = changeGray(table);
	adaptiveThreshold(~horizontal, horizontal, 255, CV_ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);
	horizontal = workHorizontal(horizontal, scale);


	for (int i = delta; i < width && marks.size() == 0; i++) {
		for (int j = delta; j < height; j++) {
			if (horizontal.at<uchar>(j, i) != 0) {
				marks.push_back(j);
				j += delta;
			}
		}
	}

	return marks;
}

vector<float> getLabel(char *name, int size) {
	vector<float> labels;
	float start;
	bool manymarks = false;
	for (char *p = strstr(useOCR(name), "\"description\":") + DESCRIPTION_SIZE; *p != '\"'; p++) {	//읽은 문자열 인식 - 8부터 시작 9부터 시작?
		if (*p <= '9' && *p > '0') {
			start = *p - '0';
			labels.push_back(start);
			if (strstr(p, "30"))
				manymarks = true;
			break;
		}
	}
	if (labels.size() == 0) {
		printf("error), cannot read number label!\n");
		waitKey();
		exit(1);
	}

	float time = start + ((size > 15 || manymarks) ? 0.5f : 1);
	for (int i = 1; i < size; i++) {
		if (size > 15 || manymarks) {
			labels.push_back(time);
			time += 0.5f;
		}
		else {
			labels.push_back(time);
			time += 1;
		}
	}

	return labels;
}

void divideTable(Mat table, vector<short> lines, short offset) {
	Mat colums[6];
	for (int i = 0; i < 6; i++) {
		if (i == 0)
			colums[i] = table(Rect(0, offset, lines.at(i), table.rows - offset));
		else if (i == 5)
			colums[i] = table(Rect(lines.at(i - 1), offset, table.cols - lines.at(i - 1), table.rows - offset));
		else
			colums[i] = table(Rect(lines.at(i - 1), offset, lines.at(i) - lines.at(i - 1), table.rows - offset));
	}

	string name[] = { "0.jpg","1.jpg","2.jpg","3.jpg","4.jpg","5.jpg" };
	for (int i = 0; i < 6; i++) {
		imshow(name[i], colums[i]);
		colums[i] = changeGray(colums[i]);
		if (i == 0)
			adaptiveThreshold(~colums[i], colums[i], 255, CV_ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);
		imwrite(name[i], colums[i]);
	}
}

/* reference : https://devenix.wordpress.com/2008/01/18/howto-base64-encode-and-decode-with-c-and-openssl-2/ */
char *base64(char *input, int length)
{
	BIO *bmem, *b64;
	BUF_MEM *bptr;

	b64 = BIO_new(BIO_f_base64());
	bmem = BIO_new(BIO_s_mem());
	b64 = BIO_push(b64, bmem);
	BIO_write(b64, input, length);
	BIO_flush(b64);
	BIO_get_mem_ptr(b64, &bptr);

	char *buff = (char *)malloc(bptr->length);
	memcpy(buff, bptr->data, bptr->length - 1);
	buff[bptr->length - 1] = 0;

	BIO_free_all(b64);

	return buff;
}
/* reference : https://devenix.wordpress.com/2008/01/18/howto-base64-encode-and-decode-with-c-and-openssl-2/ */

char *useOCR(char *name) {
	
	BIO* bio;
	SSL* ssl;
	SSL_CTX* ctx;
	FILE *fp = NULL;
	char post[] = "POST /v1/images:annotate?key=AIzaSyDl0trokyUz3QVyGwBEIi03PAWcBO1voCM HTTP/1.1\r\n";
	char host[] = "Host: vision.googleapis.com\r\n";
	char *buffer;
	SSL_library_init();
	ctx = SSL_CTX_new(SSLv23_client_method());

	if (ctx == NULL) {
		printf("error), ctx is null!\n");
		waitKey();
		exit(1);
	}

	bio = BIO_new_ssl_connect(ctx);
	BIO_set_conn_hostname(bio, GOOGLE_VISION);

	if (BIO_do_connect(bio) <= 0) {
		printf("error), cannot connect to Google!\n");
		waitKey();
		exit(1);
	}

	fp = fopen(name, "rb");
	if (fp == NULL) {
		printf("error), cannot open file!\n");
		waitKey();
		exit(1);
	}

	fseek(fp, 0, SEEK_END);
	int fileSize = ftell(fp);
	fseek(fp, 0, SEEK_SET);

	buffer = (char*)malloc(fileSize + 1);

	fread(buffer, fileSize, 1, fp);
	char *content = base64(buffer, fileSize);
	free(buffer);
	char *request = (char*)malloc((strlen(content) + 140 + 1) * sizeof(char));
	strcpy(request, "{\"requests\":[{\"image\":{\"content\":\"");
	strcat(request, content);

	strcat(request, "\"},\"features\" : {\"type\":\"TEXT_DETECTION\",\"maxResults\" : 10}}]}");
	int digitSize = 0;
	for (int i = strlen(request); i > 0; digitSize++, i /= 10);
	char *length = (char*)malloc((20 + digitSize + 1) * sizeof(char));
	sprintf(length, "Content-Length: %d\r\n\r\n", strlen(request));

	char *message = (char*)malloc((strlen(post) + strlen(host) + strlen(length) + strlen(request) + 1) * sizeof(char));
	strcpy(message, post);
	strcat(message, host);
	strcat(message, length);
	strcat(message, request);

	if (BIO_write(bio, message, strlen(message)) <= 0) {
		printf("error), fail to send message!\n");
		waitKey();
		exit(1);
	}

	buffer = (char*)malloc(BUFF_SIZE * 2 * sizeof(char));
	BIO_read(bio, buffer, BUFF_SIZE * 2);

	BIO_free_all(bio);
	SSL_CTX_free(ctx);

	return buffer;
}