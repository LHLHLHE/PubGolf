package jg.com.pubgolf.data.model.FriendModels

data class FriendResponse(
    val id: Int,
    val username: String,
    val role: String,
    val bio: String,
    val photo: String,
    val registeredOffice: String,
    val phoneNumber: String,
    val is_friend: String
)