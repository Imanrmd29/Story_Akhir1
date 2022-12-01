package com.iman.story_akhir1.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import com.iman.story_akhir1.R
import com.iman.story_akhir1.com.SharedPreferenceProvider
import com.iman.story_akhir1.core.Resource
import com.iman.story_akhir1.databinding.ActivityLoginBinding
import com.iman.story_akhir1.ui.custom.CustomTextInput
import com.iman.story_akhir1.ui.home.HomeActivity
import com.iman.story_akhir1.ui.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Login"

        with(binding) {
            etPassword.globalChange()
            etEmail.globalChange()
            btnLogin.setOnClickListener {
                viewModel.setLoginParam(etEmail.text.toString(), etPassword.text.toString())
                viewModel.doLogin().observe(this@LoginActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            viewLoading.root.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            viewLoading.root.visibility = View.GONE
                            goToHomeActivity()
                        }
                        is Resource.Error -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
            tvRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        animationSet()
    }

    private fun goToHomeActivity() {
        Intent(this@LoginActivity, HomeActivity::class.java)
            .apply {
                startActivity(this)
                finish()
            }
    }

    private fun animationSet() {
        with(binding.root) {
            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {

                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {

                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {

                    if (currentId == R.id.end_splash) {
                        if (SharedPreferenceProvider(this@LoginActivity).getToken() != null)
                            goToHomeActivity()
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {

                }

            })
        }
    }


    private fun CustomTextInput.globalChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    btnLogin.isEnabled = etEmail.isValid == true && etPassword.isValid == true
                }
            }

        })
    }

    companion object{
        const val EXTRA_TOKEN = "extra-token"
    }
}