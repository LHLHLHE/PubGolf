# Generated by Django 4.1.7 on 2023-05-22 14:13

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0001_initial'),
    ]

    operations = [
        migrations.AlterUniqueTogether(
            name='friendshiprequest',
            unique_together={('from_user', 'to_user')},
        ),
    ]
