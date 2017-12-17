# -*- coding: utf-8 -*-
from openpyxl import load_workbook
import sys  
import csv

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

work_book = load_workbook(filename='data.xlsx', read_only=True)
csv_file = open('attribute.csv','w')
csv_writer = csv.writer(csv_file)
number = -1
count = 0
network = 0
system = 0
for work_sheet in work_book.worksheets:
	for row in work_sheet.rows:
		if row[2].value == unicode('소프트웨어학과','euc-kr').encode('utf-8'):
			if number != row[4].value:
				if number != -1:
					csv_writer.writerow([number,float(float(network)/count),float(float(system)/count)])
				number = row[4].value
				count = 0
				network = 0
				system = 0
			count+=1
			if row[3].value.replace(' ','') == unicode('운영체제','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('시스템프로그래밍','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('임베디드소프트웨어','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('분산시스템설계','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('임베디드소프트웨어','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('모바일시스템설계','euc-kr').encode('utf-8'):
				system+=1
			elif row[3].value.replace(' ','') == unicode('컴퓨터네트워크','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('네트워크소프트웨어','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('컴퓨터통신','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('무선네트워크','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('네트워크운용사례','euc-kr').encode('utf-8'):
				network+=1
csv_file.close()
print 'finished'