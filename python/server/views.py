# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt

@csrf_exempt 
def postTable(request):
    if request.method == 'POST':
        print(request.POST.get('data',''))
    return render(request,'server/template/index.html')

# Create your views here.
