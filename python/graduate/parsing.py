import httplib
import json
import xml.etree.ElementTree as e

c = httplib.HTTPSConnection("haksa.ajou.ac.kr")

year="2018"
major="DS030020201"
abeek="1"
params="strYy="+year+"&strMjCd="+major+"&strFg="+abeek
headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

c.request("POST","/uni/uni/cour/curi/findCourCurriculumInq.action?",params,headers)
response = c.getresponse()
if response.status == 200:
#	when load xml
#	tree = e.parse(response.read())
#
	if abeek=='1':
		f = open('A_'+year+'_'+major+'.txt','w')
	else:
		f = open('N_'+year+'_'+major+'.txt','w')
	f.write(response.read())
	f.close()
c.close()