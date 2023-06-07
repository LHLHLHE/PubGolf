from djoser.serializers import (
    UserSerializer,
    UserCreatePasswordRetypeSerializer,
    PasswordRetypeSerializer,
    CurrentPasswordSerializer
)
from rest_framework import serializers
from rest_framework.generics import get_object_or_404

from users.models import CustomUser, FriendshipRequest
from pubs.models import Pub, Menu
from games.models import Game, GameUser, Stage, StageMenu


class CustomUserCreateSerializer(UserCreatePasswordRetypeSerializer):

    class Meta:
        model = CustomUser
        fields = (
            'id',
            'username',
            'email',
            'password',
            'role',
            'bio',
            'photo',
            'registered_office',
            'phone_number'
        )
        required_fields = (
            'username',
            'email',
            'password'
        )


class CustomUserSerializer(UserSerializer):
    is_friend = serializers.SerializerMethodField()

    class Meta:
        model = CustomUser
        fields = (
            'id',
            'username',
            'role',
            'bio',
            'photo',
            'registered_office',
            'phone_number',
            'is_friend'
        )

    def get_is_friend(self, obj):
        request_user = self.context['request'].user

        return (request_user.is_authenticated
                and obj in request_user.friends.all())


class CustomUserMeSerializer(CustomUserSerializer):

    class Meta:
        model = CustomUser
        fields = (
            'id',
            'username',
            'email',
            'role',
            'bio',
            'photo',
            'registered_office',
            'phone_number',
            'is_friend'
        )


class CustomPasswordSerializer(PasswordRetypeSerializer):
    current_password = serializers.CharField(required=True)


class SetEmailSerializer(
    serializers.ModelSerializer,
    CurrentPasswordSerializer
):
    new_email = serializers.EmailField(required=True)

    class Meta:
        model = CustomUser
        fields = ('new_email', 'current_password')

    def validate(self, attrs):
        user = self.context['request'].user or self.user
        assert user is not None

        if attrs['new_email'] == user.email:
            raise serializers.ValidationError({
                'new_email': 'Введена та же почта, что и в аккаунте'
            })
        return super().validate(attrs)


class FriendshipRequestSerializer(serializers.ModelSerializer):
    from_user = serializers.CharField(
        source='from_user.username',
        read_only=True
    )
    to_user = serializers.CharField(
        source='to_user.username',
        read_only=True
    )

    class Meta:
        model = FriendshipRequest
        fields = '__all__'

    def validate(self, data):
        user = CustomUser.objects.get(id=self.context['request'].user.id)
        friend = CustomUser.objects.get(id=self.context['friend_id'])

        if user == friend:
            raise serializers.ValidationError(
                'Нельзя добавить в друзья самого себя!'
            )

        if friend.is_company:
            raise serializers.ValidationError(
                'Нельзя добавить в друзья аккаунт комапнии!'
            )

        if FriendshipRequest.objects.filter(
            from_user=user,
            to_user=friend
        ).exists():
            raise serializers.ValidationError('Заявка уже отправлена!')

        if friend in user.friends.all():
            raise serializers.ValidationError(
                'Пользователь уже есть у Вас в друзьях!'
            )

        return data


class PubSerializer(serializers.ModelSerializer):
    company = serializers.SlugRelatedField(
        slug_field='username',
        read_only=True
    )

    class Meta:
        model = Pub
        fields = (
            'id',
            'name',
            'pub_address',
            'company'
        )


class PubInStageSerializer(serializers.ModelSerializer):

    class Meta:
        model = Pub
        fields = (
            'name',
            'pub_address'
        )


class MenuSerializer(serializers.ModelSerializer):
    pub = serializers.IntegerField(
        source='pub.id',
        read_only=True
    )

    class Meta:
        model = Menu
        fields = (
            'id',
            'alcohol_name',
            'alcohol_percent',
            'cost',
            'pub'
        )

    def validate_alcohol_name(self, alcohol_name):
        user = CustomUser.objects.get(id=self.context['request'].user.id)
        pub = Pub.objects.get(company=user)
        if Menu.objects.filter(pub=pub, alcohol_name=alcohol_name).exists():
            raise serializers.ValidationError(
                'Такой алкоголь уже есть в меню.'
            )
        return alcohol_name

    def validate_cost(self, cost):
        if cost < 0:
            raise serializers.ValidationError(
                'Цена не может быть меньше 0.'
            )
        return cost

    def validate_alcohol_percent(self, alcohol_percent):
        if alcohol_percent > 100:
            raise serializers.ValidationError(
                'Процент содержания спирта не может быть больше 100%.'
            )
        elif alcohol_percent < 0:
            raise serializers.ValidationError(
                'Процент содержания спирта не может быть меньше 0%.'
            )
        return alcohol_percent


class GameCreateSerializer(serializers.ModelSerializer):
    players = serializers.ListField(
        child=serializers.PrimaryKeyRelatedField(
            queryset=CustomUser.objects.all()
        ),
        write_only=True
    )

    class Meta:
        model = Game
        fields = '__all__'
        required_fields = (
            'name',
            'difficulty_level',
            'budget_level',
            'players'
        )

    def create(self, validated_data):
        players = validated_data.pop('players')
        game = Game.objects.create(**validated_data)

        for player in players:
            GameUser.objects.create(
                game=game,
                user=player
            )

        return game

    def update(self, instance, validated_data):
        players = validated_data.pop('players')
        instance.name = validated_data.get('name', instance.name)
        instance.difficulty_level = validated_data.get(
            'difficulty_level',
            instance.difficulty_level
        )
        instance.budget_level = validated_data.get(
            'budget_level',
            instance.budget_level
        )
        instance.save()

        GameUser.objects.filter(game=instance).delete()
        for player in players:
            GameUser.objects.create(
                user=get_object_or_404(
                    CustomUser,
                    id=player.id),
                game=instance
            )

        return instance


class MenuInStageSerializer(serializers.ModelSerializer):

    class Meta:
        model = Menu
        fields = (
            'id',
            'alcohol_name',
            'alcohol_percent',
            'cost'
        )


class StageSerializer(serializers.ModelSerializer):
    pub = PubInStageSerializer(read_only=True)
    drinks = serializers.SerializerMethodField()

    class Meta:
        model = Stage
        fields = (
            'id',
            'pub',
            'drinks'
        )

    def get_drinks(self, obj):
        drinks_ids = StageMenu.objects.filter(
            stage=obj
        ).values_list('drink_id')
        drinks = Menu.objects.filter(id__in=drinks_ids)

        return MenuInStageSerializer(drinks, many=True).data


class FinishGameSerializer(serializers.ModelSerializer):
    class Meta:
        model = GameUser
        fields = ('user', 'player_status')


class GameSerializer(serializers.ModelSerializer):
    stats = serializers.SerializerMethodField()

    class Meta:
        model = Game
        exclude = ('players',)

    def get_stats(self, obj):
        return FinishGameSerializer(
            GameUser.objects.filter(game=obj),
            many=True
        ).data
