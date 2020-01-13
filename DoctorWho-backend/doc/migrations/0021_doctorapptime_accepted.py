# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0020_auto_20151219_0459'),
    ]

    operations = [
        migrations.AddField(
            model_name='doctorapptime',
            name='accepted',
            field=models.IntegerField(null=True),
        ),
    ]
