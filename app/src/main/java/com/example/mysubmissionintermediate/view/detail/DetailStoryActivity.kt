package com.example.mysubmissionintermediate.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mysubmissionintermediate.R
import com.example.mysubmissionintermediate.data.ResultState
import com.example.mysubmissionintermediate.databinding.ActivityDetailStoryBinding
import com.example.mysubmissionintermediate.view.ViewModelFactory
import com.example.mysubmissionintermediate.view.adapter.StoryAdapter
import com.example.mysubmissionintermediate.view.main.MainViewModel

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailStoryBinding

    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usernameItem = intent.getStringExtra(STORY_ID) ?: ""


        getDetailStory(usernameItem)
    }

    private fun getDetailStory(id : String) {
        viewModel.getDetailStory(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        Glide.with(this)
                            .load(result.data.story?.photoUrl)
                            .into(binding.imageProfile)
                        binding.textName.text = result.data.story?.name
                        binding.textDesc.text = result.data.story?.description
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}