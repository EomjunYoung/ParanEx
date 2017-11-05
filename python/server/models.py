# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.db import models

class User(models.Model):
    number = models.IntegerField(primary_key=True)
    name = models.CharField(max_length=20)
    grade = models.CharField(max_length=20)
    major = models.CharField(max_length=40)
    before = models.CharField(max_length=40, null=True)
    abeek = models.IntegerField(default=0)
    fresh = models.IntegerField(default=1)
    def __str__(self):
        return self.id

class Subject(models.Model):
    number = models.IntegerField()
    re = models.IntegerField()
    name = models.CharField(max_length=100)
    type = models.CharField(max_length=10)
    class Meta:
        unique_together = (('number', 'name'),)
    def __str__(self):
        return self.name

class Lecture(models.Model):
    name = models.CharField(max_length=40,primary_key=True)
    type = models.CharField(max_length=10, null=True)
    major = models.CharField(max_length=40, null=True)
    label = models.IntegerField(null=True)
    def __str__(self):
        return self.name