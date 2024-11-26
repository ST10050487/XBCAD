package za.co.varsitycollage.st10050487.knights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _fixtureUpdated = MutableLiveData<Int>()
    val fixtureUpdated: LiveData<Int> get() = _fixtureUpdated

    fun updateFixture(fixtureId: Int) {
        _fixtureUpdated.value = fixtureId
    }
}