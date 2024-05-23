package com.example.mysubmissionintermediate.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysubmissionintermediate.data.ResultState
import com.example.mysubmissionintermediate.databinding.ActivityRegisterBinding
import com.example.mysubmissionintermediate.view.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        binding.signupButton.setOnClickListener {
            setupActionRegister()
        }

    }

    private fun setupActionRegister() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        if (password.length <= 7){
            // Password is less than or equal to 8 characters
            Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.register(name, email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Yeah!")
                                setMessage("Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
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


    }

    private fun playAnimation() {

        val nameView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(nameView, nameEditLayout)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(emailView, emailEditLayout)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(passwordView, passwordEditLayout)
        }

        AnimatorSet().apply {
            playSequentially(togetherName, togetherEmail, togetherPassword, signup)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}