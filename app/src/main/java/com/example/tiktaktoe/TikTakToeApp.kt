package com.example.tiktaktoe

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TikTakToeApp: Application() {
    companion object {
        lateinit var applicationData: ApplicationData
        lateinit var coroutineScope: CoroutineScope
    }

    override fun onCreate() {
        super.onCreate()
        coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        applicationData = ApplicationData(this)
    }
}