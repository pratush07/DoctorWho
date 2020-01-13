# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0007_auto_20151118_1127'),
    ]

    operations = [
        migrations.AddField(
            model_name='doctor',
            name='consultation',
            field=models.IntegerField(null=True),
        ),
    ]
