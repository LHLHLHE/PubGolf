from django.db import models

from pubs.models import Pub, Menu
from users.models import CustomUser

DIFFICULTY_LEVELS = (
    ('underbeerman', 'Подпивасник'),
    ('fan', 'Любитель'),
    ('freelanholic', 'Фриланголик')
)
BUDGET_LEVELS = (
    ('homeless', 'Бомж'),
    ('fan', 'Любитель'),
    ('major', 'Мажор')
)
GAME_STATUSES = (
    ('created', 'Создана комната'),
    ('started', 'Игра стартовала'),
    ('finished', 'Игра завершилась')
)
PLAYER_STATUSES = (
    ('playing', 'Играет'),
    ('won', 'Выиграл'),
    ('lost', 'Проиграл')
)


class Game(models.Model):
    """Модель комнаты."""

    name = models.CharField(
        unique=True,
        max_length=150,
        verbose_name='Название комнаты'
    )
    difficulty_level = models.CharField(
        max_length=50,
        choices=DIFFICULTY_LEVELS,
        verbose_name='Уровень сложности'
    )
    budget_level = models.CharField(
        max_length=50,
        choices=BUDGET_LEVELS,
        verbose_name='Уровень бюджета'
    )
    status = models.CharField(
        max_length=50,
        choices=GAME_STATUSES,
        default='created',
        verbose_name='Статус игры'
    )
    start_time = models.DateTimeField(
        blank=True,
        null=True,
        verbose_name='Время старта игры'
    )
    finish_time = models.DateTimeField(
        blank=True,
        null=True,
        verbose_name='Время завершения игры'
    )
    players = models.ManyToManyField(
        CustomUser,
        through='GameUser',
        verbose_name='Игроки',
    )

    class Meta:
        verbose_name = 'Игра'
        verbose_name_plural = 'Игры'

    def __str__(self):
        return self.name


class GameUser(models.Model):
    user = models.ForeignKey(
        CustomUser,
        on_delete=models.CASCADE,
        verbose_name='Игрок'
    )
    game = models.ForeignKey(
        Game,
        on_delete=models.CASCADE,
        verbose_name='Комната'
    )
    player_status = models.CharField(
        max_length=50,
        choices=PLAYER_STATUSES,
        default='playing',
        verbose_name='Статус игрока'
    )

    class Meta:
        unique_together = ('user', 'game')

    def __str__(self):
        return f'{self.user} --- {self.game}: {self.player_status}'


class Stage(models.Model):
    game = models.ForeignKey(
        Game,
        on_delete=models.CASCADE
    )
    pub = models.ForeignKey(
        Pub,
        on_delete=models.CASCADE
    )

    class Meta:
        unique_together = ('game', 'pub')

    def __str__(self):
        return f'{self.game} --- {self.pub}'


class StageMenu(models.Model):
    stage = models.ForeignKey(
        Stage,
        on_delete=models.CASCADE
    )
    drink = models.ForeignKey(
        Menu,
        on_delete=models.CASCADE
    )

    class Meta:
        unique_together = ('stage', 'drink')

    def __str__(self):
        return f'{self.stage} --- {self.drink}'
