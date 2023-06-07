package jg.com.pubgolf.data.model.GameModels

data class NewGameRequest(
    val players: List<Int>,
    val name: String,
    val difficulty_level: String,
    val budget_level: String,
    val status: String,
    val start_time: String
)