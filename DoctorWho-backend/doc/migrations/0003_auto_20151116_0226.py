# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0002_auto_20151110_2155'),
    ]

    operations = [
        migrations.CreateModel(
            name='Doctor',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('firstName', models.CharField(max_length=30)),
                ('lastName', models.CharField(max_length=30)),
                ('email', models.EmailField(max_length=70, null=True, blank=True)),
                ('gender', models.CharField(max_length=1, null=True)),
                ('profilepic', models.CharField(max_length=100, null=True)),
                ('qualification', models.CharField(max_length=40, null=True)),
                ('score', models.IntegerField(null=True)),
            ],
        ),
        migrations.RemoveField(
            model_name='user',
            name='userType',
        ),
        migrations.AddField(
            model_name='user',
            name='profilepic',
            field=models.CharField(max_length=100, null=True),
        ),
    ]
