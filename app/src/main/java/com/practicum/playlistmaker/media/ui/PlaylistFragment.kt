package com.practicum.playlistmaker.media.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistFragmentBinding
import com.practicum.playlistmaker.media.domain.entity.PlaylistErrorType
import com.practicum.playlistmaker.media.domain.entity.PlaylistState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment(R.layout.playlist_fragment) {
    private val viewModel: PlaylistViewModel by viewModel<PlaylistViewModel>()
    private var _binding: PlaylistFragmentBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Error -> showError(it.type)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showError(type: PlaylistErrorType) {
        binding.errorMsgImg.isVisible = true
        binding.errorMsgText.isVisible = true
        binding.errorMsgText.text = textFromErrorType(type)
    }

    private fun textFromErrorType(type: PlaylistErrorType): String {
        return when (type) {
            PlaylistErrorType.EMPTY_PLAYLIST -> getString(R.string.empty_playlist_error)
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}