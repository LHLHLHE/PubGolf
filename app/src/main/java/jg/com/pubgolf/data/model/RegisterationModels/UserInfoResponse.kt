package jg.com.pubgolf.data.model.RegisterationModels

data class UserInfoResponse (
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val bio: String,
    val photo: String,
    val registered_office: String,
    val phone_number: String,
    val re_password: String
)