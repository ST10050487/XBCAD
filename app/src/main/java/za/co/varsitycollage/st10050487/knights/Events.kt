package za.co.varsitycollage.st10050487.knights

data class Events(
    val eventName: String,      // Corresponds to "EVENT_NAME"
    val eventDate: String,      // Corresponds to "EVENT_DATE"
    val eventTime: String,      // Corresponds to "EVENT_TIME"
    val eventLocation: String,   // Corresponds to "EVENT_LOCATION"
    val eventPrice: Double,      // Corresponds to "EVENT_PRICE"
    val imageResId: Int         // This can remain as is, assuming it is used for image resources
)