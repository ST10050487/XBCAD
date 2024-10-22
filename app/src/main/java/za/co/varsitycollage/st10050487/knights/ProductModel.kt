package za.co.varsitycollage.st10050487.knights

data class ProductModel(
    val prodId: Int,
    val userId: Int,
    val prodName: String,
    val prodDescription: String,
    val prodPrice: Double,
    val prodPicture: ByteArray?
)
