package com.example.mysubmissionintermediate.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mysubmissionintermediate.data.local.entity.StoryLocal
import com.example.mysubmissionintermediate.databinding.ItemRowStoryBinding
import com.example.mysubmissionintermediate.view.detail.DetailStoryActivity
import java.text.SimpleDateFormat
import java.util.Locale

class StoryAdapter : PagingDataAdapter<StoryLocal, StoryAdapter.ListViewHolder>(DIFF_CALLBACK)  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.bind(it) }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.STORY_ID, user?.id)
            intent.putExtra(DetailStoryActivity.DATA_DETAIL, user)
            context.startActivity(intent)
        }
    }

    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: StoryLocal) {
            binding.authorStory.text = user.name
            binding.dateStory.text = user.createdAt?.let { formatDate(it) }
            Glide.with(binding.imageStory)
                .load(user.photoUrl)
                .into(binding.imageStory)
        }

        private fun formatDate(dateString: String): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = sdf.parse(dateString)

            val formattedSdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            return formattedSdf.format(date!!)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryLocal>() {
            override fun areItemsTheSame(oldItem: StoryLocal, newItem: StoryLocal): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryLocal, newItem: StoryLocal
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}