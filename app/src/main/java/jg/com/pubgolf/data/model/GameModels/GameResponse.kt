package jg.com.pubgolf.data.model.GameModels

data class GameResponse(
    val id: Int,
    val stats: List<Stats>,
    val name: String,
    val difficulty_level: String,
    val budget_level: String,
    val status: String,
    val start_time: String,
    val finish_time: String
)

data class Stats(
    val user: Int,
    val player_status: String
)