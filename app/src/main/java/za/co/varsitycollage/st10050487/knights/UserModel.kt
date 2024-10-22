package za.co.varsitycollage.st10050487.knights

data class UserModel(
    val userId: Int,
    val name: String,
    val surname: String,
    val profilePicture: ByteArray?,
    val email: String,
    val password: String?,
    val dateOfBirth: String,

)