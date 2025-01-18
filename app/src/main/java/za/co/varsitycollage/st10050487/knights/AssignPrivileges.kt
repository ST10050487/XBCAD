package za.co.varsitycollage.st10050487.knights

import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AssignPrivileges : AppCompatActivity() {
    private lateinit var userListView: ListView
    private lateinit var privilegesContainer: LinearLayout
    private lateinit var assignPrivilegesButton: Button
    private lateinit var searchView: SearchView
    private lateinit var dbHelper: DBHelper
    private lateinit var users: List<UserModel>
    private lateinit var privileges: List<String>
    private var selectedUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_assign_privileges)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DBHelper(this)
        userListView = findViewById(R.id.user_list_view)
        privilegesContainer = findViewById(R.id.privileges_container)
        assignPrivilegesButton = findViewById(R.id.assign_privileges_button)
        searchView = findViewById(R.id.search_view)

        loadUsers()
        loadPrivileges()

        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = users[position]
            selectedUserId = selectedUser.userId
            Toast.makeText(this, "Selected user: ${selectedUser.name} ${selectedUser.surname}", Toast.LENGTH_SHORT).show()
        }

        assignPrivilegesButton.setOnClickListener { assignPrivileges() }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchUsers(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchUsers(newText)
                }
                return false
            }
        })
    }

    private fun loadUsers() {
        users = dbHelper.getAllUsers()
        val adapter = UserAdapter(this, users)
        userListView.adapter = adapter
    }

    private fun loadPrivileges() {
        privileges = dbHelper.getAllPrivileges()
        for (privilege in privileges) {
            val checkBox = CheckBox(this)
            checkBox.text = privilege
            privilegesContainer.addView(checkBox)
        }
    }

    private fun assignPrivileges() {
        if (selectedUserId == -1) {
            Toast.makeText(this, "Please select a user", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedPrivilegeIds = mutableListOf<Int>()
        for (i in 0 until privilegesContainer.childCount) {
            val checkBox = privilegesContainer.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                selectedPrivilegeIds.add(i + 1) // Assuming privilege IDs are 1-based
            }
        }
        dbHelper.assignPrivilegesToUser(selectedUserId, selectedPrivilegeIds)
        Toast.makeText(this, "Privileges assigned successfully", Toast.LENGTH_SHORT).show()
    }

    private fun searchUsers(query: String) {
        users = dbHelper.searchUsers(query)
        val adapter = UserAdapter(this, users)
        userListView.adapter = adapter
    }
}