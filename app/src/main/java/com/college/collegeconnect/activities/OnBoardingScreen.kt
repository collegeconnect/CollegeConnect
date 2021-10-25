package com.college.collegeconnect.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.college.collegeconnect.R
import com.college.collegeconnect.adapters.ViewPagerAdapter
import com.college.collegeconnect.databinding.ActivityOnBoardingScreenmBinding
import com.college.collegeconnect.datamodels.SaveSharedPreference.getRef
import com.college.collegeconnect.datamodels.SaveSharedPreference.setRef

class OnBoardingScreen : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingScreenmBinding

    private val layouts = intArrayOf(R.layout.first_slide, R.layout.second_slide, R.layout.third_slide)
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getRef(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding = ActivityOnBoardingScreenmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pager = findViewById<ViewPager>(R.id.viewPager)
        val adapter: PagerAdapter = ViewPagerAdapter(layouts, this@OnBoardingScreen)
        pager.adapter = adapter
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
//                createDots(position);
                currentPage = position
                when (currentPage) {
                    0 -> {
                        binding.buttonBack.isEnabled = false
                        binding.buttonNext.isEnabled = true
                        binding.buttonBack.visibility = View.INVISIBLE
                    }
                    layouts.size - 1 -> {
                        binding.buttonBack.isEnabled = true
                        binding.buttonNext.isEnabled = true
                        binding.buttonBack.visibility = View.VISIBLE
                        binding.buttonNext.text = "FINISH"
                    }
                    else -> {
                        binding.buttonBack.isEnabled = true
                        binding.buttonNext.isEnabled = true
                        binding.buttonBack.visibility = View.VISIBLE
                        binding.buttonNext.text = "NEXT"
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding.buttonBack.setOnClickListener(View.OnClickListener { pager.currentItem = currentPage - 1 })

        binding.buttonNext.setOnClickListener(
            View.OnClickListener {
                if (currentPage == layouts.size - 1) {
                    setRef(this@OnBoardingScreen, true)
                    startActivity(Intent(this@OnBoardingScreen, MainActivity::class.java))
                    finish()
                } else pager.currentItem = currentPage + 1
            }
        )

        binding.wormDotsIndicator.setViewPager(pager)
        //        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(pager, true);
    }
}
