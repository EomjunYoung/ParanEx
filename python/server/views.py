# -*- coding: utf-8 -*-

from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from models import *
from method.Lecture import *
from method.Time import *
from method.Requirement import *
from method.Constraint import *
from operator import itemgetter
import base64
import os
import httplib
import xml.etree.ElementTree as e
import json
import sys  
import csv

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

@csrf_exempt 
def postSubject(request):
	temp = request.POST.get('data','')
	studentNumber = request.POST.get('number','')
	print temp
	print studentNumber

	#delete previous subject to refresh
	try:
		Subject.objects.filter(number=studentNumber).delete()
	#if there is no subject
	except:
		pass

	line = temp.split('\n')
	for l in line:
		data = l.split('\t')
		Subject(number=studentNumber,re=(0 if (data[1]=='X') else 1),type=data[3],name=data[5]).save()
	return render(request,'server/template/index.html')

@csrf_exempt 
def postUser(request):
   	temp = request.POST.get('data','')
	print temp
   	data = temp.split('/')
	User(number=data[0],name=data[1],grade=data[2],major=data[3],before=data[4],abeek=data[5],fresh=data[6]).save()
	return render(request,'server/template/index.html')

@csrf_exempt 
def checkTable(request):
	number = request.POST.get('number','')
	result = TimeTable.objects.filter(number=number).count()
	print result
	return render(request,'server/template/index.html',{'result':result})	

@csrf_exempt 
def postTable(request):
    number = request.POST.get('number',0)
    data = request.POST.get('data','')
    data = data.replace(' ','+')
    result = ''
    if data :
        decoded_data = base64.b64decode(data)
        output = open('test.jpg','wb')
        output.write(decoded_data)
        print 'ok data is saved'
	result = os.popen('start /b ../c++/OpenCV/x64/Debug/OpenCV.exe').read()
	if 'error' in result:
		result = result[result.find('error'):]
		result = result.split('/')[0]
   		return render(request,'server/template/index.html',{'result':result})

	result = result[result.find('Allocated time : ')+18:]
	result = result.replace(' \n','/')
	result = result.replace('\n','/')
	time = result.split('/')
	
	file = open('key.txt','rb')
	key = file.read()
	file.close()
	
	subject = [[]for cols in range(6)]
	for i in range(1,6):
		image = open(str(i)+'.jpg','rb')
       		encoded_image = base64.b64encode(image.read())
		c = httplib.HTTPSConnection("vision.googleapis.com")

		params = '{\"requests\":[{\"image\":{\"content\":\"'
		params += encoded_image
		params += '\"},\"features\" : {\"type\":\"TEXT_DETECTION\",\"maxResults\" : 10}}]}'

		c.request("POST", "/v1/images:annotate?key=" + key, params)
		response = c.getresponse()
		print response.status, response.reason
		data = response.read()
		j = json.loads(data)
		try:
			if j['responses'][0]['textAnnotations']:
				list = j['responses'][0]['textAnnotations'][0]['description'].split('\n')
				for l in list:
					print l
					if l=='':
						continue
					search = Lecture.objects.filter(name__startswith=l)
					if search.exists():
						for s in search:
							check = s.time.replace(' ','').split('(')[0][1:]
							if check.find('~')>=0:
								if time[i-1].find(check[0:2])<0:
									continue
							elif check.find('.')>=0:
								if time[i-1].find(str(int(check[0:1])+9))<0:
									continue
							elif check.find('A')>=0:
								if time[i-1].find('9.00')<0:
									continue
							elif check.find('B')>=0:
								if time[i-1].find('10.50')<0:
									continue
							elif check.find('C')>=0:
								if time[i-1].find('12.00')<0:
									continue
							elif check.find('D')>=0:
								if time[i-1].find('13.50')<0:
									continue
							elif check.find('E')>=0:
								if time[i-1].find('15.00')<0:
									continue
							elif check.find('F')>=0:
								if time[i-1].find('16.50')<0:
									continue
							elif check.find('G')>=0:
								if time[i-1].find('18.00')<0:
									continue
							else:
								if time[i-1].find(str(int(check[0:1])+8))<0:
									continue

							index = s.time.find(unicode('월','euc-kr').encode('utf-8')) 
							if index>=0:
								subject[0].append((s.name,s.time))
							index = s.time.find(unicode('화','euc-kr').encode('utf-8')) 
							if index>=0:
								subject[1].append((s.name,s.time))
							index = s.time.find(unicode('수','euc-kr').encode('utf-8')) 
							if index>=0:
								subject[2].append((s.name,s.time))
							index = s.time.find(unicode('목','euc-kr').encode('utf-8')) 
							if index>=0:
								subject[3].append((s.name,s.time))
							index = s.time.find(unicode('금','euc-kr').encode('utf-8')) 
							if index>=0:
								subject[4].append((s.name,s.time))
		except:
			pass
		c.close()
		image.close()	

	#delete previous timetable to refresh
	try:
		TimeTable.objects.filter(number=number).delete()
	#if there is no time table
	except:
		pass

	result =''
	i = 0
	for s in subject:
		for ss in s:
			for r in getTime(i,ss[1]):
				#input data at db
				try:
					TimeTable(number=number,name=ss[0],week=i,start=r[0],end=r[1]).save()
				except:
					pass
				#save data at buffer to print out
				result += ss[0]+':'+str(i)+'s'+str(r[0])+'f'+str(r[1])+'/'
		i=i+1
    # if no data comming
    else :
        print 'no data'
	
    print result
    return render(request,'server/template/index.html',{'result':result})

