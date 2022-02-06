package com.tinkoff.androidfintech.repository

import com.tinkoff.androidfintech.data.Gif
import retrofit2.Call
import retrofit2.http.GET

interface GifApi {

    @GET("random?json=true")
    fun getRandomGif(): Call<Gif>
}