from django.contrib import admin

from .models import Game, GameUser


@admin.register(Game)
class GameAdmin(admin.ModelAdmin):
    list_display = ('name', 'status')
    search_fields = ('name',)


@admin.register(GameUser)
class GameUserAdmin(admin.ModelAdmin):
    list_display = ('user', 'game')
    search_fields = ('game',)
