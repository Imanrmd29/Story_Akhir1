package com.iman.story_akhir1.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.iman.story_akhir1.core.data.local.entity.StoryEntity
import com.iman.story_akhir1.databinding.ActivityDetailBinding
import com.iman.story_akhir1.ui.home.HomeAdapter

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"

        val extras = intent.extras
        val data = extras?.getParcelable<StoryEntity>(HomeAdapter.EXTRA_STORY)

        with(binding) {
            data?.apply {
                supportActionBar?.title = name
                tvUsername.text = name
                tvDetailStory.text = description
                Glide.with(this@DetailActivity)
                    .load(photoUrl)
                    .into(ivStory)
            }
        }
    }
}