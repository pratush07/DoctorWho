# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0017_auto_20151129_1715'),
    ]

    operations = [
        migrations.CreateModel(
            name='DoctorAppTime',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('doc_id', models.IntegerField(null=True)),
                ('pat_id', models.IntegerField(null=True)),
                ('appt_date', models.DateField(blank=True)),
                ('appt_time', models.TimeField(blank=True)),
            ],
        ),
    ]
