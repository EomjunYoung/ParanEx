# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
import base64
import os
import httplib
import json

@csrf_exempt 
def postTable(request):
    data = request.POST.get('data','')
    data = data.replace(' ','+')
    if data :
        decoded_data = base64.b64decode(data)
        output = open('test.jpg','wb')
        output.write(decoded_data)
        print 'ok data is saved'
        result = os.popen('start /b ../c++/OpenCV/x64/Debug/OpenCV.exe').read()
        print result
	
	file = open('key.txt','rb')
	key = file.read()
	file.close()
	for i in range(1,5):
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
			for t in j['responses'][0]['textAnnotations']:
				print t['description']
		except:
			print ''
		c.close()
		image.close()
    # if no data comming
    else :
        print 'no data'

    return render(request,'server/template/index.html')

# Create your views here.
