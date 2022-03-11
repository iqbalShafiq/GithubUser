package space.iqbalsyafiq.githubuser.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import space.iqbalsyafiq.githubuser.R
import space.iqbalsyafiq.githubuser.databinding.ItemUserBinding
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.view.fragment.FavoriteFragment
import space.iqbalsyafiq.githubuser.view.fragment.HomeFragment
import space.iqbalsyafiq.githubuser.view.fragment.ListFragment

class UserListAdapter(
    private val fragment: Fragment,
    private val userList: ArrayList<UserResponse>
) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var _binding: ItemUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        with(holder.itemView) {
            tvUsername.text = user.login

            // set user's avatar
            val avatar = user.avatarUrl ?: user.gravatarId
            Glide.with(context)
                .asBitmap()
                .circleCrop()
                .load(avatar)
                .into(ivUser)

            // fragment conditional
            if (fragment is ListFragment) {
                btnDetails.visibility = View.GONE
                ivLove.visibility = View.GONE
            }

            btnDetails.setOnClickListener {
                if (fragment is HomeFragment) {
                    fragment.goToDetail(user.login)
                } else if (fragment is FavoriteFragment) {
                    fragment.navigateToDetail(user.login)
                }
            }

            ivLove.setOnClickListener {
                if (fragment is HomeFragment) {
                    if (!user.isFavorite) {
                        fragment.setFavorite(user)
                    } else {
                        fragment.deleteFavorite(user.login)
                    }
                } else if (fragment is FavoriteFragment) {
                    fragment.deleteFavorite(user.login)
                }
            }

            // set favorite icon
            when {
                fragment is FavoriteFragment -> {
                    ivLove.setImageResource(R.drawable.ic_love_pressed)
                }
                user.isFavorite -> {
                    ivLove.setImageResource(R.drawable.ic_love_pressed)
                }
                else -> {
                    ivLove.setImageResource(R.drawable.ic_love)
                }
            }
        }
    }

    override fun getItemCount(): Int = userList.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _binding = null
    }
}