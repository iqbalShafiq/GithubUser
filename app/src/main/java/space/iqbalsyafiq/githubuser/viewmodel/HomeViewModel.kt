package space.iqbalsyafiq.githubuser.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.iqbalsyafiq.githubuser.model.SearchResponse
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.repository.SettingPreferences
import space.iqbalsyafiq.githubuser.repository.api.ApiConfig
import space.iqbalsyafiq.githubuser.repository.local.UserDatabase

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val dao = UserDatabase(getApplication()).userDao()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val pref = SettingPreferences.getInstance(application.dataStore)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    private val _users = MutableLiveData<List<UserResponse>>()
    val users: LiveData<List<UserResponse>> = _users

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getUsers() {
        _isLoading.value = true

        viewModelScope.launch {
            ApiConfig.getApiService().getListUsers().enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _isError.value = false

                        viewModelScope.launch {
                            val users = dao.getUsers()
                            val newUsers = response.body()?.map {
                                val user = dao.getUser(it.login)
                                if (users.contains(user)) it.isFavorite = true
                                it
                            }

                            _users.value = newUsers!!
                        }

                    } else {
                        Log.d(TAG, "onResponse: ${response.errorBody()}")
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true

                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun getUsers(username: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().searchUsers(username).enqueue(object: Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _isError.value = false

                        if (response.body()?.users!!.isEmpty()) {
                            _isEmpty.value = true
                        } else {
                            _isEmpty.value = false
                            viewModelScope.launch {
                                val users = dao.getUsers()
                                val newUsers = response.body()?.users?.map {
                                    val user = dao.getUser(it.login)
                                    if (users.contains(user)) it.isFavorite = true
                                    it
                                }

                                _users.value = newUsers!!
                            }
                        }

                    } else {
                        Log.d(TAG, "onResponse: ${response.errorBody()}")
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true

                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun storeToFavorite(user: UserResponse, query: String = "") {
        viewModelScope.launch {
            dao.insertUserToFavorite(user)
            if (query.isNotEmpty()) getUsers(query)
            else getUsers()
        }
    }

    fun deleteFromFavorite(username: String, query: String = "") {
        viewModelScope.launch {
            dao.deleteUser(username)
            if (query.isNotEmpty()) getUsers(query)
            else getUsers()
        }
    }
    
    companion object {
        const val TAG = "HomeVM"
    }
}