package space.iqbalsyafiq.githubuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    val company: String?,
    val followers: Int?,
    val following: Int?,
    @SerializedName("gravatar_id")
    val gravatarId: String?,
    val location: String?,
    @PrimaryKey
    val login: String,
    val name: String?,
    @SerializedName("public_gists")
    val gist: Int?,
    @SerializedName("public_repos")
    val repositories: Int?
) {
    var isFavorite = false
}