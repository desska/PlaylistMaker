package com.practicum.playlistmaker.media.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FavoriteFragmentBinding
import com.practicum.playlistmaker.media.domain.entity.FavoriteErrorType
import com.practicum.playlistmaker.media.domain.entity.FavoriteState
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FavoriteFragmentBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteState.Error -> showError(it.type)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showError(type: FavoriteErrorType) {
        binding.errorMsgImg.isVisible = true
        binding.errorMsgText.isVisible = true
        binding.errorMsgText.text = textFromErrorType(type)
    }

    private fun textFromErrorType(type: FavoriteErrorType): String {
        return when (type) {
            FavoriteErrorType.EMPTY_MEDIA -> getString(R.string.empty_media_error)
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}