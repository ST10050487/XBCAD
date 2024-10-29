package za.co.varsitycollage.st10050487.knights

data class PlayerProfileView(
    val name: String,
    val surname: String,
    val email: String, // Corrected to String
    val age: Int,      // Corrected to Int
    val grade: String
)