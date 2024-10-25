// SharedViewModel.kt
package za.co.varsitycollage.st10050487.knights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _fixtureId = MutableLiveData<Long>()
    val fixtureId: LiveData<Long> get() = _fixtureId

    fun setFixtureId(id: Long) {
        _fixtureId.value = id
    }
}