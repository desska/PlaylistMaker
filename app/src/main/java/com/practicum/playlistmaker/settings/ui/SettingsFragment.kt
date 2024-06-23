package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val themeSwitcher = binding.themeSwitcher

        viewModel.getTheme().observe(viewLifecycleOwner) {
            themeSwitcher.isChecked = it
        }

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.onToggleTheme(checked)
        }

        val shareButton = binding.shareButton
        shareButton.setOnClickListener {
            viewModel.onShare()
        }

        val supportButton = binding.supportButton
        supportButton.setOnClickListener {
            viewModel.onSupport()
        }

        val offerButton = binding.offerButton
        offerButton.setOnClickListener {
            viewModel.onOpenLink()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}