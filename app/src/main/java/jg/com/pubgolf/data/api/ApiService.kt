package jg.com.pubgolf.data.api

import jg.com.pubgolf.data.model.AuthModels.AuthRequest
import jg.com.pubgolf.data.model.AuthModels.AuthResponse
import jg.com.pubgolf.data.model.FriendModels.FriendRequestResponse
import jg.com.pubgolf.data.model.FriendModels.FriendResponse
import jg.com.pubgolf.data.model.GameModels.NewGameRequest
import jg.com.pubgolf.data.model.GameModels.NewGameResponse
import jg.com.pubgolf.data.model.GameModels.GameResponse
import jg.com.pubgolf.data.model.MeResponse
import jg.com.pubgolf.data.model.RegisterationModels.RegistrationRequest
import jg.com.pubgolf.data.model.RegisterationModels.UserInfoResponse
import retrofit2.http.*


interface ApiService {

    @POST("auth/token/login/")
    suspend fun auth(@Body request: AuthRequest): AuthResponse

    @POST("users/")
    suspend fun register(@Body request: RegistrationRequest): UserInfoResponse

    @GET("users/friends/")
    suspend fun getFriends(@Header("Authorization") token: String): List<FriendResponse>

    @GET("users/me/")
    suspend fun getMe(@Header("Authorization") token: String): MeResponse

    @GET("users/friendship-requests/")
    suspend fun getFriendsRequest(@Header("Authorization") token: String): List<FriendRequestResponse>

    @GET("users/friendship-requests?from-me=1")
    suspend fun getFriendsOutputRequest(@Header("Authorization") token: String): List<FriendRequestResponse>

    @GET("users/")
    suspend fun getAllUsers(@Header("Authorization") token: String): List<FriendResponse>

    @POST("users/{userId}/friend/")
    suspend fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): FriendRequestResponse


    @POST("users/friendship-requests/{id}/accept/")
    suspend fun acceptFriendRequest(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): FriendRequestResponse

    @DELETE("users/friends/{id}/delete/")
    suspend fun deleteFriend(@Header("Authorization") token: String, @Path("id") userId: Int)

    @GET("games/")
    suspend fun getAllGames(@Header("Authorization") token: String): List<GameResponse>

    @POST("games/")
    suspend fun createGame(
        @Header("Authorization") token: String,
        @Body request: NewGameRequest
    ): NewGameResponse
}