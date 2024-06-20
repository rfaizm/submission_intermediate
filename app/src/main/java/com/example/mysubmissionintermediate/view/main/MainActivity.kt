package com.example.mysubmissionintermediate.view.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmissionintermediate.R
import com.example.mysubmissionintermediate.data.ResultState
import com.example.mysubmissionintermediate.databinding.ActivityMainBinding
import com.example.mysubmissionintermediate.view.ViewModelFactory
import com.example.mysubmissionintermediate.view.adapter.LoadingStateAdapter
import com.example.mysubmissionintermediate.view.adapter.StoryAdapter
import com.example.mysubmissionintermediate.view.image.UploadImageActivity
import com.example.mysubmissionintermediate.view.maps.MapsActivity
import com.example.mysubmissionintermediate.view.register.RegisterActivity
import com.example.mysubmissionintermediate.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else if (user.isLogin){
                val layoutManager = LinearLayoutManager(this)
                binding.rvStory.layoutManager = layoutManager
                val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
                binding.rvStory.addItemDecoration(itemDecoration)

                getQuotes()

                binding.fabAdd.setOnClickListener {
                    startActivity(Intent(this, UploadImageActivity::class.java))
                }
            }
        }

    }


    private fun getQuotes() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.quote.observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                viewModel.logout()
            }
            R.id.menu2 -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getQuotes()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}