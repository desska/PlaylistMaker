package com.practicum.playlistmaker.edittracks.ui

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentEditTracksBinding
import com.practicum.playlistmaker.edittracks.domain.entity.TracksState
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.player.domain.entity.BottomSheetState
import com.practicum.playlistmaker.player.domain.entity.Track
import com.practicum.playlistmaker.playlist.ui.PlaylistFragment
import com.practicum.playlistmaker.search.ui.PLAYER_TRACKS_KEY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

class EditTracksFragment : Fragment() {
    private var _binding: FragmentEditTracksBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true
    private val playlistId: Int by lazy {
        arguments?.getInt(PlaylistFragment.PLAYLIST_KEY) ?: -1
    }
    private lateinit var localeContext: Context
    private lateinit var adapter: EditTracksTrackAdapter
    private lateinit var dialogDeleteTrack: MaterialAlertDialogBuilder
    private lateinit var dialogDeleteList: MaterialAlertDialogBuilder
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var tracksBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val viewModel: EditTracksViewModel by viewModel {
        parametersOf(playlistId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationIcon(R.drawable.arrow_back)
        binding.toolbar.navigationIcon?.setTint(requireContext().getColor(R.color.black))
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val onTrackClickListener = { track: Track -> viewModel.onTrackClick(track) }
        val onLongTrackClickListener =
            { track: Track -> viewModel.onLongTrackClick(track) }

        adapter = EditTracksTrackAdapter(onTrackClickListener, onLongTrackClickListener)
        binding.trackList.adapter = adapter

        viewModel.observeLongTrackClickEvent().observe(viewLifecycleOwner) {
            dialogDeleteTrack.show()
        }

        viewModel.observeEmptyShareToastEvent().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(), getString(R.string.empty_share_msg), Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.observeTracksState().observe(this.viewLifecycleOwner) {
            showTracksState(it)
        }
        val configuration = Configuration(this.requireContext().resources.configuration)
        configuration.setLocale(Locale("ru"))
        localeContext = this.requireContext().createConfigurationContext(configuration)

        viewModel.observeTracksQuantityState().observe(this.viewLifecycleOwner) {
            binding.quantity.text =
                localeContext.resources.getQuantityString(R.plurals.tracks, it, it)
        }

        viewModel.observeTracksTimeState().observe(this.viewLifecycleOwner) {
            binding.time.text = localeContext.resources.getQuantityString(R.plurals.minutes, it, it)
        }

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            showPlaylistState(it)
        }

        viewModel.observeTrackClickEvent().observe(this.viewLifecycleOwner) {
            if (clickDebounce()) {
                openTrack(it)
            }
        }

        viewModel.observeMenuBottomSheetState().observe(this.viewLifecycleOwner) {
            showMenuBottomSheetState(it)
        }

        viewModel.observeExitEvent().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        viewModel.observeDeleteListDialogEvent().observe(viewLifecycleOwner) {
            dialogDeleteList.show()
        }

        viewModel.observeEditListEvent().observe(viewLifecycleOwner) {
            openEditPlaylist(it)
        }

        binding.share.setOnClickListener {
            viewModel.share()
        }

        binding.more.setOnClickListener {
            viewModel.onMenuClick()
        }

        binding.menuShare.setOnClickListener {
            viewModel.share()
        }

        binding.menuEdit.setOnClickListener {
            viewModel.onEditPlaylistClick()
        }

        binding.menuDelete.setOnClickListener {
            viewModel.onDeletePlaylistClick()
        }

        initDialogs()
        initSheetsBehaviors()
        viewModel.fillData()

    }

    private fun showPlaylistState(playlist: Playlist?) {
        val playlist = playlist ?: Playlist()

        binding.name.text = playlist.name
        binding.playlistInfo.name.text = playlist.name

        binding.playlistInfo.qty.text = localeContext.resources.getQuantityString(
            R.plurals.tracks, playlist.quantity, playlist.quantity
        )

        if (playlist.cover.toUri() == Uri.EMPTY) {
            binding.playlistInfo.cover.setImageResource(R.drawable.placeholder)
        } else {
            binding.playlistInfo.cover.setImageURI(playlist.cover.toUri())
        }

        if (playlist.description.isNotEmpty()) {
            binding.description.isVisible = true
            binding.description.text = playlist.description

        } else {
            binding.description.isVisible = false
        }

        if (playlist.cover.isEmpty()) {
            binding.cover.setImageResource(R.drawable.placeholder)
            binding.cover.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            binding.cover.setImageURI(playlist.cover.toUri())
            binding.cover.scaleType = ImageView.ScaleType.FIT_XY
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()

    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            R.id.action_editTracksFragment_to_player, bundleOf(
                PLAYER_TRACKS_KEY to track
            )
        )

    }

    private fun openEditPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_editTracksFragment_to_editListFragment, bundleOf(
                PlaylistFragment.PLAYLIST_KEY to playlist
            )
        )

    }

    private fun showTracksContent(data: List<Track>) {
        binding.trackList.isVisible = true
        binding.emptyMessage.isVisible = false
        adapter.setItems(data)
    }

    private fun showTracksEmpty() {
        binding.trackList.isVisible = false
        binding.emptyMessage.isVisible = true
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

    private fun showTracksState(state: TracksState) {
        when (state) {
            is TracksState.Content -> showTracksContent(state.data)
            is TracksState.Empty -> showTracksEmpty()
        }
    }

    private fun showMenuBottomSheetState(state: BottomSheetState) {
        when (state) {
            is BottomSheetState.Hidden -> menuBottomSheetBehavior.state =
                BottomSheetBehavior.STATE_HIDDEN

            is BottomSheetState.Collapsed -> menuBottomSheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun initDialogs() {
        dialogDeleteTrack = MaterialAlertDialogBuilder(
            requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setTitle(getString(R.string.confirm_delete_track_title))
            .setMessage(getString(R.string.confirm_delete_track_message))
            .setNegativeButton(getString(R.string.confirm_delete_track_negative_button)) { _, _ ->
            }.setPositiveButton(R.string.confirm_delete_track_positive_button) { _, _ ->
                viewModel.onConfirmDeleteTrack()
            }

        dialogDeleteList = MaterialAlertDialogBuilder(
            requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        ).setTitle(getString(R.string.confirm_delete_list_title))
            .setMessage(getString(R.string.confirm_delete_list_message))
            .setNegativeButton(getString(R.string.confirm_delete_list_negative_button)) { _, _ ->
            }.setPositiveButton(R.string.confirm_delete_list_positive_button) { _, _ ->
                viewModel.onConfirmDeleteList()
            }

    }

    private fun initSheetsBehaviors() {
        tracksBottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
            .apply { state = BottomSheetBehavior.STATE_COLLAPSED }

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet)
            .apply { state = BottomSheetBehavior.STATE_HIDDEN }
        val menuBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
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
        menuBottomSheetBehavior.addBottomSheetCallback(menuBottomSheetBehaviorCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}