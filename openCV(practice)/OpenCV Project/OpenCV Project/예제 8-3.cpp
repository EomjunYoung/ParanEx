#include "opencv.hpp"
using namespace cv;
using namespace std;
int main() {
	Mat srcImage = imread("untitled.jpg", IMREAD_GRAYSCALE);
	if (srcImage.empty())
		return -1;

	Mat edges;
	Canny(srcImage, edges, 50, 100);
	imshow("edge", edges);

	/*
	Mat kernel = getStructuringElement(MORPH_ELLIPSE, Size(3, 3));	//¾î´ÀÁ¤µµ·Î ¹¶°¶Áö?
	Mat morph;
	morphologyEx(edges, morph, CV_MOP_CLOSE, kernel);
	imshow("morph", morph);
	*/	

	vector<Vec4i> lines;
	HoughLinesP(edges, lines, 1, CV_PI / 180.0, 10, 30, 10);

	Mat dstImage(srcImage.size(), CV_8UC3);
	cvtColor(srcImage, dstImage, COLOR_GRAY2BGR);
	Vec4i params;
	int x1, x2, y1, y2;

	for (int k = 0; k < lines.size(); k++) {
		params = lines[k];
		x1 = params[0];
		y1 = params[1];
		x2 = params[2];
		y2 = params[3];
		printf("lines[%2d] = P1(%4d, %4d) = P2(%4d, %4d)\n", k, x1, y1, x2, y2);

		Point pt1(x1, y1), pt2(x2, y2);
		line(dstImage, pt1, pt2, Scalar(0, 0, 255), 2);
	}

	imshow("dstImage", dstImage);

	waitKey();
	return 0;
}
