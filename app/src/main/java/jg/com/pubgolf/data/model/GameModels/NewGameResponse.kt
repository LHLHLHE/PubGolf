package jg.com.pubgolf.data.model.GameModels

data class NewGameResponse(
    val id: Int,
    val name: String,
    val difficulty_level: String,
    val budget_level: String,
    val status: String,
    val start_time: String,
    val finish_time: Nothing? = null
)