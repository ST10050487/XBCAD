package za.co.varsitycollage.st10050487.knights

data class FixtureModel(
    val fixtureId: Int,
    val userId: Int,
    val sport: String,
    val homeTeam: String,
    val awayTeam: String,
    val ageGroup: String,
    val league: String,
    val matchLocation: String,
    val matchDate: String,
    val matchTime: String,
    val matchDescription: String,
    val homeLogo: ByteArray?,
    val awayLogo: ByteArray?,
    val picture: ByteArray?,
    val leagueId: Int,
    val isHomeGame: Boolean // Add this line
)