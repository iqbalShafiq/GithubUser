package space.iqbalsyafiq.githubuser.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import space.iqbalsyafiq.githubuser.R
import space.iqbalsyafiq.githubuser.databinding.FragmentListBinding
import space.iqbalsyafiq.githubuser.model.UserResponse
import space.iqbalsyafiq.githubuser.view.adapter.UserListAdapter
import space.iqbalsyafiq.githubuser.viewmodel.ListViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get index from adapter's bundle
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        // get user list data
        if (index == FOLLOWERS_CODE) {
            viewModel.getFollowers(username!!)
        } else {
            viewModel.getFollowing(username!!)
        }

        // refresh by parentFragment
        if (parentFragment is DetailFragment) {
            (parentFragment as DetailFragment).binding.swipeRefresh.setOnRefreshListener {
                if (index == FOLLOWERS_CODE) viewModel.getFollowers(username)
                else viewModel.getFollowing(username)

                (parentFragment as DetailFragment).viewModel.getDetailUser(username)
            }
        }

        // observe live data
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewModel.followers.observe(viewLifecycleOwner) { followers ->
            followers?.let {
                binding.rvUser.adapter = UserListAdapter(
                    this,
                    it as ArrayList<UserResponse>
                )

                if (it.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = requireContext().getString(R.string.empty_followers)
                }
            }
        }

        viewModel.following.observe(viewLifecycleOwner) { following ->
            following?.let {
                binding.rvUser.adapter = UserListAdapter(
                    this,
                    it as ArrayList<UserResponse>
                )

                if (it.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = requireContext().getString(R.string.empty_following)
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
        const val FOLLOWERS_CODE = 1
    }
}