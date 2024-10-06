package za.co.varsitycollage.st10050487.knights

data class PlayerProfileModel(
    val playerId: Int,
    val name: String,
    val surname: String,
    val nickname: String,
    val age: Int,
    val grade: String,
    val height: String,
    val position: String,
    val dateOfBirth: String,
    val ageGroup: String,
    val userId: Int,
    val profilePicture: ByteArray?
)
