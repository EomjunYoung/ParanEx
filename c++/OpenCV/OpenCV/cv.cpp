#include "cv.h"

Mat fetchTable(string file, int scale) {
	Mat src, edge, gray, bw, horizontal, vertical, mask, joints, rsz;
	int width, height;
	double angle;

	src = inputFile(file);

	Canny(src, edge, 50, 200, 3);
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
		for (int j = delta; j < width-delta; j++) {
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
	printf("%d\n", lines.size());
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
		for (int j = delta; j < height-delta; j++) {
			if (horizontal.at<uchar>(j, i) != 0) {
				marks.push_back(j);
				j += delta;
			}
		}
	}

	return marks;
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

vector<float> *getTime(vector<float> labels, vector<short> marks) {
	static vector<float> time[5];
	int middle;

	string name[] = { "1.jpg","2.jpg","3.jpg","4.jpg","5.jpg" };
	for (int i = 0; i < 5; i++) {
		Mat empty = inputFile(name[i]);
		empty = changeGray(empty);
		middle = empty.cols/2;

		erode(empty, empty, Mat(), Point(-1, -1), 10); // should be improved
		dilate(empty, empty, Mat(), Point(-1, -1), 5); // should be improved

		threshold(empty, empty, 0, 255, THRESH_OTSU);

		bool manymarks = (labels[1] - labels[0] < 1.0f) ? true : false;
		for (int j = 0, length = marks.size(), delta; j < length - 1; j++) {
			if (manymarks) {
				delta = (marks[j + 1] - marks[j]) / 2;
				if (empty.at<uchar>(marks[j] - marks[0] + delta, middle) == 0)
					time[i].push_back(labels[j]);
			}
			else {
				delta = (marks[j + 1] - marks[j]) / 4;
				if (empty.at<uchar>(marks[j] - marks[0] + delta, middle) == 0) 
					time[i].push_back(labels[j]);
				if (empty.at<uchar>(marks[j] - marks[0] + 3 * delta, middle) == 0) 
					time[i].push_back(labels[j]+0.5f);
			}
		}
	}

	return time;
}