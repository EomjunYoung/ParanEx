#include "opencv.hpp"
using namespace cv;
using namespace std;

void mergeRelatedLines(vector<Vec2f> *lines, Mat &img)
{
	vector<Vec2f>::iterator current;
	for (current = lines->begin(); current != lines->end(); current++)
	{
		if ((*current)[0] == 0 && (*current)[1] == -100) continue;
		float p1 = (*current)[0];
		float theta1 = (*current)[1];
		Point pt1current, pt2current;
		if (theta1>CV_PI * 45 / 180 && theta1<CV_PI * 135 / 180)
		{
			pt1current.x = 0;

			pt1current.y = p1 / sin(theta1);

			pt2current.x = img.size().width;
			pt2current.y = -pt2current.x / tan(theta1) + p1 / sin(theta1);
		}
		else
		{
			pt1current.y = 0;

			pt1current.x = p1 / cos(theta1);

			pt2current.y = img.size().height;
			pt2current.x = -pt2current.y / tan(theta1) + p1 / cos(theta1);

		}
		vector<Vec2f>::iterator    pos;
		for (pos = lines->begin(); pos != lines->end(); pos++)
		{
			if (*current == *pos) continue;
			if (fabs((*pos)[0] - (*current)[0])<20 && fabs((*pos)[1] - (*current)[1])<CV_PI * 10 / 180)
			{
				float p = (*pos)[0];
				float theta = (*pos)[1];
				Point pt1, pt2;
				if ((*pos)[1]>CV_PI * 45 / 180 && (*pos)[1]<CV_PI * 135 / 180)
				{
					pt1.x = 0;
					pt1.y = p / sin(theta);
					pt2.x = img.size().width;
					pt2.y = -pt2.x / tan(theta) + p / sin(theta);
				}
				else
				{
					pt1.y = 0;
					pt1.x = p / cos(theta);
					pt2.y = img.size().height;
					pt2.x = -pt2.y / tan(theta) + p / cos(theta);
				}
				if (((double)(pt1.x - pt1current.x)*(pt1.x - pt1current.x) + (pt1.y - pt1current.y)*(pt1.y - pt1current.y)<64 * 64) &&
					((double)(pt2.x - pt2current.x)*(pt2.x - pt2current.x) + (pt2.y - pt2current.y)*(pt2.y - pt2current.y)<64 * 64))
				{
					// Merge the two
					(*current)[0] = ((*current)[0] + (*pos)[0]) / 2;

					(*current)[1] = ((*current)[1] + (*pos)[1]) / 2;

					(*pos)[0] = 0;
					(*pos)[1] = -100;
				}
			}
		}
	}
}



int main() {
	Mat srcImage = imread("untitled7.jpg", IMREAD_GRAYSCALE);
	if (srcImage.empty())
		return -1;
	imshow("src",srcImage);
	Mat edges;
	GaussianBlur(srcImage, edges, Size(11, 11), 0); 
	adaptiveThreshold(edges, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 5, 2);
	adaptiveThreshold(srcImage, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 5, 2);
	bitwise_not(edges, edges); 
	Mat kernel = (Mat_<uchar>(3, 3) << 0, 1, 0, 1, 1, 1, 0, 1, 0);
	dilate(edges, edges, kernel);
	int max = -1;

	Point maxPt;

	for (int y = 0; y<edges.size().height; y++)
	{
		uchar *row = edges.ptr(y);
		for (int x = 0; x<edges.size().width; x++)
		{
			if (row[x] >= 128)
			{

				int area = floodFill(edges, Point(x, y), CV_RGB(0, 0, 64));

				if (area>max)
				{
					maxPt = Point(x, y);
					max = area;
				}
			}
		}

	} floodFill(edges, maxPt, CV_RGB(255, 255, 255));
	for (int y = 0; y<edges.size().height; y++)
	{
		uchar *row = edges.ptr(y);
		for (int x = 0; x<edges.size().width; x++)
		{
			if (row[x] == 64 && x != maxPt.x && y != maxPt.y)
			{
				int area = floodFill(edges, Point(x, y), CV_RGB(0, 0, 0));
			}
		}
	}
	erode(edges, edges, kernel);
	imshow("thresholded", edges);
	

	Mat hori=edges.clone();
	int size = hori.cols / 15;
	Mat horizontalStructure = getStructuringElement(MORPH_RECT, Size(size, 1));
	erode(hori, hori, horizontalStructure, Point(-1, -1));
	dilate(hori, hori, horizontalStructure, Point(-1, -1));
	imshow("hori", hori);



	Mat vert = edges.clone();
	size = vert.rows / 15;
	Mat verticalStructure = getStructuringElement(MORPH_RECT, Size(1, size));
	erode(vert, vert, verticalStructure, Point(-1, -1));
	dilate(vert, vert, verticalStructure, Point(-1, -1));
	imshow("vert", vert);

	Mat matVert = Mat::zeros(srcImage.rows, srcImage.cols, IMREAD_GRAYSCALE);
	line(matVert, Point(srcImage.cols/2, 0),Point(srcImage.cols/2,srcImage.rows), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matVert, hori, matVert);
	int number = 0;
	for (int i = 0, j = matVert.cols / 2; i < matVert.rows; i++) {
		if (matVert.at<uchar>(i, j) != 0) {
			number++;
			i += (matVert.rows*0.02f);
		}
	}
	printf("%d", number);
	imshow("matVert", matVert);


	Mat matHori = Mat::zeros(srcImage.rows, srcImage.cols, IMREAD_GRAYSCALE);
	line(matHori, Point(0, srcImage.rows / 2), Point(srcImage.cols, srcImage.rows/2), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matHori, vert, matHori);
	number = 0;
	for (int i = 0, j = matHori.rows / 2; i < matHori.cols; i++) {
		if (matHori.at<uchar>(j, i) != 0) {
			number++;
			i += (matHori.rows*0.02f);
		}
	}
	printf("%d", number);
	imshow("matHori", matHori);
	/*
	Canny(edges, edges, 250, 500,3);
	imshow("edge", edges);
	
	vector<Vec2f> lines;
	HoughLines(edges, lines, 1, CV_PI / 180.0, 200);   

	mergeRelatedLines(&lines, edges); // Add this line

	Mat dstImage(srcImage.size(), CV_8UC3);
	cvtColor(srcImage, dstImage, COLOR_GRAY2BGR);
	cout << "lines.size()=" << lines.size() << endl;

	Vec2f params;
	float rho, theta;
	float c, s;
	float x0, y0;
	for (int k = 0; k < lines.size(); k++) {
		params = lines[k];

		rho = params[0];
		theta = params[1];
		printf("lines[%2d](rho, theta) = (%f, %f)\n", k, rho, theta);

		c = cos(theta);
		s = sin(theta);
		x0 = rho*c;
		y0 = rho*s;

		Point pt1, pt2;
		pt1.x = cvRound(x0 + 1000 * (-s));
		pt1.y = cvRound(y0 + 1000 * (c));

		pt2.x = cvRound(x0 - 1000 * (-s));
		pt2.y = cvRound(y0 - 1000 * (c));
		line(dstImage, pt1, pt2, Scalar(0, 0, 255), 2);
	}

	imshow("dstImage", dstImage);
	*/
	waitKey();
	return 0;
}