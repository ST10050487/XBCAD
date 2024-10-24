package za.co.varsitycollage.st10050487.knights

data class FixtureModel(
    val userId: Int,
    val fixtureId: Int,
    val sport: String,
    val homeTeam: String,
    val awayTeam: String,
    val ageGroup: String,
    val league: String,
    val matchDate: String,
    val matchTime: String,
    val matchLocation: String,
    val homeLogo: ByteArray?,
    val awayLogo: ByteArray?,
    val matchDescription: String,
    val picture: ByteArray?,
)