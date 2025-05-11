package com.example.e_messengerapplication.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_messengerapplication.databinding.ItemConversationBinding
import com.example.e_messengerapplication.domain.Conversation

class ConversationAdapter(private val onItemClick: (Conversation) -> Unit): ListAdapter<Conversation, ConversationAdapter.ConversationViewHolder>(ConversationDiffCallback) {
    inner class ConversationViewHolder(private val binding: ItemConversationBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Conversation) {
            binding.root.setOnClickListener {
                onItemClick(item)
            }
            binding.tvDisplayName.text = item.name ?: ""
            binding.tvLastMessage.text = item.lastMessage ?: ""
            binding.tvLastMessageTime.text = item.lastMessageTime ?: ""
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationAdapter.ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ConversationAdapter.ConversationViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }
}

object ConversationDiffCallback: DiffUtil.ItemCallback<Conversation>() {
    override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
        return oldItem == newItem
    }

}
