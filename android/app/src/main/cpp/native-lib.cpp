#include <jni.h>
#include <iostream>
#include <opencv2/opencv.hpp>

using namespace cv;
using namespace std;

int scale=15; // play with this variable in order to increase/decrease the amount of lines to be detected

extern "C" {
JNIEXPORT int JNICALL
Java_kr_ac_ajou_paran_util_Recognizer_rectangle(JNIEnv *env, jobject instance, jlong matAddrInput, jlong matAddrResult) {

    Mat &matInput = *(Mat *) matAddrInput;
    Mat &matResult = *(Mat *) matAddrResult;

    Mat gray;
    cvtColor(matInput, gray, CV_RGBA2GRAY);

    Mat bw;
    adaptiveThreshold(~gray, bw, 255, CV_ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, scale, -2);

    matResult = bw;
    Mat horizontal = bw.clone();
    Mat vertical = bw.clone();

    // Specify size on horizontal axis
    int horizontalsize = horizontal.cols / scale;

    // Create structure element for extracting horizontal lines through morphology operations
    Mat horizontalStructure = getStructuringElement(MORPH_RECT, Size(horizontalsize, 1));

    // Apply morphology operations
    erode(horizontal, horizontal, horizontalStructure, Point(-1, -1));
    dilate(horizontal, horizontal, horizontalStructure, Point(-1, -1));
    //    dilate(horizontal, horizontal, horizontalStructure, Point(-1, -1)); // expand horizontal lines

    // Specify size on vertical axis
    int verticalsize = vertical.rows / scale;

    // Create structure element for extracting vertical lines through morphology operations
    Mat verticalStructure = getStructuringElement(MORPH_RECT, Size(1, verticalsize));

    // Apply morphology operations
    erode(vertical, vertical, verticalStructure, Point(-1, -1));
    dilate(vertical, vertical, verticalStructure, Point(-1, -1));

    Mat mask = horizontal + vertical;

    // find the joints between the lines of the tables, we will use this information in order to descriminate tables from pictures (tables will contain more than 4 joints while a picture only 4 (i.e. at the corners))
    Mat joints;
    bitwise_and(horizontal, vertical, joints);

    // Find external contours from the mask, which most probably will belong to tables or to images
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

        // filter individual lines of blobs that might exist and they do not represent a table
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

        rois.push_back(matInput(boundRect[i]).clone());
        rectangle(matInput, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 255, 0), 1, 8, 0);
    }

    if (rois.size() == 1) {
        matResult = rois[0];
    }
    return rois.size();
}
}


extern "C" {
JNIEXPORT void JNICALL
Java_kr_ac_ajou_paran_util_Recognizer_save(JNIEnv *env, jobject instance, jlong matAddrResult) {

    Mat &matResult = *(Mat *) matAddrResult;
    imwrite("/storage/emulated/0/test.jpg",matResult);
}
}