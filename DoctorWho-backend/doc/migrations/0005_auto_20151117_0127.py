# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0004_auto_20151116_0256'),
    ]

    operations = [
        migrations.AlterField(
            model_name='doctor',
            name='profilePic',
            field=models.CharField(max_length=300, null=True),
        ),
        migrations.AlterField(
            model_name='user',
            name='profilePic',
            field=models.CharField(max_length=300, null=True),
        ),
    ]
