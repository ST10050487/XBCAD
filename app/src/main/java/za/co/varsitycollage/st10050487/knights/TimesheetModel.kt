package za.co.varsitycollage.st10050487.knights


data class TimesheetModel(
    val timeId: Int,
    val fixtureId: Int,
    val matchstatus: Int?,
    val meetTime: String?,
    val busDepartureTime: String?,
    val busReturnTime: String?,
    val message: String?,
    val manOfTheMatch: String?,
    val homeScore: Int?,
    val awayScore: Int?
)