package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlayerCardFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player_card, container, false)

        dbHelper = DBHelper(requireContext())
        // Getting the userId from the arguments
        userId = arguments?.getInt("USER_ID") ?: 0
        val playerProfile = dbHelper.getPlayerProfile(userId)

        view.findViewById<TextView>(R.id.txtFullname).text = "Jojn Marcus"
        view.findViewById<TextView>(R.id.txtEmail).text = "Email: JM@gmail.com"
        view.findViewById<TextView>(R.id.txtGrade).text = "Grade: 11"
        view.findViewById<TextView>(R.id.txtAge).text = "Age: 17 years old"

        //REAL DATA PULL FROM DATABASE
        /*
        if (playerProfile != null) {
            view.findViewById<TextView>(R.id.txtFullname).text = "${playerProfile.name} ${playerProfile.surname}"
            view.findViewById<TextView>(R.id.txtEmail).text = "Email: ${playerProfile.nickname}"
            view.findViewById<TextView>(R.id.txtGrade).text = "Grade: ${playerProfile.grade}"
            view.findViewById<TextView>(R.id.txtAge).text = "Age: ${playerProfile.age}"
        }
        */

        return view
    }

    companion object {
        fun newInstance(userId: Int): PlayerCardFragment {
            val fragment = PlayerCardFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }
}