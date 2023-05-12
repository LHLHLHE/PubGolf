# Generated by Django 4.1.7 on 2023-05-12 07:49

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0005_friendship_accepted'),
    ]

    operations = [
        migrations.CreateModel(
            name='FriendshipRequest',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
            ],
            options={
                'verbose_name': 'Заявка в друзья',
                'verbose_name_plural': 'Заявки в друзья',
            },
        ),
        migrations.AddField(
            model_name='customuser',
            name='friends',
            field=models.ManyToManyField(blank=True, to=settings.AUTH_USER_MODEL),
        ),
        migrations.DeleteModel(
            name='Friendship',
        ),
        migrations.AddField(
            model_name='friendshiprequest',
            name='from_user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='from_me_requests', to=settings.AUTH_USER_MODEL, verbose_name='Отправитель'),
        ),
        migrations.AddField(
            model_name='friendshiprequest',
            name='to_user',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, related_name='to_me_requests', to=settings.AUTH_USER_MODEL, verbose_name='Получатель'),
        ),
    ]
