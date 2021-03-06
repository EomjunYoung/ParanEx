# -*- coding: utf-8 -*-
# Generated by Django 1.11.3 on 2017-11-13 08:22
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Lecture',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('name', models.CharField(max_length=40)),
                ('type', models.CharField(max_length=10, null=True)),
                ('major', models.CharField(max_length=40, null=True)),
                ('label', models.IntegerField(null=True)),
                ('time', models.CharField(default='', max_length=100, null=True)),
            ],
        ),
        migrations.CreateModel(
            name='Subject',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('number', models.IntegerField()),
                ('re', models.IntegerField()),
                ('name', models.CharField(max_length=100)),
                ('type', models.CharField(max_length=10)),
            ],
        ),
        migrations.CreateModel(
            name='TimeTable',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('number', models.IntegerField()),
                ('name', models.CharField(max_length=40)),
                ('week', models.IntegerField()),
                ('start', models.FloatField()),
                ('end', models.FloatField()),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('number', models.IntegerField(primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=20)),
                ('grade', models.CharField(max_length=20)),
                ('major', models.CharField(max_length=40)),
                ('before', models.CharField(max_length=40, null=True)),
                ('abeek', models.IntegerField(default=0)),
                ('fresh', models.IntegerField(default=1)),
            ],
        ),
        migrations.AlterUniqueTogether(
            name='timetable',
            unique_together=set([('number', 'name', 'week')]),
        ),
        migrations.AlterUniqueTogether(
            name='subject',
            unique_together=set([('number', 'name')]),
        ),
        migrations.AlterUniqueTogether(
            name='lecture',
            unique_together=set([('name', 'time')]),
        ),
    ]
