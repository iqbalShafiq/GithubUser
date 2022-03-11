package space.iqbalsyafiq.githubuser.view.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import space.iqbalsyafiq.githubuser.R
import space.iqbalsyafiq.githubuser.databinding.FragmentDetailBinding
import space.iqbalsyafiq.githubuser.view.adapter.SectionPagerAdapter
import space.iqbalsyafiq.githubuser.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    val binding get() = _binding!!
    val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set view pager adapter and tab layout
        val sectionPagerAdapter = SectionPagerAdapter(this, args.username)
        with(binding) {
            viewPager.adapter = sectionPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        // get details data
        viewModel.getDetailUser(args.username)

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
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                binding.swipeRefresh.isRefreshing = it
                binding.btnLove.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
                binding.btnLove.visibility = if (it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            binding.btnLove.apply {
                if (isFavorite != null) {
                    setImageResource(R.drawable.ic_love_pressed)
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.light_secondary
                        )
                    )
                } else {
                    setImageResource(R.drawable.ic_love)
                    backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green_accent
                        )
                    )
                }
            }
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                with(binding) {
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvUsername.text = it.login
                    tvRepository.text =
                        requireContext().getString(R.string.repositories, it.repositories)
                    tvGist.text = requireContext().getString(R.string.gists, it.gist)

                    // optional full name, company, and location
                    if (!it.company.isNullOrEmpty()) {
                        tvFullName.text = it.name
                    } else {
                        tvFullName.visibility = View.GONE
                    }

                    if (!it.company.isNullOrEmpty()) {
                        tvCompany.text = it.company
                    } else {
                        tvCompany.visibility = View.GONE
                    }

                    if (!it.company.isNullOrEmpty()) {
                        tvLocation.text = it.location
                    } else {
                        tvLocation.visibility = View.GONE
                    }

                    // set image
                    val avatar = it.avatarUrl ?: it.gravatarId
                    Glide.with(requireContext())
                        .asBitmap()
                        .circleCrop()
                        .load(avatar)
                        .into(ivUser)

                    // set favorite state
                    btnLove.setOnClickListener {
                        viewModel.setFavorite(user)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}