@csrf_exempt 
def getTable(request):
	result = ''
	number = request.POST.get('number','')
	tuples = TimeTable.objects.filter(number=number)

	for tuple in tuples:
		result+= tuple.name+':'+str(tuple.week)+'s'+str(tuple.start)+'f'+str(tuple.end)+'/'
	
	print result
	return render(request,'server/template/index.html',{'result':result})	

def getRequirement(request):
	user = User.objects.filter(number=201523485)[0]
	subject = Subject.objects.filter(number=user.number).distinct()
	code = getPriorCode(user)
	data = getRequirementFromTable(user, subject, code)
	for d in data:
		lecture = Lecture.objects.filter(name=d.replace(' ',''))
		if lecture and lecture[0].grade<=user.grade:
		#	req.append((lecture[0].name,lecture[0].grade))
			print lecture[0].name+' '+str(lecture[0].grade)
	return render(request,'server/template/index.html')

def getLecture(request):
	if Lecture.objects.count() > 0 :
		print 'data is already saved'
		return render(request,'server/template/index.html')
	major = getMajor(2017,2,major)
	for m in major:
		Lecture(name=m[0],diff=m[1],time=m[2],type=m[3],grade=m[4],score=m[5]).save()

	culture = getCulture(2017,2,culture)
	for c in culture:
		Lecture(name=c[0],diff=c[1],time=c[2],type=c[3],grade=c[4],score=c[5]).save()

	base = getBase(2017,2,'00',base)
	base = getBase(2017,2,'DS0300202',base)
	for b in base:
		Lecture(name=b[0],diff=b[1],time=b[2],type=b[3],grade=b[4],score=b[5]).save()

	area = getArea(2017,2,area)
	for a in area:
		Lecture(name=a[0],diff=a[1],time=a[2],type=a[3],grade=a[4],score=a[5]).save()
	return render(request,'server/template/index.html')

