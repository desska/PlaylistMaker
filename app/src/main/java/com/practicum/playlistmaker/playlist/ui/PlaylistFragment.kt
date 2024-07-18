package com.practicum.playlistmaker.playlist.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.newlist.domain.entity.Playlist
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistErrorType
import com.practicum.playlistmaker.playlist.domain.entity.PlaylistState
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {
    private val viewModel: PlaylistViewModel by viewModel()
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fillData()
        val configuration = Configuration(this.requireContext().resources.configuration)
        configuration.setLocale(Locale("ru"))
        val localeContext = this.requireContext().createConfigurationContext(configuration)

        adapter = PlaylistAdapter(localeContext)
        binding.list.adapter = adapter
        binding.list.layoutManager = GridLayoutManager(this.requireContext(), COLUMNS_QTY)

        binding.createList.setOnClickListener {
            viewModel.onCreateListClick()
        }

        viewModel.getNewListClickEvent().observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_mediaFragment_to_newList)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistState.Error -> showError(it.type)
                is PlaylistState.Content -> showContent(it.data)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()

    }

    private fun showError(type: PlaylistErrorType) {
        binding.errorMsgImg.isVisible = true
        binding.errorMsgText.isVisible = true
        binding.errorMsgText.text = textFromErrorType(type)
        binding.list.isVisible = false
    }

    private fun showContent(data: List<Playlist>) {
        binding.errorMsgImg.isVisible = false
        binding.errorMsgText.isVisible = false
        binding.errorMsgText.text = ""

        adapter.setItems(data)
        binding.list.isVisible = true

    }

    private fun textFromErrorType(type: PlaylistErrorType): String {
        return when (type) {
            PlaylistErrorType.EMPTY_PLAYLIST -> getString(R.string.empty_playlist_error)
        }
    }

    companion object {
        fun newInstance() = PlaylistFragment()
        const val COLUMNS_QTY = 2

    }
}