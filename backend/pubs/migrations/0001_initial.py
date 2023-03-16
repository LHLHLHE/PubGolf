# Generated by Django 4.1.7 on 2023-03-16 10:50

from django.db import migrations, models
import pubs.validators


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Alcohol',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
            ],
        ),
        migrations.CreateModel(
            name='Menu',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
            ],
        ),
        migrations.CreateModel(
            name='Pub',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('address', models.CharField(max_length=255, verbose_name='Адрес')),
                ('phone', models.CharField(max_length=12, unique=True, validators=[pubs.validators.validate_phone], verbose_name='Номер телефона')),
                ('email', models.EmailField(max_length=254, unique=True, verbose_name='Почта')),
            ],
            options={
                'verbose_name': 'Паб',
                'verbose_name_plural': 'Пабы',
            },
        ),
    ]
