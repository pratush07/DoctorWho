# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0018_doctorapptime'),
    ]

    operations = [
        migrations.AlterField(
            model_name='doctorapptime',
            name='appt_date',
            field=models.DateField(null=True, blank=True),
        ),
        migrations.AlterField(
            model_name='doctorapptime',
            name='appt_time',
            field=models.TimeField(null=True, blank=True),
        ),
    ]
