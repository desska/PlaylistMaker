package com.practicum.playlistmaker.newlist.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewListBinding
import com.practicum.playlistmaker.media.ui.PLAYLIST_NAME
import com.practicum.playlistmaker.newlist.domain.entity.NewListState
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewListFragment : Fragment() {
    private var _binding: FragmentNewListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewListViewModel by viewModel()
    lateinit var dialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        binding.viewmodel = viewModel

        binding.toolbar.setNavigationIcon(R.drawable.arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            viewModel.onBackPressed()
        }
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.onCoverChanged(it)
            }
        }


        binding.createButton.setOnClickListener {
            viewModel.addList()
        }

        binding.cover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.getListState().observe(this.viewLifecycleOwner) { state ->
            when (state) {
                is NewListState.ReadyToCreate -> showReadyState()
                is NewListState.NotReadyToCreate -> showNotReadyState()
                is NewListState.Created -> showCreatedState(state.name)
                is NewListState.Confirm -> showConfirmDialog()
                is NewListState.Exit -> exit()
            }
        }

        viewModel.getCoverState().observe(this.viewLifecycleOwner) {
            updateCover(it)
        }

    }


    private fun exit() {
        findNavController().navigateUp()
    }

    private fun showConfirmDialog() {
        dialog.show()
    }

    private fun showCreatedState(playlistName: String) {
        val bundle = bundleOf(PLAYLIST_NAME to playlistName)
        setFragmentResult(PLAYLIST_NAME, bundle)
        findNavController().navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showReadyState() {
        binding.createButton.isEnabled = true
    }

    private fun showNotReadyState() {
        binding.createButton.isEnabled = false
    }

    private fun updateCover(cover: Uri?) {
        if ((cover == null) || (cover == Uri.EMPTY)) {
            binding.cover.setImageResource(R.drawable.new_list)
            binding.cover.scaleType = ImageView.ScaleType.CENTER
        } else {
            binding.cover.setImageURI(cover)
            binding.cover.scaleType = ImageView.ScaleType.FIT_XY
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.onBackPressed()
        }
        dialog = MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle(getString(R.string.confirm_new_list_title))
            .setMessage(getString(R.string.confirm_new_list_message))
            .setNegativeButton(getString(R.string.confirm_new_list_negative_button)) { _, _ ->
                viewModel.onConfirmNegative()
            }
            .setPositiveButton(R.string.confirm_new_list_positive_button) { _, _ ->
                viewModel.onConfirmPositive()
            }
    }

}