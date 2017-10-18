#include "opencv.hpp"
using namespace cv;
using namespace std;

int main() {
	vector<short> lines, marks;
	Mat src, edges, kernel;
	src = imread("test.jpg");
	if (src.empty()) {
		printf("error) cannot open image\n");
		waitKey();
		return -1;
	}
//	imshow("src", src);
	printf("%d %d\n", src.rows, src.cols);
	cvtColor(src, edges, CV_BGR2GRAY);
	GaussianBlur(edges, edges, Size(11, 11), 0);
	adaptiveThreshold(edges, edges, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 5, 2);
	bitwise_not(edges, edges);
	kernel = (Mat_<uchar>(3, 3) << 0, 1, 0, 1, 1, 1, 0, 1, 0);
	dilate(edges, edges, kernel);
	int max = -1;
	Point maxPt;

	for (int y = 0; y < edges.size().height; y++)
	{
		uchar *row = edges.ptr(y);
		for (int x = 0; x < edges.size().width; x++)
		{
			if (row[x] >= 128)
			{

				int area = floodFill(edges, Point(x, y), CV_RGB(0, 0, 64));

				if (area > max)
				{
					maxPt = Point(x, y);
					max = area;
				}
			}
		}

	} floodFill(edges, maxPt, CV_RGB(255, 255, 255));
	for (int y = 0; y < edges.size().height; y++)
	{
		uchar *row = edges.ptr(y);
		for (int x = 0; x < edges.size().width; x++)
		{
			if (row[x] == 64 && x != maxPt.x && y != maxPt.y)
			{
				int area = floodFill(edges, Point(x, y), CV_RGB(0, 0, 0));
			}
		}
	}
	erode(edges, edges, kernel);
	//	imshow("thresholded", edges);


	Mat hori = edges.clone();
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

	Mat matVert = Mat::zeros(src.rows, src.cols, IMREAD_GRAYSCALE);
	line(matVert, Point(src.cols / 2, 0), Point(src.cols / 2, src.rows), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matVert, hori, matVert);
	for (int i = 0, j = matVert.cols / 2, delta = matVert.rows*0.02f; i < matVert.rows; i++) {
		if (matVert.at<uchar>(i, j) != 0) {
			marks.push_back(i);
			i += delta;
		}
	}
	//	imshow("matVert", matVert);


	Mat matHori = Mat::zeros(src.rows, src.cols, IMREAD_GRAYSCALE);
	line(matHori, Point(0, src.rows / 2), Point(src.cols, src.rows / 2), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matHori, vert, matHori);
	for (int i = 0, j = matHori.rows / 2, delta = matHori.rows*0.02f; i < matHori.cols; i++) {
		if (matHori.at<uchar>(j, i) != 0) {
			lines.push_back(i);
			i += delta;
		}
	}
	//	imshow("matHori", matHori);

	if (lines.size() < 7) {
		printf("error) there were few columns\n");
		waitKey();
		return -1;
	}

	printf("this table is %d X %d\n", lines.size(), marks.size());
	printf("lines : ", lines.size());
	for (int i = 0; i < lines.size(); i++) {
		printf("%d ", lines.at(i));
	}
	printf("\n");

	printf("marks : ", marks.size());
	for (int i = 0; i < marks.size(); i++) {
		printf("%d ", marks.at(i));
	}
	printf("\n");

	Mat colums[6];
	string name[] = { "0.jpg","1.jpg","2.jpg","3.jpg","4.jpg","5.jpg" };
	for (int i = 0, height = marks.at(marks.size() - 1); i < 6; i++) {
		colums[i] = src(Rect(lines.at(i), marks.at(1), lines.at(i + 1) - lines.at(i), height - marks.at(1)));
	/*	cvtColor(colums[i], colums[i], CV_BGR2GRAY);
		erode(colums[i], colums[i], Mat(), Point(-1, -1), 10); // should be improved
		dilate(colums[i], colums[i], Mat(), Point(-1, -1), 5); // should be improved
		threshold(colums[i], colums[i], 0, 255, THRESH_OTSU);	*/
	//	imshow(name[i], colums[i]);
		imwrite(name[i], colums[i]);
	}
	
	waitKey();
	return 0;
}