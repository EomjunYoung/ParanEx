#-*- coding: utf-8 -*-
import sys 
import re
import httplib

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

def httpRequirement(year,abeek,before):
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
	return response

			