package com.example.e_messengerapplication.ui.chat

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_messengerapplication.databinding.BottomSheetImagePickerBinding
import com.example.e_messengerapplication.utils.Constant.TAG_MESSAGE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImagePickerBottomSheet: BottomSheetDialogFragment() {
    private var _binding: BottomSheetImagePickerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetImagePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerViewImages
        val adapter = ImageAdapter{
            Log.d(TAG_MESSAGE, "Image picked: ${it.toString()}")
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            val images = loadImagesFromMediaStore()

            withContext(Dispatchers.Main) {
                adapter.submitList(images)
            }
        }
    }

    private fun loadImagesFromMediaStore(): List<Uri> {
        var count = 0
        val maxImages = 100

        val images = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext() ) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                images.add(uri)
                count++
            }
        }
        return images
    }
}