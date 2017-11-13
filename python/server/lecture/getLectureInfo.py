import httplib
import xml.etree.ElementTree as e
import sys 

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

def getMajor(year,semester,major):
	h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

	year = str(year)
	semester = 'U000200'+str(2*semester-1)

	params='<?xml version="1.0" encoding="utf-8"?>\n'
	params+='<root>\n'
	params+='<params>\n'
	params+='<param id="strYy" type="STRING">'+year+'</param>\n'
	params+='<param id="strShtmCd" type="STRING">'+semester+'</param>\n'
	params+='<param id="strSubmattFg" type="STRING">U0209001</param>\n'
	params+='<param id="strMjCd" type="STRING">DS030020201</param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/uni/uni/cour/lssn/findCourLecturePlanDocumentReg.action?",params,headers)
	response = h.getresponse()
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			major.append((r.find('sbjtKorNm').text.replace(' ',''),r.find('ltTmNm').text))
	h.close()
	return major
	
def getCulture(year,semester,culture):
	h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

	year = str(year)
	semester = 'U000200'+str(2*semester-1)

	params='<?xml version="1.0" encoding="utf-8"?>\n'
	params+='<root>\n'
	params+='<params>\n'
	params+='<param id="strYy" type="STRING">'+year+'</param>\n'
	params+='<param id="strShtmCd" type="STRING">'+semester+'</param>\n'
	params+='<param id="strSubmattFg" type="STRING">U0209002</param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/uni/uni/cour/lssn/findCourLecturePlanDocumentReg.action?",params,headers)
	response = h.getresponse()
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			culture.append((r.find('sbjtKorNm').text.replace(' ',''),r.find('ltTmNm').text))
	h.close()
	return culture;

def getBase(year,semester,major,base):
	h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

	year = str(year)
	semester = 'U000200'+str(2*semester-1)

	params='<?xml version="1.0" encoding="utf-8"?>\n'
	params+='<root>\n'
	params+='<params>\n'
	params+='<param id="strYy" type="STRING">'+year+'</param>\n'
	params+='<param id="strShtmCd" type="STRING">'+semester+'</param>\n'
	params+='<param id="strSubmattFg" type="STRING">U0209003</param>\n'
	params+='<param id="strSustcd" type="STRING">'+major+'</param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/uni/uni/cour/lssn/findCourLecturePlanDocumentReg.action?",params,headers)
	response = h.getresponse()
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			base.append((r.find('sbjtKorNm').text.replace(' ',''),r.find('ltTmNm').text))
	h.close()
	return base;

def getArea(year,semester,area):
	h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

	year = str(year)
	semester = 'U000200'+str(2*semester-1)

	params='<?xml version="1.0" encoding="utf-8"?>\n'
	params+='<root>\n'
	params+='<params>\n'
	params+='<param id="strYy" type="STRING">'+year+'</param>\n'
	params+='<param id="strShtmCd" type="STRING">'+semester+'</param>\n'
	params+='<param id="strSubmattFg" type="STRING">U0209005</param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/uni/uni/cour/lssn/findCourLecturePlanDocumentReg.action?",params,headers)
	response = h.getresponse()
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			area.append((r.find('sbjtKorNm').text.replace(' ',''),r.find('ltTmNm').text))
	h.close()
	return area;