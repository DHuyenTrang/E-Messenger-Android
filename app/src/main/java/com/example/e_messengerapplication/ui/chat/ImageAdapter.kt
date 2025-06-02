package com.example.e_messengerapplication.ui.chat

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_messengerapplication.databinding.ItemImagePickerBinding

class ImageAdapter(private val onClick: (Uri) -> Unit) :
    ListAdapter<Uri, ImageAdapter.ImageViewHolder>(ImageDiffCallback()) {

    var selectedUri: Uri? = null

    inner class ImageViewHolder(private val binding: ItemImagePickerBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: Uri) {
            Glide.with(binding.root.context)
                .load(imageUri)
                .centerCrop()
                .into(binding.imageItem)

            if (imageUri == selectedUri) {
                binding.grayOverlay.visibility = View.VISIBLE
                binding.selectedOverlay.visibility = View.VISIBLE
            } else {
                binding.grayOverlay.visibility = View.GONE
                binding.selectedOverlay.visibility = View.GONE
            }
            binding.root.setOnClickListener {
                onClick(imageUri)
                selectedUri = imageUri
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemImagePickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ImageDiffCallback: DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

}