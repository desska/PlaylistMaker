package com.practicum.playlistmaker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaBinding

const val PLAYLIST_NAME = "playlistName"

class MediaFragment : Fragment() {
    private lateinit var tabMediator: TabLayoutMediator
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = getString(R.string.favorite_tracks_page)
                1 -> tab.text = getString(R.string.playlist_page)
            }
        }
        tabMediator.attach()

        setFragmentResultListener(PLAYLIST_NAME) { key, bundle ->
            val value = bundle.getString(key)

            if (value != null) {
                showCreatedMessage(value)
            }
        }

    }

    private fun showCreatedMessage(playlistName: String?) {
        val toast = Toast.makeText(
            context,
            getString(R.string.playlist_created_message, playlistName),
            Toast.LENGTH_SHORT
        )
        toast.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }

}