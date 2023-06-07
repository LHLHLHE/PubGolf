package jg.com.pubgolf.data.model

data class MeResponse (
    val id: Int,
    val username: String,
    val email: String,
    val role: String,
    val bio: String?,
    val photo: String?,
    val registered_office: String,
    val phone_number: String?,
    val is_friend: Boolean?
    )