package com.tinkoff.androidfintech.repository

import android.util.Log
import com.tinkoff.androidfintech.data.Gif
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GifService {

    private val retrofit = ServiceBuilder.buildService(GifApi::class.java)

    fun getRandomGif(onResult: (Gif?) -> Unit) {
        retrofit.getRandomGif().enqueue(
            object : Callback<Gif> {
                override fun onFailure(call: Call<Gif>, t: Throwable) {
                    onResult(null)
                    Log.d(TAG, t.message!!)
                }

                override fun onResponse(
                    call: Call<Gif>,
                    response: Response<Gif>
                ) {
                    Log.d(TAG, "ok")
                    Log.d(TAG, "onResponse: " + response.code())
                    val gif = response.body()
                    Log.d(TAG, gif.toString())
                    onResult(gif)
                }
            }
        )
    }

    companion object {
        const val TAG = "GIF_SERVICE"
    }
}