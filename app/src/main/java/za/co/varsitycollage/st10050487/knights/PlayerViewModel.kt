package za.co.varsitycollage.st10050487.knights

data class PlayerProfile(
    val playerId: Int,
    val name: String,
    val surname: String,
    val nickname: String?,
    val age: Int,
    val grade: String?,
    val height: String?,
    val positions: List<String>,
    val teams: List<String>,
    val dateOfBirth: String?,
    val userId: Int
)