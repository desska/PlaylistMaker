package com.practicum.playlistmaker.player.ui

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.media.ui.PLAYLIST_NAME
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.player.domain.entity.BottomSheetState
import com.practicum.playlistmaker.player.domain.entity.PlayerState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistErrorType
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistState
import com.practicum.playlistmaker.player.domain.entity.ToastState
import com.practicum.playlistmaker.search.ui.PLAYER_TRACKS_KEY
import com.practicum.playlistmaker.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var track: Track
    private lateinit var trackInfo: TrackInfo
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistAdapter: PlaylistAdapter
    private var isClickAllowed = true
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillPlaylistData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(PLAYLIST_NAME) { key, bundle ->
            val value = bundle.getString(key)

            if (value != null) {
                showCreatedMessage(value)
            }
        }

        track =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arguments?.getSerializable(
                PLAYER_TRACKS_KEY, Track::class.java
            )!!
            else arguments?.getSerializable(PLAYER_TRACKS_KEY) as Track

        trackInfo = TrackMapper.map(track)

        binding.playerToolbar.setNavigationIcon(R.drawable.arrow_back)
        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.newList.setOnClickListener {
            findNavController().navigate(R.id.action_player_to_newList)
        }
        val onPlaylistListener = { playlist: Playlist, track: Track ->
            if (clickDebounce()) {
                viewModel.onPlaylistClick(playlist, track)
            }
        }
        val configuration = Configuration(this.requireContext().resources.configuration)
        configuration.setLocale(Locale("ru"))
        val localeContext = this.requireContext().createConfigurationContext(configuration)

        playlistAdapter = PlaylistAdapter(
            localeContext, onPlaylistListener, track
        )
        binding.playlists.adapter = playlistAdapter
        binding.playlists.layoutManager = LinearLayoutManager(requireContext())

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        viewModel.fillPlaylistData()
                        binding.overlay.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding.addToPlaylist.setOnClickListener {
            viewModel.onAddPlaylistClick()
        }

        viewModel.observeListState().observe(this.viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Error -> showError(it.type)
                is PlaylistState.Content -> showContent(it.data)
            }
        }

        viewModel.observeBottomSheetState().observe(this.viewLifecycleOwner) {
            when (it) {
                is BottomSheetState.Hidden -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_HIDDEN

                is BottomSheetState.Collapsed -> bottomSheetBehavior.state =
                    BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.observeToastState().observe(this.viewLifecycleOwner) {
            when (it) {
                is ToastState.IsAdded -> showAddedToast(it.name)
                is ToastState.IsInList -> showIsInListToast(it.name)
            }
        }

        viewModel.fillPlaylistData()

        updateFromTrack(trackInfo)

        binding.playButton.setOnClickListener { viewModel.onPlaybackControl() }

        binding.playerFavoriteButton.setOnClickListener { viewModel.toggleFavorite() }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            when (it) {
                is PlayerState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)

                }

                is PlayerState.Prepared -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.playerElapsedTime.text = getString(R.string.track_null_time)

                }

                is PlayerState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.play_button)
                    binding.playButton.isEnabled = true

                }

                is PlayerState.Error -> {
                    binding.playButton.isEnabled = false

                }

                is PlayerState.Default -> {
                    binding.playButton.isEnabled = false
                    binding.playButton.setImageResource(R.drawable.play_button)
                }

                else -> {}
            }
        }

        viewModel.getProgress().observe(viewLifecycleOwner) {
            binding.playerElapsedTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(it)
        }

        viewModel.getIsFavoriteState().observe(viewLifecycleOwner) {
            updateFavoriteButton(it)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun updateFromTrack(track: TrackInfo) {
        with(binding) {
            playerTrackName.text = track.trackName
            playerArtistName.text = track.artistName
            timeInfo.text = track.trackTime
            collectionInfo.text = track.collectionName
            yearInfo.text = track.releaseYear
            genreInfo.text = track.primaryGenreName
            countryInfo.text = track.country
            playerElapsedTime.text = getString(R.string.track_sample_time)
        }

        Glide.with(binding.playerCover).load(track.coverArtwork).centerCrop().transform(
            RoundedCorners(
                Utils.dpToPx(
                    binding.playerCover.context.resources.getInteger(R.integer.track_big_cover_corner)
                        .toFloat(), binding.playerCover.context
                )
            )
        ).placeholder(R.drawable.big_cover_placeholder).into(binding.playerCover)

    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        val res = if (isFavorite) {
            R.drawable.favorite_on
        } else {
            R.drawable.favorite
        }
        binding.playerFavoriteButton.setImageResource(res)
    }

    private fun showError(type: PlaylistErrorType) {
        playlistAdapter.clear()

    }

    private fun showContent(data: List<Playlist>) {
        playlistAdapter.setItems(data)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        )
        toast.show()
    }

    private fun showIsInListToast(name: String) {
        val msg = getString(R.string.track_is_in_list_msg, name)
        showToast(msg)
    }

    private fun showAddedToast(name: String) {
        val msg = getString(R.string.track_added_in_list_msg, name)
        showToast(msg)
    }

    private fun showCreatedMessage(playlistName: String?) {
        val toast = Toast.makeText(
            context, getString(R.string.playlist_created_message, playlistName), Toast.LENGTH_SHORT
        )
        toast.show()

    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}