package com.practicum.playlistmaker.favorite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.favorite.domain.entity.FavoriteErrorType
import com.practicum.playlistmaker.favorite.domain.entity.FavoriteState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.search.ui.PLAYER_TRACKS_KEY
import com.practicum.playlistmaker.search.ui.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true
    private lateinit var favoriteAdapter: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onTrackListener = { track: Track ->
            if (clickDebounce()) {
                viewModel.onTrackClick(track)

            }
        }

        viewModel.fillData()

        favoriteAdapter = TrackAdapter(onTrackListener)
        binding.trackList.adapter = favoriteAdapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.Error -> showError(it.type)
                is FavoriteState.Content -> showContent(it.data)
                else -> {}
            }
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner) {
            openTrack(track = it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            R.id.action_mediaFragment_to_player, bundleOf(
                PLAYER_TRACKS_KEY to track
            )
        )

    }

    private fun showError(type: FavoriteErrorType) {
        favoriteAdapter.clear()
        binding.errorMsgImg.isVisible = true
        binding.errorMsgText.isVisible = true
        binding.errorMsgText.text = textFromErrorType(type)
    }

    private fun showContent(data: List<Track>) {
        binding.errorMsgImg.isVisible = false
        binding.errorMsgText.isVisible = false
        favoriteAdapter.setItems(data)
    }

    private fun textFromErrorType(type: FavoriteErrorType): String {
        return when (type) {
            FavoriteErrorType.EMPTY_MEDIA -> getString(R.string.empty_media_error)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false
            lifecycle.coroutineScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }

    companion object {
        fun newInstance() = FavoriteFragment()
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}