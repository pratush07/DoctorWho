# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0012_auto_20151128_0911'),
    ]

    operations = [
        migrations.AddField(
            model_name='comment',
            name='com_type',
            field=models.CharField(max_length=9, null=True),
        ),
        migrations.AlterField(
            model_name='comment',
            name='com',
            field=models.CharField(max_length=500),
        ),
    ]
