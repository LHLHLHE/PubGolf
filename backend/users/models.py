from django.contrib.auth.models import AbstractUser
from django.core.validators import RegexValidator
from django.db import models

ROLE_CHOICES = (
    ('player', 'Аутентифицированный пользователь'),
    ('company', 'Компания')
)


class CustomUser(AbstractUser):
    email = models.EmailField(
        unique=True,
        max_length=254,
        verbose_name='Почта',
    )
    role = models.CharField(
        max_length=30,
        choices=ROLE_CHOICES,
        blank=True,
        default='player',
        verbose_name='Роль',
    )
    bio = models.TextField(
        blank=True,
        null=True,
        verbose_name='Доп. информация',
    )
    registered_office = models.CharField(
        max_length=256,
        blank=True,
        null=True,
        verbose_name='Юр. адрес'
    )
    phone_number = models.CharField(
        max_length=12,
        validators=[RegexValidator(regex=r'^\+7[0-9]{10}$')],
        unique=True,
        blank=True,
        null=True,
        verbose_name='Телефонный номер'
    )
    photo = models.ImageField(
        blank=True,
        null=True,
        verbose_name='Фото'
    )
    friends = models.ManyToManyField(
        'CustomUser',
        blank=True
    )

    class Meta:
        verbose_name = 'Пользователь'
        verbose_name_plural = 'Пользователи'

    @property
    def is_company(self):
        return self.role == 'company'


class FriendshipRequest(models.Model):
    from_user = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        related_name='from_me_requests',
        verbose_name='Отправитель'
    )
    to_user = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        related_name='to_me_requests',
        verbose_name='Получатель'
    )

    class Meta:
        unique_together = ('from_user', 'to_user')
        verbose_name = 'Заявка в друзья'
        verbose_name_plural = 'Заявки в друзья'

    def __str__(self):
        return f'{self.from_user} --- {self.to_user}'
