#include "main.h"

int main()
{
	Mat table;
	int scale = 15;
	vector<short> lines, marks;
	vector<float> labels;
	vector<float> *times;

	/* input	file  : name of image					*/
	/*			scale : the bigger the more detailed	*/
	table = fetchTable("untitled8.jpg", scale);

	imshow("roi", table);
	cout << endl << "size : " << table.cols << "x" << table.rows << endl;

	lines = getVerticalCoord(table, scale);
	printf("coordinate of vertical lines : ");
	for (int i = 0; i < lines.size(); printf("%d ", lines[i]), i++);

	cout << endl;

	marks = getHorizontalCoord(table, scale);
	printf("coordinate of horizontal lines : ");
	for (int i = 0; i < marks.size(); printf("%d ", marks[i]), i++);

	divideTable(table, lines, marks[0]);

	labels = getLabel(LABEL_NAME, marks.size());
	printf("\nLabel : ");
	for (int i = 0; i < labels.size(); printf("%.1f ", labels[i]), i++);

	printf("\n\nAllocated time : \n");
	times = getTime(labels, marks);
	for (int i = 0; i < 5; i++) {
		for (int j = 0; j < times[i].size(); j++)
			printf("%.2f ", times[i][j]);
		printf("\n");
	}
	
	waitKey();
	return 0;
}