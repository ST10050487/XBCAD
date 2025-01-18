package za.co.varsitycollage.st10050487.knights

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class UserAdapter(private val context: Context, private val users: List<UserModel>) : BaseAdapter() {

    override fun getCount(): Int = users.size

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = users[position].userId.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_list_item, parent, false)
        val user = users[position]

        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)

        nameTextView.text = "${user.name} ${user.surname}"
        emailTextView.text = user.email
        // Assuming profilePicture is a byte array
        if (user.profilePicture != null) {
            val bitmap = BitmapFactory.decodeByteArray(user.profilePicture, 0, user.profilePicture.size)
            profileImageView.setImageBitmap(bitmap)
        } else {
            profileImageView.setImageResource(R.drawable.ic_profile_icon) // Default profile picture
        }

        return view
    }
}