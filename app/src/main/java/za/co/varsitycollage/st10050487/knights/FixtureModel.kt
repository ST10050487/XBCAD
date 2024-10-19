package za.co.varsitycollage.st10050487.knights

data class FixtureModel(
    val userId: Int,
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
    val matchLocation: String, // New field
    val matchDate: String, // New field
    val matchTime: String, // New field
    val price: Double, // New field
    val matchDescription: String, // New field
    val picture: ByteArray?, // New field
    val timeId: Int, // New field
)