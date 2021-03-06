package com.rafal.unsplashwallpapers.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.rafal.unsplashwallpapers.databinding.FragmentPhotosBinding
import com.rafal.unsplashwallpapers.view.adapters.PhotosPagingAdapter
import com.rafal.unsplashwallpapers.view.adapters.ResultsLoadStateAdapter
import com.rafal.unsplashwallpapers.view.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosFragment : Fragment(), PhotosPagingAdapter.onPhotoClickListener {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagingAdapter = PhotosPagingAdapter(this)
        val recyclerView = binding.photosRv

        pagingAdapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                binding.apply {
                    photoFailLayout.visibility = View.VISIBLE
                    photosRv.visibility = View.GONE
                }
            } else {
                binding.apply {
                    photoFailLayout.visibility = View.GONE
                    photosRv.visibility = View.VISIBLE
                }
            }
        }

        binding.photoRetryBtn.setOnClickListener {
            pagingAdapter.retry()
        }

        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = ResultsLoadStateAdapter { pagingAdapter.retry() },
            footer = ResultsLoadStateAdapter { pagingAdapter.retry() }
        )

        binding.fabPhotos.setOnClickListener {
            recyclerView.scrollToPosition(0)
        }

        viewModel.photoLiveData.observe(viewLifecycleOwner) {
            binding.photosEmptyIv.visibility = View.GONE
            pagingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onPhotoClick(photoID: String) {
        val action =
            SearchFragmentDirections.actionSearchFragmentToPhotoDetailsFragment(photoID = photoID)
        findNavController().navigate(action)
    }

}