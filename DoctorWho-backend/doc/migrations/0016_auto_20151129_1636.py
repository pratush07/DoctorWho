# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0015_averagecomrate'),
    ]

    operations = [
        migrations.RenameField(
            model_name='doctor',
            old_name='score',
            new_name='Rate',
        ),
    ]
