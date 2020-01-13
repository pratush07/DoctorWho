# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('doc', '0003_auto_20151116_0226'),
    ]

    operations = [
        migrations.RenameField(
            model_name='doctor',
            old_name='profilepic',
            new_name='profilePic',
        ),
        migrations.RenameField(
            model_name='user',
            old_name='profilepic',
            new_name='profilePic',
        ),
    ]
