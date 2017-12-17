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
		if row[2].value == unicode('����Ʈ�����а�','euc-kr').encode('utf-8'):
			if number != row[4].value:
				if number != -1:
					csv_writer.writerow([number,float(float(network)/count),float(float(system)/count)])
				number = row[4].value
				count = 0
				network = 0
				system = 0
			count+=1
			if row[3].value.replace(' ','') == unicode('�ü��','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('�ý������α׷���','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('�Ӻ�������Ʈ����','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('�л�ý��ۼ���','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('�Ӻ�������Ʈ����','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('����Ͻý��ۼ���','euc-kr').encode('utf-8'):
				system+=1
			elif row[3].value.replace(' ','') == unicode('��ǻ�ͳ�Ʈ��ũ','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('��Ʈ��ũ����Ʈ����','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('��ǻ�����','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('������Ʈ��ũ','euc-kr').encode('utf-8') or row[3].value.replace(' ','') == unicode('��Ʈ��ũ�����','euc-kr').encode('utf-8'):
				network+=1
csv_file.close()
print 'finished'