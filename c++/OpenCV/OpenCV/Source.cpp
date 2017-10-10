#include "opencv.hpp"
using namespace cv;
using namespace std;

int main() {
	Mat srcImage = imread("untitled11.jpg", IMREAD_GRAYSCALE);
	if (srcImage.empty())
		return -1;
	imshow("src",srcImage);
	printf("%d %d\n", srcImage.rows, srcImage.cols);
	Mat edges;
	GaussianBlur(srcImage, edges, Size(11, 11), 0); 
	adaptiveThreshold(edges, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 5, 2);
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
//	imshow("thresholded", edges);
	

	Mat hori=edges.clone();
	int size = hori.cols / 15;
	Mat horizontalStructure = getStructuringElement(MORPH_RECT, Size(size, 1));
	erode(hori, hori, horizontalStructure, Point(-1, -1));
	dilate(hori, hori, horizontalStructure, Point(-1, -1));
//	imshow("hori", hori);



	Mat vert = edges.clone();
	size = vert.rows / 15;
	Mat verticalStructure = getStructuringElement(MORPH_RECT, Size(1, size));
	erode(vert, vert, verticalStructure, Point(-1, -1));
	dilate(vert, vert, verticalStructure, Point(-1, -1));
//	imshow("vert", vert);

	Mat matVert = Mat::zeros(srcImage.rows, srcImage.cols, IMREAD_GRAYSCALE);
	line(matVert, Point(srcImage.cols/2, 0),Point(srcImage.cols/2,srcImage.rows), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matVert, hori, matVert);
	int number = 0;
	for (int i = 0, j = matVert.cols / 2; i < matVert.rows; i++) {
		if (matVert.at<uchar>(i, j) != 0) {
			number++;
			printf("%d ", i);
			i += (matVert.rows*0.02f);
		}
	}
	printf("%d\n", number);
//	imshow("matVert", matVert);


	Mat matHori = Mat::zeros(srcImage.rows, srcImage.cols, IMREAD_GRAYSCALE);
	line(matHori, Point(0, srcImage.rows / 2), Point(srcImage.cols, srcImage.rows/2), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matHori, vert, matHori);
	number = 0;
	for (int i = 0, j = matHori.rows / 2; i < matHori.cols; i++) {
		if (matHori.at<uchar>(j, i) != 0) {
			number++;
			printf("%d ", i);
			i += (matHori.rows*0.02f);
		}
	}
	printf("%d\n", number);
//	imshow("matHori", matHori);
	
	waitKey();
	return 0;
}