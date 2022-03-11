package space.iqbalsyafiq.githubuser.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import space.iqbalsyafiq.githubuser.R
import space.iqbalsyafiq.githubuser.databinding.FragmentHomeBinding
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.view.adapter.UserListAdapter
import space.iqbalsyafiq.githubuser.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get list of users
        viewModel.getUsers()

        // set view
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.getUsers()
                searchUsername.setQuery("", false)
            }

            searchUsername.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query?.isNotEmpty()!!) {
                        viewModel.getUsers(query)
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })

            btnLove.setOnClickListener {
                val action = HomeFragmentDirections.navigateToFavoriteFragment()
                Navigation.findNavController(binding.root).navigate(action)
            }

            switchTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.saveThemeSetting(isChecked)
            }
        }

        // observe live data
        observeLiveData()
    }

    fun setFavorite(user: UserResponse) {
        viewModel.storeToFavorite(
            user,
            binding.searchUsername.query.toString()
        )
    }

    fun deleteFavorite(username: String) {
        viewModel.deleteFromFavorite(
            username,
            binding.searchUsername.query.toString()
        )
    }

    private fun observeLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                binding.swipeRefresh.isRefreshing = it
                binding.rvUser.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
                binding.rvUser.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { empty ->
            empty?.let {
                binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
                binding.rvUser.visibility = if (it) View.GONE else View.VISIBLE

                if (it) {
                    binding.tvError.text = getString(R.string.empty_message)
                }
            }
        }

        viewModel.users.observe(viewLifecycleOwner) { userList ->
            userList?.let {
                binding.rvUser.adapter = UserListAdapter(
                    this,
                    it as ArrayList<UserResponse>
                )
            }
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val query = binding.searchUsername.query
        if (query.isNotEmpty()) {
            viewModel.getUsers(query.toString())
        } else {
            viewModel.getUsers()
        }
    }

    fun goToDetail(username: String) {
        val action = HomeFragmentDirections.navigateToDetailFragment()
        action.username = username
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}