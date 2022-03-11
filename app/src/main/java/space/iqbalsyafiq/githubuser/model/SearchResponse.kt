package space.iqbalsyafiq.githubuser.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items")
    val users: List<UserResponse>
)