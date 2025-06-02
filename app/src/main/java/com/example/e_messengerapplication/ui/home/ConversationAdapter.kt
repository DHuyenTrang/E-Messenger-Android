package com.example.e_messengerapplication.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.databinding.ItemConversationBinding
import com.example.e_messengerapplication.domain.Conversation
import com.example.e_messengerapplication.utils.Constant
import javax.inject.Inject

class ConversationAdapter(
    private val appStore: AppStore,
    private val onItemClick: (Conversation) -> Unit,
): ListAdapter<Conversation, ConversationAdapter.ConversationViewHolder>(ConversationDiffCallback) {

    inner class ConversationViewHolder(private val binding: ItemConversationBinding)
        : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Conversation) {
            binding.root.setOnClickListener {
                onItemClick(item)
            }

            binding.tvDisplayName.text = item.name ?: ""
            binding.tvLastMessage.text = formatLastMessage(item)
            binding.tvLastMessageTime.text = item.lastMessageTime?.let {
                Constant.formatMessageTime(it)
            } ?: ""

            val url = item.avatarUrl ?: "http://res.cloudinary.com/dtvuu5b2g/image/upload/v1746720818/other_default_avatar_fif3lm.jpg"
            val secureUrl = url.replace("http://", "https://")
            Glide.with(binding.root.context)
                .load(secureUrl)
                .into(binding.imageViewAvatar)
        }
        private fun formatLastMessage(item: Conversation, maxLength: Int = 30): String {
            val currentUserId = appStore.getUserID()
            val prefix = if (item.lastActorId == currentUserId) {
                "You: "
            } else {
                "${item.lastActorName}: "
            }

            val fullMessage = prefix + item.lastMessage
            return if (fullMessage.length > maxLength) {
                fullMessage.take(maxLength - 3) + "..."
            } else {
                fullMessage
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationAdapter.ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
