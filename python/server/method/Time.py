#-*- coding: utf-8 -*-
import sys 
import re

#setting utf-8
reload(sys)  
sys.setdefaultencoding('utf-8')

p = re.compile('\([^\)]*\)')
pz= re.compile(':00')
ph= re.compile(':30')

def getTime(week,time):
	if week == 0:
		return parseTime(unicode('월','euc-kr').encode('utf-8'),p.sub('',time).replace('  ',' '))
	elif week == 1:
		return parseTime(unicode('화','euc-kr').encode('utf-8'),p.sub('',time).replace('  ',' '))
	elif week == 2:
		return parseTime(unicode('수','euc-kr').encode('utf-8'),p.sub('',time).replace('  ',' '))
	elif week == 3:
		return parseTime(unicode('목','euc-kr').encode('utf-8'),p.sub('',time).replace('  ',' '))
	elif week == 4:
		return parseTime(unicode('금','euc-kr').encode('utf-8'),p.sub('',time).replace('  ',' '))

def parseTime(letter,time):
	list = []
	for t in time[time.find(letter):].split(' '):
		if t.find(letter) < 0:
			break
		list.append(t[1:])
	answer = []
	for l in list:
		if l.find('A')>=0:
			answer.append((9.0,10.5))
		elif l.find('B')>=0:
			answer.append((10.5,12.0))
		elif l.find('C')>=0:
			answer.append((12.0,13.5))
		elif l.find('D')>=0:
			answer.append((13.5,15.0))
		elif l.find('E')>=0:
			answer.append((15.0,16.5))
		elif l.find('F')>=0:
			answer.append((16.5,18.0))
		elif l.find('G')>=0:
			answer.append((18.0,19.5))
		elif l.find('H')>=0:
			answer.append((19.5,21.0))
		elif l.find('~')>=0:
			temp = ph.sub('.5',pz.sub('.0',l)).split('~')
			answer.append((float(temp[0]),float(temp[1])))
		else:
			answer.append(((float(l)+8),(float(l)+9)))
	return answer

		