def getProcessed(request):
	if Processed.objects.count() > 0 :
		print 'data is already saved'
		return render(request,'server/template/index.html')
	result = Lecture.objects.filter()
	for r in result:
		for i in range(0,5):
			try :
				for a in getTime(i,r.time):
					if r.name == unicode('운영체제','euc-kr').encode('utf-8') or r.name == unicode('시스템프로그래밍','euc-kr').encode('utf-8') or r.name == unicode('임베디드소프트웨어','euc-kr').encode('utf-8') or r.name == unicode('분산시스템설계','euc-kr').encode('utf-8'):
						Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=1,week=i,start=a[0],finish=a[1]).save()
					elif r.name == unicode('컴퓨터네트워크','euc-kr').encode('utf-8') or r.name == unicode('네트워크소프트웨어','euc-kr').encode('utf-8') or r.name == unicode('컴퓨터통신','euc-kr').encode('utf-8') or r.name == unicode('무선네트워크','euc-kr').encode('utf-8') or r.name == unicode('네트워크운용사례','euc-kr').encode('utf-8'):
						Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=2,week=i,start=a[0],finish=a[1]).save()
					elif r.name.find(unicode('IT집중교육','euc-kr').encode('utf-8'))>-1 or r.name == unicode('IT전문영어','euc-kr').encode('utf-8') or r.name == unicode('SW캡스톤디자인','euc-kr').encode('utf-8') or r.name == unicode('자기주도프로젝트','euc-kr').encode('utf-8'):
						Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=3,week=i,start=a[0],finish=a[1]).save()
					elif r.name.find(unicode('SW현장실습','euc-kr').encode('utf-8'))>-1 or r.name.find(unicode('창업현장실습','euc-kr').encode('utf-8'))>-1 or r.name == unicode('SW창업론','euc-kr').encode('utf-8'):
						Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=4,week=i,start=a[0],finish=a[1]).save()
					else:
						Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,week=i,start=a[0],finish=a[1]).save()
			except:
				if r.name == unicode('운영체제','euc-kr').encode('utf-8') or r.name == unicode('시스템프로그래밍','euc-kr').encode('utf-8') or r.name == unicode('임베디드소프트웨어','euc-kr').encode('utf-8') or r.name == unicode('분산시스템설계','euc-kr').encode('utf-8'):
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=1,week=i,start=0,finish=0).save()
				elif r.name == unicode('컴퓨터네트워크','euc-kr').encode('utf-8') or r.name == unicode('네트워크소프트웨어','euc-kr').encode('utf-8') or r.name == unicode('컴퓨터통신','euc-kr').encode('utf-8') or r.name == unicode('무선네트워크','euc-kr').encode('utf-8') or r.name == unicode('네트워크운용사례','euc-kr').encode('utf-8'):
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=2,week=i,start=0,finish=0).save()
				elif r.name.find(unicode('IT집중교육','euc-kr').encode('utf-8'))>-1 or r.name == unicode('IT전문영어','euc-kr').encode('utf-8') or r.name == unicode('SW캡스톤디자인','euc-kr').encode('utf-8') or r.name == unicode('자기주도프로젝트','euc-kr').encode('utf-8'):
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=3,week=i,start=0,finish=0).save()
				elif r.name.find(unicode('SW현장실습','euc-kr').encode('utf-8'))>-1 or r.name.find(unicode('창업현장실습','euc-kr').encode('utf-8'))>-1 or r.name == unicode('SW창업론','euc-kr').encode('utf-8'):
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,label=4,week=i,start=0,finish=0).save()	
				else:
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,week=i,start=0,finish=0).save()
	return render(request,'server/template/index.html')

@csrf_exempt 
def updateTable(request):
	timeTables = request.POST.get('data','')
	studentNumber = request.POST.get('number','')
	print timeTables
	print studentNumber

	#delete previous table to refresh
	try:
		TimeTable.objects.filter(number=studentNumber).delete()
	#if there is no subject
	except:
		pass

	line = timeTables.split('/')
	for l in line:
		temp = l.split(':')		
		name = temp[0]
		temp = temp[1].split('s')
		week = temp[0]
		temp = temp[1].split('f')
		start = temp[0]
		end = temp[1]
		TimeTable(number=studentNumber,name=name,week=week,start=start,end=end).save()
	return render(request,'server/template/index.html')

