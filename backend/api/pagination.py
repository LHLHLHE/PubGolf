from rest_framework.pagination import PageNumberPagination


class GamesAndFriendsPagination(PageNumberPagination):
    page_size = 10
