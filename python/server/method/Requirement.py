# -*- coding: utf-8 -*-

import os
import httplib
import xml.etree.ElementTree as e
import json
import sys  

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

def getPriorCode(result):
	before = result.before
	abeek = result.abeek
	year = result.number/100000
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
	params+='<param id="strYy" type="STRING">'+str(year)+'</param>\n'
	params+='<param id="strPosiGrpCd" type="STRING">31</param>\n'
	params+='<param id="strUpDeptCd" type="STRING"></param>\n'
	params+='<param id="strUseFg" type="STRING">1</param>\n'
	params+='<param id="strCampCd" type="STRING">S</param>\n'
	params+='<param id="strUserDeptCd" type="STRING">0000000000</param>\n'
	params+='<param id="strFg" type="STRING">'+str(abeek)+'</param>\n'
	params+='<param id="AUDIT9_ID" type="STRING"></param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/com/com/cmmn/code/findDeptList3.action?",params,headers)
	response = h.getresponse()
	h.close
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			if str(r.find('deptKorNm').text) == before:
				code = str(r.find('deptCd').text)
				print code
	return code

def getRequirementFromTable(user, subject, code):
	name = ''
	number = user.number;
	if user.abeek==1:
		name ='server\\graduate\\A_'+str(number/100000)+'_'+code+'.txt'
		f = open(name,'r')
	else:
		name ='server\\graduate\\N_'+str(number/100000)+'_'+code+'.txt'
		f = open(name,'r')
	print 'file "'+name+'" is open'

	data = []
	count = []
	group = []
	root = e.fromstring(f.read())
	if user.abeek==1:
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

	for r in subject:
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
							count[i]=str(int(count[i])-1)
				
			else:
				del(data[index])
				del(count[index])
				del(group[index])
		except:
			pass
	return data
#	for d in data:
#		result = Lecture.objects.filter(name=d.replace(' ','')).distinct()
#		if result[0]:
#			req.append((result.name,result.grade))
#			print result.name+' '+result.grade
#	return req