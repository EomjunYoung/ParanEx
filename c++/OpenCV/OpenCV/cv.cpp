#include "cv.h"

Mat inputFile(string file) {
	Mat src = imread(file);
	if (!src.data) {
		printf("error), Please check your input!\n");
		waitKey();
		exit(1);
	}

	return src;
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
	int width = table.cols, height = table.rows;
	int delta = width*0.02f;
	vector<short> lines;

	Mat matHori = Mat::zeros(height, width, IMREAD_GRAYSCALE);
	line(matHori, Point(0, height / 2), Point(width, height / 2), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matHori, table, matHori);
	for (int i = 0, j = matHori.rows / 2, delta = matHori.rows*0.02f; i < matHori.cols; i++) {
		if (matHori.at<uchar>(j, i) != 0) {
			lines.push_back(i);
			i += delta;
		}
	}

	if (lines.size() < 7) {
		printf("error) there were few columns\n");
		waitKey();
		exit(1);
	}

	return lines;
}

vector<short> getHorizontalCoord(Mat table, int scale, int offset) {
	int width = table.cols, height = table.rows;
	int delta = width*0.02f;
	vector<short> marks;

	Mat matVert = Mat::zeros(height, width, IMREAD_GRAYSCALE);
	line(matVert, Point(offset, 0), Point(offset, height), Scalar(255, 255, 255), 3, 8, 0);
	bitwise_and(matVert, table, matVert);

	for (int i = 0, j = offset, delta = matVert.rows*0.02f; i < matVert.rows; i++) {
		if (matVert.at<uchar>(i, j) != 0) {
			marks.push_back(i);
			i += delta;
		}
	}

	return marks;
}

void divideTable(Mat table, vector<short> lines, vector<short> marks) {
	
	Mat colums[6];
	string name[] = { "0.jpg","1.jpg","2.jpg","3.jpg","4.jpg","5.jpg" };
	for (int i = 0, height = marks.at(marks.size() - 1); i < 6; i++) {
		colums[i] = table(Rect(lines.at(i), marks.at(1), lines.at(i + 1) - lines.at(i), height - marks.at(1)));
		//	imshow(name[i], colums[i]);
		imwrite(name[i], colums[i]);
	}
}

vector<float> *getTime(vector<float> labels, vector<short> marks) {
	static vector<float> time[5];
	int middle;

	string name[] = { "1.jpg","2.jpg","3.jpg","4.jpg","5.jpg" };
	for (int i = 0; i < 5; i++) {
		Mat empty = inputFile(name[i]);
		cvtColor(empty, empty, CV_BGR2GRAY);
		middle = empty.cols / 2;

		erode(empty, empty, Mat(), Point(-1, -1), 10); // should be improved
		dilate(empty, empty, Mat(), Point(-1, -1), 5); // should be improved

		threshold(empty, empty, 0, 255, THRESH_OTSU);

		bool manymarks = (labels[1] - labels[0] < 1.0f) ? true : false;
		for (int j = 1, length = marks.size()-2, delta; j < length - 1; j++) {
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
					time[i].push_back(labels[j] + 0.5f);
			}
		}
	}

	return time;
}