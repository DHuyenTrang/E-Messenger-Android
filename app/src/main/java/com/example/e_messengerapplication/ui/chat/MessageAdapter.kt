package com.example.e_messengerapplication.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.databinding.ItemMessageReceivedBinding
import com.example.e_messengerapplication.databinding.ItemMessageSentBinding
import com.example.e_messengerapplication.domain.Message
import javax.inject.Inject

class MessagesAdapter(private val tokenManager: TokenManager): ListAdapter<Message, RecyclerView.ViewHolder>(ChatDiffCallback) {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    inner class SentMessageVH(private val binding: ItemMessageSentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.tvMessage.text = text
        }
    }

    inner class ReceivedMessageVH(private val binding: ItemMessageReceivedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.tvMessage.text = text
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).senderId) {
            tokenManager.getUserID() -> VIEW_TYPE_SENT
            else -> VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            VIEW_TYPE_SENT -> {
                val binding =
                    ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return SentMessageVH(binding)
            }
            VIEW_TYPE_RECEIVED -> {
                val binding =
                    ItemMessageReceivedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ReceivedMessageVH(binding)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SentMessageVH -> {
                holder.bind(getItem(position).text)
            }
            is ReceivedMessageVH -> {
                holder.bind(getItem(position).text)
            }
        }
    }

    object ChatDiffCallback: DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.text == newItem.text
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

}