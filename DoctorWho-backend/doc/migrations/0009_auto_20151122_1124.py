# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0008_doctor_consultation'),
    ]

    operations = [
        migrations.AlterField(
            model_name='doctor',
            name='score',
            field=models.FloatField(null=True),
        ),
    ]
