# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0016_auto_20151129_1636'),
    ]

    operations = [
        migrations.RenameField(
            model_name='doctor',
            old_name='Rate',
            new_name='rate',
        ),
    ]
