# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0006_like'),
    ]

    operations = [
        migrations.RenameField(
            model_name='like',
            old_name='com_from',
            new_name='like_from',
        ),
        migrations.RenameField(
            model_name='like',
            old_name='com_to',
            new_name='like_to',
        ),
    ]
