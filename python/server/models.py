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
    name = models.CharField(max_length=40)
    diff = models.IntegerField(default=1)
    type = models.CharField(max_length=10, null=True)
    score = models.IntegerField(default=0)
    grade = models.IntegerField(default=0)
    major = models.CharField(max_length=40, null=True)
    time = models.CharField(max_length=100, null=True, default='')
    class Meta:
        unique_together = (('name', 'time', 'diff'),)
    def __str__(self):
        return self.name

class Processed(models.Model):
    name = models.CharField(max_length=40)
    diff = models.IntegerField(default=1)
    type = models.CharField(max_length=10, null=True)
    score = models.IntegerField(default=0)
    grade = models.IntegerField(default=0)
    label = models.IntegerField(null=True)
    week = models.IntegerField(default='')
    start = models.FloatField(default=0.0)
    finish = models.FloatField(default=0.0)
    class Meta:
        unique_together = (('name', 'start', 'diff','week'),)
    def __str__(self):
        return self.name

class TimeTable(models.Model):
    number = models.IntegerField()
    name = models.CharField(max_length=40)
    week = models.IntegerField()
    start = models.FloatField()
    end = models.FloatField()
    class Meta:
        unique_together = (('number', 'name', 'week','start'),)
    def __str__(self):
        return self.name