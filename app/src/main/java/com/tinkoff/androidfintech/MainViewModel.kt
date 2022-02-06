package com.tinkoff.androidfintech

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinkoff.androidfintech.data.Gif
import com.tinkoff.androidfintech.repository.GifService

class MainViewModel : ViewModel() {

    private val gifService = GifService()

    private val gifs = ArrayList<Gif>()
    private var gifsCursor = 0
    private val gifLiveData: MutableLiveData<Gif> by lazy {
        MutableLiveData<Gif>().also {
            loadRandomGif()
        }
    }

    fun getGif(): LiveData<Gif> = gifLiveData

    fun loadBeforeGif() {
        gifsCursor--
        gifLiveData.value = gifs[gifsCursor]
    }

    fun loadNextGif() {
        gifsCursor++
        if (gifsCursor == gifs.size) {
            loadRandomGif()
        } else {
            gifLiveData.value = gifs[gifsCursor]
        }
    }

    private fun loadRandomGif() {
        gifService.getRandomGif {
            it?.let {
                gifLiveData.value = it
                gifs.add(it)
            }
        }
    }

    fun isLastGif() = gifs.size - 1 == gifsCursor

    fun isFirstGif() = gifsCursor == 0
}