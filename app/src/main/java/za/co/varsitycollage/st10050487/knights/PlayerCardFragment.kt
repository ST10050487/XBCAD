package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import za.co.varsitycollage.st10050487.knights.databinding.FragmentPlayerCardBinding

class PlayerCardFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private var userId: Int = 0
    private lateinit var binding: FragmentPlayerCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DBHelper(requireContext())

        // Getting the userId from the arguments
        userId = arguments?.getInt("USER_ID") ?: 0
        val playerProfile = dbHelper.getPlayerProfile(userId)
        val user = dbHelper.getUser(userId)

        binding.checkSelect.visibility = View.GONE
        if (playerProfile != null) {
            binding.txtFullname.text = "${playerProfile.name} ${playerProfile.surname}"
            binding.txtNickname.text = "Nickname: ${playerProfile.nickname}"
            binding.txtPosition.text = "Position: ${playerProfile.position}"
            binding.txtAge.text = "Age: ${playerProfile.age}"
        } else if (user != null) {
            binding.txtFullname.text = "${user.name} ${user.surname}"
            binding.txtNickname.text = "Email: ${user.email}"
            binding.txtPosition.text = "Date of Birth: ${user.dateOfBirth}"
            binding.txtAge.text = "Age: ${user.dateOfBirth}"
        } else {
            binding.txtFullname.text = "Player data not found"
        }
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