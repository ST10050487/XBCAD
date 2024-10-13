package za.co.varsitycollage.st10050487.knights

data class EventModel(
    val eventId: Int,
    val eventName: String,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val eventPrice: Double,
    var selected: Boolean = false
)