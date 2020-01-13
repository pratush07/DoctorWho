# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0011_auto_20151127_1727'),
    ]

    operations = [
        migrations.AlterField(
            model_name='comment',
            name='com',
            field=models.CharField(max_length=9),
        ),
    ]
