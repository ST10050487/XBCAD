package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlayerCardFragment : Fragment() {

    private lateinit var dbHelper: DBHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_card, container, false)

        dbHelper = DBHelper(requireContext())

        val userId = 121 // Replace with the actual user ID
        val playerProfile = dbHelper.getPlayerProfileByUserId(userId)

        if (playerProfile != null) {
            view.findViewById<TextView>(R.id.txtFullname).text = "${playerProfile.name} ${playerProfile.surname}"
            view.findViewById<TextView>(R.id.txtEmail).text = "Email: ${playerProfile.nickname}"
            view.findViewById<TextView>(R.id.txtGrade).text = "Grade: ${playerProfile.grade}"
            view.findViewById<TextView>(R.id.txtAge).text = "Age: ${playerProfile.age}"
        }

        return view
    }
}