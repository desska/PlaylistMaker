package com.practicum.playlistmaker.editlist.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.newlist.domain.entity.NewListState
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.newlist.ui.NewListFragment
import com.practicum.playlistmaker.playlist.ui.PlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditListFragment : NewListFragment() {
    private lateinit var playlist: Playlist
    override val viewModel: EditListViewModel by viewModel() {
        parametersOf(playlist)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.viewmodel = viewModel

        binding.toolbar.setNavigationIcon(R.drawable.arrow_back)
        binding.toolbar.navigationIcon?.setTint(
            MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnSecondary,
                requireContext().getColor(R.color.black)
            )
        )

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbarTitle.text = getString(R.string.edit_playlist_toolbar_title)
        binding.createButton.text = getString(R.string.edit_playlist_save_button_title)
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.onCoverChanged(it)
            }
        }

        binding.createButton.setOnClickListener {
            viewModel.saveList()
        }

        binding.cover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.getListState().observe(this.viewLifecycleOwner) { state ->
            when (state) {
                is NewListState.ReadyToSave -> showReadyState()
                is NewListState.NotReadyToSave -> showNotReadyState()
                is NewListState.Created -> findNavController().navigateUp()
                else -> {}
            }
        }

        viewModel.getCoverState().observe(this.viewLifecycleOwner) {
            updateCover(it)
        }

    }

    override fun showCreatedState(playlistName: String) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlist =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arguments?.getSerializable(
                PlaylistFragment.PLAYLIST_KEY, Playlist::class.java
            )!!
            else arguments?.getSerializable(PlaylistFragment.PLAYLIST_KEY) as Playlist

        initDialog()
    }

}