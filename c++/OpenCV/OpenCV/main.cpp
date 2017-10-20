#include "main.h"

using namespace cv;
using namespace std;

int main() {
	vector<short> lines, marks;
	Mat src, edges, kernel, hori, vert;
	src = inputFile("test.jpg");
	printf("this table size is %d X %d\n\n", src.rows, src.cols);
	//	imshow("src", src);

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

	hori = edges.clone();
	hori = workHorizontal(hori,15);
	//	imshow("hori", hori);

	vert = edges.clone();
	vert = workVertical(vert, 15);
	//	imshow("vert", vert);

	marks = getHorizontalCoord(hori, 15);
	lines = getVerticalCoord(vert, 15);
	printf("there are %d by %d lines\n", lines.size(), marks.size());
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

	divideTable(src, lines, marks);

	vector<float> labels = getLabel(LABEL_NAME, marks.size() - 2);	//2를 빼는 이유는 양쪽 끝 선은 포함 하면 안되므로
	printf("\nLabel : ");
	for (int i = 0; i < labels.size(); printf("%.1f ", labels[i]), i++);

	printf("\n\nAllocated time : \n");

	vector<float> *times = getTime(labels, marks);
	for (int i = 0; i < 5; i++) {
		for (int j = 0; j < times[i].size(); j++)
			printf("%.2f ", times[i][j]);
		printf("\n");
	}
	
	waitKey();
	return 0;
}