@csrf_exempt
def postConstraint(request):
	"""
	query = Processed.objects.all()
	"""

	studentNumber = request.POST.get('number','')
	studentNumber = 201523485
	print request.POST.get('time','')
	print request.POST.get('score','')
	print request.POST.get('re','')
	print request.POST.get('include','')

	#graduate requirement
	user = User.objects.filter(number=studentNumber)[0]
	subject = Subject.objects.filter(number=user.number)
	code = getPriorCode(user)
	data = getRequirementFromTable(user, subject, code)
	requirement = []
	for d in data:
		lecture = Lecture.objects.filter(name=d.replace(' ',''))
		if lecture and lecture[0].grade<=user.grade:
			requirement.append(lecture[0].name)

	#check user's timetable
	"""
	result = TimeTable.objects.filter(number=studentNumber)
	for r in result:
		query = query.exclude(name__startswith=r.name.replace(' ',''))
	"""
	result = TimeTable.objects.filter(number=studentNumber)
	for r in result:
		if r.name.replace(' ','') in requirement:
			requirement.remove(r.name)

	#check user's subject
	"""
	result = Subject.objects.filter(number=studentNumber)
	for r in result:
		query = query.exclude(name__startswith=r.name.replace(' ',''))
	"""
	result = Subject.objects.filter(number=studentNumber)
	for r in result:
		if r.name.replace(' ','') in requirement:
			requirement.remove(r.name.replace(' ',''))

	#make query
	query = Processed.objects.none()
	for r in requirement:
		query = query | Processed.objects.filter(name=r.replace(' ',''))  


	#delete duplicate timetable
	timetable = TimeTable.objects.filter(number=studentNumber)
	query = checkTime(timetable,query)
	

	"""	
	grade = User.objects.filter(number=studentNumber)[0].grade
	query = query.exclude(grade__gte=grade)
	"""
	
	if request.POST.get('week','') != '':
		week = request.POST.get('week','').split("/")
		delete = set()
		for w in week:
			for q in query.filter(week = w):
				delete.add((q.name,q.diff))
		for d in delete:
			query = query.exclude(name = d[0],diff=d[1])

	for q in query:
		print q.name, q.diff
	print len(query)
	for r in requirement:
		print r
	print len(requirement)
	return render(request,'server/template/index.html')

def test(request):
	number = request.GET.get('number',0)
	print number
	result = Subject.objects.filter(number=number)
	count = result.count()
	subjects=[]
	network = 0
	system = 0
	for r in result:
		subjects.append(r.name.replace(' ',''))
		if r.name.replace(' ','') == unicode('운영체제','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('시스템프로그래밍','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('임베디드소프트웨어','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('분산시스템설계','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('임베디드소프트웨어','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('모바일시스템설계','euc-kr').encode('utf-8'):
			system+=1
		elif r.name.replace(' ','') == unicode('컴퓨터네트워크','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('네트워크소프트웨어','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('컴퓨터통신','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('무선네트워크','euc-kr').encode('utf-8') or r.name.replace(' ','') == unicode('네트워크운용사례','euc-kr').encode('utf-8'):
			network+=1
	network = float(network)/count 
	system = float(system)/count
	classes = []
	networks = []
	systems = []

	csv_file = open('server/processing/classficated.csv','r')
	csv_reader = csv.reader(csv_file)

	k = 3
	max = 0	

	for row in csv_reader:
		classes.append(row[3])
		systems.append(row[2])
		networks.append(row[1])
		if max<int(row[3]):
			max = int(row[3])
	csv_file.close()
	max = max+1
	
	
	distances = []
	for i in range(len(classes)):
		distances.append(((network-float(networks[i]))*(network-float(networks[i]))+(system-float(systems[i]))*(system-float(systems[i])),classes[i]))
	distances.sort(key=itemgetter(0))

	frequency = []
	for i in range(k):
		frequency.append(0)

	for d in distances[0:k]:
		frequency[int(d[1])] = frequency[int(d[1])]+1

	max = 0
	for i in range(k):
		if max < frequency[i]:
			max = frequency[i]
	frequency.sort()
	
	csv_file = open('server/processing/rank_'+str(frequency[k-1]-1)+'.csv','r')
	csv_reader = csv.reader(csv_file)
	
	length = 6
	i=0
	for row in csv_reader:
		if i == length:
			break
		if row[0].decode('utf-8') in subjects:
			continue
		else:
			print row[0].decode('utf-8')
			i=i+1
	csv_file.close()
	
	return render(request,'server/template/index.html')