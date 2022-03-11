package space.iqbalsyafiq.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.repository.api.ApiConfig
import space.iqbalsyafiq.githubuser.repository.local.UserDatabase

class DetailViewModel(
    application: Application
): AndroidViewModel(application) {

    private val dao = UserDatabase(getApplication()).userDao()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite = _isFavorite

    fun getDetailUser(username: String) {
        _isLoading.value = true

        viewModelScope.launch {
            ApiConfig.getApiService().getDetailUser(username).enqueue(object: Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _user.value = response.body()
                        loadFavorite(username)
                        _isError.value = false
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }
            })
        }
    }

    private fun loadFavorite(username: String) {
        viewModelScope.launch {
            val user: UserResponse? = dao.getUser(username)
            _isFavorite.value = user?.isFavorite
            Log.d(TAG, "loadFavorite: ${user?.isFavorite}")
        }
    }

    fun setFavorite(
        user: UserResponse
    ) {
        viewModelScope.launch {
            val userOnDatabase: UserResponse? = dao.getUser(user.login)
            if (userOnDatabase == null) {
                dao.insertUserToFavorite(user)
            } else {
                dao.deleteUser(user.login)
            }

            // refresh detail data
            getDetailUser(user.login)
        }
    }

    companion object {
        private var TAG = DetailViewModel::class.java.simpleName
    }
}