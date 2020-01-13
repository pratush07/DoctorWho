# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0014_auto_20151128_0915'),
    ]

    operations = [
        migrations.CreateModel(
            name='AverageComRate',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('doc_id', models.IntegerField(null=True)),
                ('rate', models.FloatField(null=True)),
            ],
        ),
    ]
