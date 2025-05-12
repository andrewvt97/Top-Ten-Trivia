package com.example.toptentrivia


import android.app.Application
import com.example.toptentrivia.data.AppContainer
import com.example.toptentrivia.data.DefaultAppContainer

class TopTenTriviaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
