package com.example.e_messengerapplication.ui.chat

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.ItemMessageReceivedBinding
import com.example.e_messengerapplication.databinding.ItemMessageSentBinding
import com.example.e_messengerapplication.domain.Message
import com.example.e_messengerapplication.domain.MessageType
import com.example.e_messengerapplication.utils.Constant

class MessagesAdapter(private val appStore: AppStore) :
    ListAdapter<Message, RecyclerView.ViewHolder>(ChatDiffCallback) {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    inner class SentMessageVH(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(message: Message) {
            renderMessage(binding, message)
        }
    }

    inner class ReceivedMessageVH(private val binding: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(message: Message) {
            renderMessage(binding, message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun renderMessage(binding: ViewBinding, message: Message) {
        when (message.type) {
            MessageType.TEXT -> renderTextMessage(binding, message)
            MessageType.IMAGE -> renderImageMessage(binding, message)
            MessageType.AUDIO -> renderAudioMessage(binding, message)
        }

        val timeTextView = when (binding) {
            is ItemMessageSentBinding -> binding.tvSentAt
            is ItemMessageReceivedBinding -> binding.tvSentAt
            else -> return
        }
        timeTextView.text = Constant.formatMessageTime(message.time)
    }

    private fun renderTextMessage(binding: ViewBinding, message: Message) {
        when (binding) {
            is ItemMessageSentBinding -> {
                binding.messageText.visibility = View.VISIBLE
                binding.messageImage.visibility = View.GONE
                binding.layoutAudio.visibility = View.GONE
                binding.messageText.text = message.content
            }

            is ItemMessageReceivedBinding -> {
                binding.messageText.visibility = View.VISIBLE
                binding.messageImage.visibility = View.GONE
                binding.layoutAudio.visibility = View.GONE
                binding.messageText.text = message.content
            }
        }
    }

    private fun renderImageMessage(binding: ViewBinding, message: Message) {
        val secureUrl = message.content.replace("http://", "https://")

        when (binding) {
            is ItemMessageSentBinding -> {
                binding.messageText.visibility = View.GONE
                binding.messageImage.visibility = View.VISIBLE
                binding.layoutAudio.visibility = View.GONE

                Glide.with(binding.root.context)
                    .load(secureUrl)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(binding.messageImage)

                binding.messageImage.setOnClickListener {
                    showFullScreenImage(binding.root.context, secureUrl)
                }
            }

            is ItemMessageReceivedBinding -> {
                binding.messageText.visibility = View.GONE
                binding.messageImage.visibility = View.VISIBLE
                binding.layoutAudio.visibility = View.GONE

                Glide.with(binding.root.context)
                    .load(secureUrl)
                    .transform(CenterCrop(), RoundedCorners(24))
                    .into(binding.messageImage)

                binding.messageImage.setOnClickListener {
                    showFullScreenImage(binding.root.context, secureUrl)
                }
            }
        }
    }

    private fun renderAudioMessage(binding: ViewBinding, message: Message) {
        val audioUrl = message.content

        when (binding) {
            is ItemMessageSentBinding -> {
                binding.messageText.visibility = View.GONE
                binding.messageImage.visibility = View.GONE
                binding.layoutAudio.visibility = View.VISIBLE

                AudioMessageHelper.setupAudio(
                    binding.root.context,
                    audioUrl,
                    binding.waveformSeekBar,
                    binding.btnPlayPause,
                    binding.audioDuration
                )
            }

            is ItemMessageReceivedBinding -> {
                binding.messageText.visibility = View.GONE
                binding.messageImage.visibility = View.GONE
                binding.layoutAudio.visibility = View.VISIBLE

                AudioMessageHelper.setupAudio(
                    binding.root.context,
                    audioUrl,
                    binding.waveformSeekBar,
                    binding.btnPlayPause,
                    binding.audioDuration
                )
            }
        }
    }


    private fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_arrrow_back)
            .error(R.drawable.ic_chat)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(24)))
            .into(imageView)
    }

    private fun showFullScreenImage(context: Context, imageUrl: String) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_full_screen_image)

        val imageView = dialog.findViewById<ImageView>(R.id.img_fullscreen)
        val backButton = dialog.findViewById<Button>(R.id.btn_back)
        backButton.setOnClickListener { dialog.dismiss() }

        Glide.with(context).load(imageUrl).into(imageView)
        dialog.show()
    }

    // ============================== Adapter Overrides ==============================

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).actorId == appStore.getUserID()) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENT -> {
                val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SentMessageVH(binding)
            }

            VIEW_TYPE_RECEIVED -> {
                val binding = ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ReceivedMessageVH(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SentMessageVH -> holder.bind(getItem(position))
            is ReceivedMessageVH -> holder.bind(getItem(position))
        }
    }

    // ============================== Diff Callback ==============================

    object ChatDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}
