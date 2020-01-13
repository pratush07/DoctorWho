# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0010_doctor_address'),
    ]

    operations = [
        migrations.CreateModel(
            name='AverageRate',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('doc_id', models.IntegerField(null=True)),
                ('rate', models.FloatField(null=True)),
            ],
        ),
        migrations.CreateModel(
            name='Rate',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('rate_from', models.IntegerField(null=True)),
                ('rate_to', models.IntegerField(null=True)),
                ('rate', models.FloatField(null=True)),
            ],
        ),
        migrations.DeleteModel(
            name='Like',
        ),
    ]
