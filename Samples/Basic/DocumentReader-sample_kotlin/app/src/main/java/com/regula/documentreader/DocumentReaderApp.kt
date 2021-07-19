package com.regula.documentreader

import android.app.Application
import android.os.StrictMode

class DocumentReaderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                        .detectCustomSlowCalls()
                        .penaltyLog()
                        .penaltyDeath()
                        .build()
        )
    }
}