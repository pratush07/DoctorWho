# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='doclist',
            old_name='usrid',
            new_name='usrID',
        ),
    ]
