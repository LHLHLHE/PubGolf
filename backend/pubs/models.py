from django.db import models

from users.models import CustomUser


class Pub(models.Model):
    """Модель паба."""

    name = models.CharField(
        max_length=255,
        verbose_name='Название'
    )
    pub_address = models.CharField(
        max_length=255,
        verbose_name='Адрес'
    )
    company = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        verbose_name='Компания',
        related_name='pubs'
    )

    class Meta:
        verbose_name = 'Паб'
        verbose_name_plural = 'Пабы'

    def __str__(self):
        return f'{self.company.username} --- {self.name}'


class Menu(models.Model):
    """Модель меню."""

    alcohol_name = models.CharField(
        max_length=50,
        verbose_name='Название',
    )
    alcohol_percent = models.PositiveIntegerField(
        verbose_name='Процент спирта',
    )
    cost = models.PositiveIntegerField(
        verbose_name='Цена',
    )
    pub = models.ForeignKey(
        Pub,
        on_delete=models.CASCADE,
        related_name='menu',
        verbose_name='Меню'
    )

    class Meta:
        verbose_name = 'Меню'
        verbose_name_plural = 'Меню'

    def __str__(self):
        return self.alcohol_name
