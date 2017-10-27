import httplib
import xml.etree.ElementTree as e
import sys  

name = []
code = []

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')


def main(year, abeek) :

	#get major code
	h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

#	year="2018"
#	abeek="1"
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
	params+='<param id="strFg" type="STRING">'+abeek+'</param>\n'
	params+='<param id="AUDIT9_ID" type="STRING"></param>\n'
	params+='</params>\n'
	params+='</root>\n'
	headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

	h.request("POST","/com/com/cmmn/code/findDeptList3.action?",params,headers)
	response = h.getresponse()
	if response.status == 200:
		root = e.fromstring(response.read())
		for r in root.find('dataset').findall('record'):
			code.append(str(r.find('deptCd').text))
			name.append(r.find('deptKorNm').text)
	h.close()


	#save requirement
	for c in code:
		h = httplib.HTTPSConnection("haksa.ajou.ac.kr")

#		year="2018"
		major=c
#		abeek="1"
		params='<?xml version="1.0" encoding="utf-8"?>\n'
		params+='<root>\n'
		params+='<params>\n'
		params+='<param id="strYy" type="STRING">'+year+'</param>\n'
		params+='<param id="strMjCd" type="STRING">'+major+'</param>\n'
		params+='<param id="strFg" type="STRING">'+abeek+'</param>\n'
		params+='<param id="AUDIT9_ID" type="STRING"></param>\n'
		params+='</params>\n'
		params+='</root>\n'
		headers = {'Content-Type': 'text/xml/SosFlexMobile;charset=utf-8'}

		h.request("POST","/uni/uni/cour/curi/findCourCurriculumInq.action?",params,headers)
		response = h.getresponse()
		if response.status == 200:
			if abeek=='1':
				f = open('A_'+year+'_'+major+'.txt','w')
			else:
				f = open('N_'+year+'_'+major+'.txt','w')
			f.write(response.read())
			f.close()
		h.close()
 
try:
	if __name__ == "__main__" :
		main(sys.argv[1],sys.argv[2])
except IndexError:
	print 'Please input year and abeek data'
