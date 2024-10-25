package za.co.varsitycollage.st10050487.knights

data class Match(
    val fixtureId: String,
    val homeTeam: String,
    val awayTeam: String,
    val startTime: String,
    val date: String,
    val homeLogo: ByteArray?,
    val awayLogo: ByteArray?,
    val status: MatchStatus
)
// Other methods...
enum class MatchStatus {
    UPCOMING,
    IN_PROGRESS,
    FINISHED
}