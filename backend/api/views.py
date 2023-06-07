import datetime as dt
from collections import defaultdict

from django.shortcuts import get_object_or_404
from djoser.views import UserViewSet
from rest_framework import (
    viewsets,
    mixins,
    status,
    permissions,
    serializers,
    generics
)
from rest_framework.decorators import action
from rest_framework.permissions import SAFE_METHODS
from rest_framework.response import Response

from api.pagination import GamesAndFriendsPagination
from api.permissions import (
    PlayerPermission,
    IsOwnerOrReadOnly,
    IsPubOwnerOrReadOnly
)
from api.serializers import (
    FriendshipRequestSerializer,
    CustomUserCreateSerializer,
    CustomPasswordSerializer,
    CustomUserSerializer,
    CustomUserMeSerializer,
    SetEmailSerializer,
    PubSerializer,
    MenuSerializer,
    GameSerializer,
    GameCreateSerializer,
    StageSerializer,
    FinishGameSerializer,
)
from users.models import CustomUser, FriendshipRequest
from pubs.models import Pub, Menu
from games.models import Game, Stage, StageMenu, GameUser


class CustomUserViewSet(UserViewSet):

    def get_serializer_class(self):
        if self.action == 'create':
            return CustomUserCreateSerializer
        elif self.action == 'set_password':
            return CustomPasswordSerializer
        elif self.action == 'set_username':
            return serializers.SetUsernameSerializer
        elif self.action == 'set_email':
            return SetEmailSerializer
        elif self.action == 'me':
            return CustomUserMeSerializer
        return CustomUserSerializer

    def get_permissions(self):
        if self.action == 'retrieve':
            self.permission_classes = [permissions.IsAuthenticated]
        elif self.action == 'set_email':
            self.permission_classes = [permissions.CurrentUserOrAdmin]
        return super().get_permissions()

    def get_queryset(self):
        return CustomUser.objects.all()

    @action(['post'], detail=False, url_path='set_email')
    def set_email(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = self.request.user
        new_email = serializer.data['new_email']

        setattr(user, 'email', new_email)
        user.save()

        return Response(status=status.HTTP_204_NO_CONTENT)


class FriendshipRequestBaseViewSet(viewsets.GenericViewSet):
    serializer_class = FriendshipRequestSerializer
    permission_classes = (PlayerPermission,)


class FriendshipRequestViewSet(
    mixins.ListModelMixin,
    FriendshipRequestBaseViewSet
):
    def get_queryset(self):
        request = self.request

        if (
            'from-me' in request.query_params
            and request.query_params['from-me']
        ):
            return request.user.from_me_requests.all()

        return request.user.to_me_requests

    @action(methods=['post'], detail=True, url_path='accept')
    def accept_request(self, request, pk):
        friend_request = FriendshipRequest.objects.get(id=pk)

        if friend_request.to_user == request.user:
            friend_request.to_user.friends.add(friend_request.from_user)
            friend_request.from_user.friends.add(friend_request.to_user)
            friend_request.delete()
            return Response(
                {'status': 'Пользователь добавлен в друзья'},
                status=status.HTTP_201_CREATED
            )

        return Response(status=status.HTTP_404_NOT_FOUND)


class FriendshipRequestCreateDestroyViewSet(
    mixins.CreateModelMixin,
    mixins.DestroyModelMixin,
    FriendshipRequestBaseViewSet
):
    queryset = FriendshipRequest.objects.all()

    def get_serializer_context(self):
        context = super().get_serializer_context()
        context['friend_id'] = self.kwargs.get('user_id')

        return context

    def create(self, request, *args, **kwargs):
        user_id = self.kwargs.get('user_id')

        to_friend_request = FriendshipRequest.objects.filter(
            from_user_id=user_id,
            to_user=request.user
        )

        if to_friend_request.exists():
            to_user = get_object_or_404(CustomUser, id=user_id)
            request.user.friends.add(to_user)
            to_user.friends.add(request.user)
            to_friend_request.delete()
            return Response(
                {'status': 'Пользователь добавлен в друзья'},
                status=status.HTTP_201_CREATED
            )

        return super().create(request)

    def perform_create(self, serializer):
        request_user = self.request.user

        serializer.save(
            from_user=request_user,
            to_user=get_object_or_404(
                CustomUser,
                id=self.kwargs.get('user_id')
            )
        )

    @action(methods=['delete'], detail=True)
    def delete(self, request, user_id):
        get_object_or_404(
            FriendshipRequest,
            from_user=request.user,
            to_user=user_id
        ).delete()

        return Response(status=status.HTTP_204_NO_CONTENT)


class FriendViewSet(
    mixins.ListModelMixin,
    viewsets.GenericViewSet
):
    serializer_class = CustomUserSerializer

    def get_queryset(self):
        return self.request.user.friends.all()

    @action(methods=['delete'], detail=True)
    def delete(self, request, pk):
        friend_to_delete = get_object_or_404(CustomUser, id=pk)
        request.user.friends.remove(friend_to_delete)
        friend_to_delete.friends.remove(request.user)

        return Response(status=status.HTTP_204_NO_CONTENT)


class PubViewSet(viewsets.ModelViewSet):

    queryset = Pub.objects.all()
    serializer_class = PubSerializer
    permission_classes = (IsOwnerOrReadOnly,)

    def perform_create(self, serializer):
        serializer.save(company=self.request.user)


class MenuViewSet(
    mixins.ListModelMixin,
    mixins.CreateModelMixin,
    mixins.DestroyModelMixin,
    mixins.RetrieveModelMixin,
    mixins.UpdateModelMixin,
    viewsets.GenericViewSet
):

    serializer_class = MenuSerializer
    permission_classes = (IsPubOwnerOrReadOnly, )

    def get_queryset(self):
        return Menu.objects.filter(pub=self.kwargs.get('pub_id'))

    def retrieve(self, request, pk=None, pub_id=None):
        serializer = self.get_serializer(self.get_object())
        return Response(serializer.data)

    def perform_create(self, serializer):
        serializer.save(pub=Pub.objects.get(company=self.request.user))


class GameViewSet(viewsets.ModelViewSet):
    permission_classes = (PlayerPermission,)

    def get_serializer_class(self):
        if self.request.method in SAFE_METHODS:
            return GameSerializer
        return GameCreateSerializer

    def get_queryset(self):
        return Game.objects.filter(players=self.request.user)

    @action(methods=['delete'], detail=True)
    def delete(self, request, pk):
        get_object_or_404(
            Game,
            id=pk
        ).delete()

        return Response(status=status.HTTP_204_NO_CONTENT)


class StartGameAPIView(generics.RetrieveAPIView):
    permission_classes = (PlayerPermission,)

    def get(self, request, game_id):
        game = get_object_or_404(
            Game,
            id=int(game_id)
        )

        if game.status != 'created':
            return Response(
                data={'detail': 'Игра уже начата или завершена.'},
                status=status.HTTP_400_BAD_REQUEST
            )

        difficulty_level = game.difficulty_level
        budget_level = game.budget_level

        all_drinks = Menu.objects.all()
        all_drinks_count = all_drinks.count()
        interval = all_drinks_count / 3

        ordered_by_alcohol = all_drinks.order_by('alcohol_percent')
        if difficulty_level == 'underbeerman':
            drinks_ids = ordered_by_alcohol[
                :interval
            ].values_list('id', flat=True)
        elif difficulty_level == 'fan':
            drinks_ids = ordered_by_alcohol[
                interval:2*interval
                         ].values_list('id', flat=True)
        elif difficulty_level == 'freelanholic':
            drinks_ids = ordered_by_alcohol[
                2*interval:
                         ].values_list('id', flat=True)

        ordered_by_cost = Menu.objects.filter(
            id__in=drinks_ids
        ).order_by('cost')

        interval = interval / 3
        if budget_level == 'homeless':
            drinks = ordered_by_cost[:interval]
        elif budget_level == 'fan':
            drinks = ordered_by_cost[interval:2 * interval]
        elif budget_level == 'major':
            drinks = ordered_by_cost[2 * interval:]

        pubs_drinks = defaultdict(list)
        for drink in drinks:
            pubs_drinks[drink.pub_id].append(drink.id)

        stages = []
        for pub, pub_drinks in pubs_drinks.items():
            stages.append(
                Stage.objects.create(
                    game=game,
                    pub=Pub.objects.get(id=int(pub))
                )
            )

        for stage in stages:
            for drink in drinks:
                if stage.pub_id == drink.pub_id:
                    StageMenu.objects.create(
                        stage=stage,
                        drink=drink
                    )

        serialized_stages = StageSerializer(stages, many=True).data

        game.start_time = dt.datetime.now()
        game.status = 'started'
        game.save()

        return Response(
            data={'id': game.id, 'stages': serialized_stages},
            status=status.HTTP_200_OK
        )


class FinishGameAPIView(generics.CreateAPIView):
    permission_classes = (PlayerPermission,)
    serializer_class = FinishGameSerializer

    def post(self, request, game_id):
        games_users = GameUser.objects.filter(game_id=game_id)

        serializer = self.get_serializer(data=request.data, many=True)
        serializer.is_valid(raise_exception=True)

        serialized_stats = serializer.data

        for stat in serialized_stats:
            game_user = games_users.get(
                user_id=stat.get('user')
            )
            game_user.player_status = stat.get('player_status')
            game_user.save()

        game = get_object_or_404(
            Game,
            id=int(game_id)
        )
        game.finish_time = dt.datetime.now()
        game.status = 'finished'
        game.save()

        return Response(
            data=serialized_stats,
            status=status.HTTP_200_OK
        )
