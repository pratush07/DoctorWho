# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0013_auto_20151128_0913'),
    ]

    operations = [
        migrations.AlterField(
            model_name='comment',
            name='com_type',
            field=models.CharField(max_length=12, null=True),
        ),
    ]
