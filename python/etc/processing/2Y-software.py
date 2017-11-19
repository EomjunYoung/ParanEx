# -*- coding: utf-8 -*-
from openpyxl import load_workbook
import sys  
import csv

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

work_book = load_workbook(filename='data.xlsx', read_only=True)
for work_sheet in work_book.worksheets:
	csv_file = open(work_sheet.title+'.csv','w')
	csv_writer = csv.writer(csv_file)

	for row in work_sheet.rows:
		if row[2].value == unicode('소프트웨어학과','euc-kr').encode('utf-8'):
			csv_writer.writerow([row[4].value,row[3].value,row[5].value,row[6].value])

	csv_file.close()