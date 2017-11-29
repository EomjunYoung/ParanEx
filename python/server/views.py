# -*- coding: utf-8 -*-

from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from models import *
from lecture.getLectureInfo import *
from time.parseTime import *
import base64
import os
import httplib
import xml.etree.ElementTree as e
import json
import sys  

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
	result = User.objects.filter(number=201222702)[0]
	year = str(result.number/100000)
	code=''

	h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

	params='<?xml version="1.0" encoding="utf-8"?>\n'
	params+='<root>\n'
	params+='<params>\n'
	params+='<param id="strDataSet" type="STRING">DS_MJ_CD_SH</param>\n'
	params+='<param id="strUserNo" type="STRING">000000000</param>\n'
	params+='<param id="strDeptUseFg" type="STRING">C0040002</param>\n'
	params+='<param id="strMngtYn1" type="STRING">1</param>\n'
	params+='<param id="strMngtYn2" type="STRING">0</param>\n'
	params+='<param id="strModeFg" type="STRING">S</param>\n'
	params+='<param id="strYy" type="STRING">'+year+'</param>\n'
	params+='<param id="strPosiGrpCd" type="STRING">31</param>\n'
	params+='<param id="strUpDeptCd" type="STRING"></param>\n'
	params+='<param id="strUseFg" type="STRING">1</param>\n'
	params+='<param id="strCampCd" type="STRING">S</param>\n'
	params+='<param id="strUserDeptCd" type="STRING">0000000000</param>\n'
	params+='<param id="strFg" type="STRING">'+str(result.abeek)+'</param>\n'
	params+='<param id="AUDIT9_ID" type="STRING"></param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/com/com/cmmn/code/findDeptList3.action?",params,headers)
	response = h.getresponse()
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			if str(r.find('deptKorNm').text) == result.before:
				code = str(r.find('deptCd').text)
				print code
	h.close()
	name = ''
	if result.abeek==1:
		name ='server\\graduate\\A_'+year+'_'+code+'.txt'
		f = open(name,'r')
	else:
		name ='server\\graduate\\N_'+year+'_'+code+'.txt'
		f = open(name,'r')
	print 'file "'+name+'" is open'

	data = []
	count = []
	group = []
	root = e.fromstring(f.read())
	if result.abeek==1:
		for r in root.find('dataset').findall('record'):
			try:
				if r.find('abeekSustLsnFgNm').text[1]==unicode('필','euc-kr').encode('utf-8'):	
					data.append(r.find('sbjtKorNm').text.replace(' ',''))
					count.append(str(r.find('abeekChoiceSbjtCnt').text))
					group.append(str(r.find('abeekGrpChoice').text))
			except:
				pass
	else:
		for r in root.find('dataset').findall('record'):	
			try:
				if r.find('sustLsnFgNm').text[1]==unicode('필','euc-kr').encode('utf-8'):	
					data.append(r.find('sbjtKorNm').text.replace(' ',''))
					count.append(str(r.find('abeekChoiceSbjtCnt').text))
					group.append(str(r.find('abeekGrpChoice').text))		
			except:
				pass
	f.close()

	result = Subject.objects.filter(number=201222702).distinct()
	for r in result:
		try:
			index = data.index(r.name.replace(' ',''))
			if count[index] != 'None':
				g = group[index]
				if int(count[index]) == 1:
					j = []
					for i in range(0,len(data)):
						if group[i] == g:
							j.append(data[i])
					for k in j:
						index = data.index(k)
						del(data[index]) 
						del(count[index])
						del(group[index])
				else:
					for i in range(0,len(data)):
						if g == group[i]:
							count[i]=count[i]-1
				
			else:
				del(data[index])
				del(count[index])
				del(group[index])
		except:
			pass
	for d in data:
		print d
	return render(request,'server/template/index.html')

def getLecture(request):
	if Lecture.objects.count() > 0 :
		print 'data is already saved'
		return render(request,'server/template/index.html')
	major = []
	major = getMajor(2017,2,major)
	for m in major:
		Lecture(name=m[0],diff=m[1],time=m[2],type=m[3],grade=m[4],score=m[5]).save()

	culture = []
	culture = getCulture(2017,2,culture)
	for c in culture:
		Lecture(name=c[0],diff=c[1],time=c[2],type=c[3],grade=c[4],score=c[5]).save()

	base = []
	base = getBase(2017,2,'00',base)
	base = getBase(2017,2,'DS0300202',base)
	for b in base:
		Lecture(name=b[0],diff=b[1],time=b[2],type=b[3],grade=b[4],score=b[5]).save()

	area = []
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
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,week=i,start=a[0],finish=a[1]).save()
			except:
					Processed(name=r.name,diff=r.diff,type=r.type,score=r.score,grade=r.grade,week=i,start=0,finish=0).save()
	return render(request,'server/template/index.html')



def test(request):
	result = ''
	return render(request,'server/template/index.html')