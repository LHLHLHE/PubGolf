from django.contrib import admin

from .models import Pub, Menu


@admin.register(Pub)
class PubAdmin(admin.ModelAdmin):
    list_display = ('name', 'pub_address', 'company')
    search_fields = ('pub_address',)
    list_filter = ('company__username',)


@admin.register(Menu)
class MenuAdmin(admin.ModelAdmin):
    list_display = ('pk', 'alcohol_name', 'alcohol_percent', 'cost', 'pub')
    search_fields = ('alcohol_name',)
    list_filter = ('pub__name', 'alcohol_percent')
