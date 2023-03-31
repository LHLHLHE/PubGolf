# Generated by Django 4.1.7 on 2023-03-16 11:40

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('pubs', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='alcohol',
            name='menu',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='alcohols', to='pubs.menu', verbose_name='Меню'),
        ),
        migrations.AlterField(
            model_name='menu',
            name='pub',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='menus', to='pubs.pub', verbose_name='Паб'),
        ),
    ]
