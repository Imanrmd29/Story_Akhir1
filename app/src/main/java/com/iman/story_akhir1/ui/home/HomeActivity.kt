package com.iman.story_akhir1.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.iman.story_akhir1.R
import com.iman.story_akhir1.com.SharedPreferenceProvider
import com.iman.story_akhir1.databinding.ActivityHomeBinding
import com.iman.story_akhir1.ui.add.AddActivity
import com.iman.story_akhir1.ui.login.LoginActivity
import com.iman.story_akhir1.ui.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private val adapter = HomeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        initView()
    }

    private fun getData() {
        val token = SharedPreferenceProvider(applicationContext).getToken()!!
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter =
                this@HomeActivity.adapter.withLoadStateFooter(
                    footer = LoadingAdapter{
                        this@HomeActivity.adapter.retry()
                    }
                )
        }
        viewModel.getStories(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }

    }

    private fun initView() {
        with(binding) {
            tvName.text = SharedPreferenceProvider(this@HomeActivity).getName()
            btnLogout.setOnClickListener {
                Intent(this@HomeActivity, LoginActivity::class.java)
                    .apply {
                        startActivity(this)
                        finishAffinity()
                        SharedPreferenceProvider(this@HomeActivity).clearPreference()
                    }
            }

            fabAddNewStory.setOnClickListener {
                Intent(this@HomeActivity, AddActivity::class.java)
                    .apply {
                        finish()
                        startActivity(this)
                    }
            }
            btnMapsView.setOnClickListener{
                Intent(this@HomeActivity, MapsActivity::class.java)
                    .apply {
                        startActivity(this)
                    }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.not_getting_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}