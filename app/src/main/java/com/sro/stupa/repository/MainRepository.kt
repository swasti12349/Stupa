import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sro.stupa.Model.User
import com.sro.stupa.Model.UserDao

class MainRepository(private val userDao: UserDao) {

    private val _userListLiveData = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>> = _userListLiveData

    // Your existing functions remain unchanged
    init {
        refreshUserList()
    }

    // Inserts a new user into the database.
    fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    // Returns the user with the given credentials
    fun getUserByEmailAndPassword(email: String, password: String): User? {
        return userDao.getUserByEmailAndPassword(email, password)
    }
    // fetch all the users from the database

    fun getAllUsers(): LiveData<List<User>> {

        return userDao.getAllUsers()
    }

    private fun refreshUserList() {
        _userListLiveData.postValue(userDao.getAllUsers().value)
    }
}
