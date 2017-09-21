#ifndef __CV_H__
#define __CV_H__

#include "common.h"

Mat fetchTable(string file, int scale);
Mat inputFile(string file);
double findAngle(Mat src);
Mat rotateMatrix(Mat src, double angle);
Mat changeGray(Mat src);
Mat workHorizontal(Mat horizontal, int scale);
Mat workVertical(Mat vertical, int scale);
vector<short> getVerticalCoord(Mat table, int scale);
vector<short> getHorizontalCoord(Mat table, int scale);
void divideTable(Mat table, vector<short> lines, short offset);
vector<float> *getTime(vector<float> labels, vector<short> marks);

#endif