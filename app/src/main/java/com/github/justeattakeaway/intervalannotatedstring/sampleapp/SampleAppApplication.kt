package com.github.justeattakeaway.intervalannotatedstring.sampleapp

import android.app.Application
import android.content.Context
import androidx.annotation.MainThread

class SampleAppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        // Sample app-hacky to have a global context.
        private var instance: SampleAppApplication? = null

        @get:MainThread
        val applicationContext: Context?
            get() {
                return instance?.applicationContext
            }
    }
}