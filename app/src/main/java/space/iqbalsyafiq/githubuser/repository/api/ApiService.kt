package space.iqbalsyafiq.githubuser.repository.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import space.iqbalsyafiq.githubuser.BuildConfig
import space.iqbalsyafiq.githubuser.model.SearchResponse
import space.iqbalsyafiq.githubuser.model.UserResponse

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun searchUsers(
        @Query("q") username: String
    ): Call<SearchResponse>

    @GET("users")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getListUsers(): Call<List<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<UserResponse>>
}