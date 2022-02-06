package com.tinkoff.androidfintech

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tinkoff.androidfintech.repository.GifService.Companion.TAG


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var ivGif: ImageView
    private lateinit var tvGifDesc: TextView
    private lateinit var tvNoInternet: TextView
    private lateinit var btnRepeat: MaterialButton
    private lateinit var btnNext: FloatingActionButton
    private lateinit var btnBefore: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        if (isInternetAvailable()) {
            initFunctional()
        } else {
            btnRepeat.setOnClickListener {
                if (isInternetAvailable()) {
                    initFunctional()
                }
            }
        }
    }

    private fun initFunctional() {
        tvNoInternet.visibility = View.GONE
        btnRepeat.visibility = View.GONE

        btnNext.visibility = View.VISIBLE

        mainViewModel.getGif().observe(this) {
            Glide.with(this)
                .asGif()
                .load(it.gifURL)
                .placeholder(getProgressDrawable())
                .error(R.drawable.ic_error)
                .into(ivGif)

            tvGifDesc.text = it.description
        }

        btnNext.setOnClickListener {

            if (!isInternetAvailable() && mainViewModel.isLastGif()) {
                Toast.makeText(this, R.string.not_connection_to_internet, Toast.LENGTH_SHORT).show()
            } else {
                mainViewModel.loadNextGif()
                btnBefore.visibility = View.VISIBLE
            }
        }

        btnBefore.setOnClickListener {
            mainViewModel.loadBeforeGif()
            if (mainViewModel.isFirstGif()) {
                btnBefore.visibility = View.INVISIBLE
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun getProgressDrawable(): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }

    private fun initView() {
        ivGif = findViewById(R.id.iv_gif)
        btnBefore = findViewById(R.id.fab_before)
        btnNext = findViewById(R.id.fab_next)
        tvGifDesc = findViewById(R.id.tv_gif_desc)
        tvNoInternet = findViewById(R.id.tv_no_internet)
        btnRepeat = findViewById(R.id.btn_repeat)
    }
}