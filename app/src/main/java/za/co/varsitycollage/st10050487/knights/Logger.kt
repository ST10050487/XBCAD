package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.util.Log

object Logger {
    private const val TAG = "AppLogger"
//
//    fun initialize(analyticsInstance:  {
//        analytics = analyticsInstance
//    }

    fun logSuspiciousActivity(message: String) {
        Log.w(TAG, message)
        //FirebaseCrashlytics.getInstance().log(message)
    }

    fun logFailedLoginEvent(email: String) {
        val bundle = Bundle().apply {
            //putString(FirebaseAnalytics.Param.METHOD, "email")
            putString("email", email)
        }
        //analytics.logEvent("login_failed", bundle)

    }
}