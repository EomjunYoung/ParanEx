#ifndef __CV_H__
#define __CV_H__

#include "common.h"

Mat inputFile(string file);
Mat workHorizontal(Mat horizontal, int scale);
Mat workVertical(Mat vertical, int scale);
vector<short> getVerticalCoord(Mat table, int scale);
vector<short> getHorizontalCoord(Mat table, int scale, int offset);
void divideTable(Mat table, vector<short> lines, vector<short> marks);
vector<float> *getTime(vector<float> labels, vector<short> marks);

#endif