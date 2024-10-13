package za.co.varsitycollage.st10050487.knights

data class FixtureModel(
    val fixtureId: Int,
    val sport: String,
    val homeTeam: String,
    val awayTeam: String,
    val ageGroup: String,
    val league: String,
    val setDate: String,
    val setTime: String,
    val setLocation: String,
    val homeLogo: ByteArray?,
    val awayLogo: ByteArray?,
    val matchId: Int,
    val userId: Int
)