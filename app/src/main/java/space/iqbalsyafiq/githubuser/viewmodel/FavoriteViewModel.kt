package space.iqbalsyafiq.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.repository.local.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UserDatabase(getApplication()).userDao()

    private var _userList = MutableLiveData<List<UserResponse>>()
    val userList: LiveData<List<UserResponse>> = _userList

    private var _empty = MutableLiveData<Boolean>()
    val empty: LiveData<Boolean> = _empty

    fun getFavoriteUsers() {
        viewModelScope.launch {
            _userList.value = dao.getUsers()
            _empty.value = _userList.value!!.isEmpty()
        }
    }

    fun deleteFromFavorite(username: String) {
        viewModelScope.launch {
            dao.deleteUser(username)
            getFavoriteUsers()
        }
    }

}