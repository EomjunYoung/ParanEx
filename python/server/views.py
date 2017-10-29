# -*- coding: utf-8 -*-

from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from models import *
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
def postTable(request):
    data = request.POST.get('data','')
    data = data.replace(' ','+')
    result = ''
    if data :
        decoded_data = base64.b64decode(data)
        output = open('test.jpg','wb')
        output.write(decoded_data)
        print 'ok data is saved'
        result = os.popen('start /b ../c++/OpenCV/x64/Debug/OpenCV.exe').read()
	
	result = result[result.find('Allocated time : ')+18:]
	result = result.replace(' \n','/')
	result = result.replace('\n','/')
	
	file = open('key.txt','rb')
	key = file.read()
	file.close()
	k=0
	for i in range(1,5):
		k+=1
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
				result += j['responses'][0]['textAnnotations'][0]['description']
				result[-1]='/'
		except:
				result += '/'

		c.close()
		image.close()
    # if no data comming
    else :
        print 'no data'
	
    print result
    return render(request,'server/template/index.html',{'result':result})


@csrf_exempt 
def postSubject(request):
	temp = request.POST.get('data','')
	n = request.POST.get('number','')
	print temp
	print n

	line = temp.split('\n')
	for l in line:
		data = l.split('\t')
		Subject(number=n,re=(0 if (data[1]=='X') else 1),type=data[3],name=data[5]).save()
	return render(request,'server/template/index.html')


@csrf_exempt 
def postUser(request):
   	temp = request.POST.get('data','')
	print temp
   	data = temp.split('/')
	User(number=data[0],name=data[1],grade=data[2],major=data[3],before=data[4],abeek=data[5],fresh=data[6]).save()
	return render(request,'server/template/index.html')

def test(request):
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
				if r.find('abeekSustLsnFgNm').text[1]==unicode('ÇÊ','euc-kr').encode('utf-8'):	
					data.append(r.find('sbjtKorNm').text.replace(' ',''))
					count.append(str(r.find('abeekChoiceSbjtCnt').text))
					group.append(str(r.find('abeekGrpChoice').text))
			except:
				pass
	else:
		for r in root.find('dataset').findall('record'):	
			try:
				if r.find('sustLsnFgNm').text[1]==unicode('ÇÊ','euc-kr').encode('utf-8'):	
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


