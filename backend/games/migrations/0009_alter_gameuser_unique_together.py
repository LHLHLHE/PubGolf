# Generated by Django 4.1.7 on 2023-05-22 14:13

from django.conf import settings
from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('games', '0008_game_players'),
    ]

    operations = [
        migrations.AlterUniqueTogether(
            name='gameuser',
            unique_together={('user', 'game')},
        ),
    ]
