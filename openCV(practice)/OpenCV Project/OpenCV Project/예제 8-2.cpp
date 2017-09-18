#include "opencv.hpp"
using namespace cv;
using namespace std;
int main() {
	Mat srcImage = imread("untitled5.jpg", IMREAD_GRAYSCALE);
	if (srcImage.empty())
		return -1;

	Mat edges;
	Canny(srcImage, edges, 50, 200);
	imshow("edge", edges);

	vector<Vec2f> lines;
	HoughLines(edges, lines, 1, CV_PI / 180.0, 100);

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

	waitKey();
	return 0;
}
