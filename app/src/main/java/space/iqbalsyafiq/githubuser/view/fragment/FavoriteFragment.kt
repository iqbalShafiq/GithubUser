package space.iqbalsyafiq.githubuser.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import space.iqbalsyafiq.githubuser.databinding.FragmentFavoriteBinding
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.view.adapter.UserListAdapter
import space.iqbalsyafiq.githubuser.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get favorite users
        viewModel.getFavoriteUsers()

        // set view
        with(binding) {
            btnBack.setOnClickListener {
                activity?.onBackPressed()
            }
        }

        // observe live data
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.userList.observe(viewLifecycleOwner) { users ->
            users?.let {
                binding.rvUser.adapter = UserListAdapter(
                    this,
                    it as ArrayList<UserResponse>
                )
            }
        }

        viewModel.empty.observe(viewLifecycleOwner) { isEmpty ->
            isEmpty?.let {
                binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }
    }

    fun navigateToDetail(username: String) {
        val action = FavoriteFragmentDirections.navigateToDetailFragmentFromFavorite()
        action.username = username
        Navigation.findNavController(binding.root).navigate(action)
    }

    fun deleteFavorite(username: String) {
        viewModel.deleteFromFavorite(username)
    }
}