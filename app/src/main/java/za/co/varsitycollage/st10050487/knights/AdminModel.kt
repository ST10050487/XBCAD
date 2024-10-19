package za.co.varsitycollage.st10050487.knights


data class AdminModel(
    val userId: Int,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val dateOfBirth: String,
    val manageUsers: Boolean,
    val manageFixtures: Boolean,
    val interactionReports: Boolean,
    val createAdmin: Boolean,
    val profilePicture: ByteArray?
)