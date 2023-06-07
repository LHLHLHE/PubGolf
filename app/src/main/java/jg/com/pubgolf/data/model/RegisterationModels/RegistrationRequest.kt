package jg.com.pubgolf.data.model.RegisterationModels

data class RegistrationRequest(
    val username: String,
    val email: String,
    val password: String,
    val role: String,
    val bio: String,
    val registered_office: String,
    val phone_number: String,
    val re_password: String
)
