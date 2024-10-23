package za.co.varsitycollage.st10050487.knights

data class Match(
    val id: String,
    val team1: String,
    val team2: String,
    val startTime: String,
    val date: String,
    var status: MatchStatus
)

enum class MatchStatus {
    UPCOMING,
    IN_PROGRESS,
    FINISHED
}