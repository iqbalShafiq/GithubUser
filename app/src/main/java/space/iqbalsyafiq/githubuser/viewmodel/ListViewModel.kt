package space.iqbalsyafiq.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.repository.api.ApiConfig

class ListViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _followers = MutableLiveData<List<UserResponse>>()
    val followers: LiveData<List<UserResponse>> = _followers

    private val _following = MutableLiveData<List<UserResponse>>()
    val following: LiveData<List<UserResponse>> = _following

    fun getFollowers(username: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().getFollowers(username).enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _followers.value = response.body()
                        _isError.value = false
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }
    }

    fun getFollowing(username: String) {
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().getFollowing(username).enqueue(object: Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _following.value = response.body()
                        _isError.value = false
                    } else {
                        _isError.value = true
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                }

            })
        }
    }
}