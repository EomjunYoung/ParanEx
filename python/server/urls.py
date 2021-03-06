"""python URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from . import views

urlpatterns = [
    url(r'^postUser', views.postUser, name='postUser'),
    url(r'^postSubject', views.postSubject, name='postSubject'),
    url(r'^checkTable', views.checkTable, name='checkTable'),
    url(r'^postTable', views.postTable, name='postTable'),
    url(r'^getTable', views.getTable, name='getTable'),
    url(r'^getRequirement', views.getRequirement, name='getRequirement'),
    url(r'^getLecture', views.getLecture, name='getLecture'),
    url(r'^getProcessed', views.getProcessed, name='getProcessed'),
    url(r'^updateTable', views.updateTable, name='updateTable'),
    url(r'^postConstraint', views.postConstraint, name='postConstraint'),
    url(r'^test', views.test, name='test'),
]
