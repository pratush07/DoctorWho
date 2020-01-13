# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0019_auto_20151202_2137'),
    ]

    operations = [
        migrations.AlterField(
            model_name='doctor',
            name='rate',
            field=models.DecimalField(null=True, max_digits=5, decimal_places=2),
        ),
    ]
