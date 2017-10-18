# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
import base64
import os

@csrf_exempt 
def postTable(request):
    data = request.POST.get('data','')
    data = data.replace(' ','+')
    if data :
        decoded_data = base64.b64decode(data)
        output = open('../c++/OpenCV/x64/Debug/test.jpg','wb')
        output.write(decoded_data)
        print 'ok data is saved'
        result = os.popen('start /b ../c++/OpenCV/x64/Debug/OpenCV.exe').read()
        print result
    # if no data comming
    else :
        print 'no data'
    return render(request,'server/template/index.html')

# Create your views here.
