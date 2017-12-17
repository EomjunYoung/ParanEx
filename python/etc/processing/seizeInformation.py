# -*- coding: utf-8 -*-
import sys  
import csv

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

def numCluster():
	csv_file = open('classficated.csv','r')
	csv_reader = csv.reader(csv_file)

	max = 0
	for row in csv_reader:
		if row:
			if max<int(row[3]):
				max = int(row[3])
	print str(max+1) + ' clusters'
	csv_file.close()
	return max+1

def countCluster(max):
	count = []
	for i in range(max):
		count.append(0)
	csv_file = open('classficated.csv','r')
	csv_reader = csv.reader(csv_file)
	for row in csv_reader:
		if row:
			count[int(row[3])] = count[int(row[3])] + 1
	print count
	csv_file.close()

def getNumbers(max):
	numbers = [[] for cols in range(max)]
	for i in range(num_cluster):
		csv_file = open('classficated.csv','r')
		csv_reader = csv.reader(csv_file)
		for row in csv_reader:
			if row:
				if i==int(row[3]):
					numbers[i].append(row[0])
		csv_file.close()
	return numbers


num_cluster = numCluster()
count_cluster = countCluster(num_cluster)
numbers = getNumbers(num_cluster)


for i in range(num_cluster):
	frequency={}
	csv_file = open('information.csv','r')
	csv_reader = csv.reader(csv_file)
	j = -1
	row = list(csv_reader)
	for k in numbers[i]:
		while True:
			j = j+1
			if int(row[j][0]) == int(k):
				try:
					while int(row[j][0]) == int(k):  
        					subject = row[j][1].replace(' ','')
        					count = frequency.get(subject,0)
        					frequency[subject] = count+1
						j = j + 1
					j = j - 1
					break
       	 			except:
        		       	 	pass

	subject_list = []
	for subject in frequency:
    		subject_list.append((frequency[subject],subject))

	sorted_subject_list = sorted(subject_list,reverse=True)
	csv_file.close()

	csv_file = open('rank_'+str(i)+'.csv','w')
	csv_writer = csv.writer(csv_file)
	for subject in sorted_subject_list:
		csv_writer.writerow([subject[1],subject[0]])
	csv_file.close()



print 'finished'