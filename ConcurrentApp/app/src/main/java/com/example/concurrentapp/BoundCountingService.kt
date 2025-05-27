package com.example.concurrentapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BoundCountingService : Service() {

    private val binder = LocalBinder()
    @Volatile private var isCounting = false
    private var countCallback: ((Int) -> Unit)? = null

    inner class LocalBinder : Binder() {
        fun getService(): BoundCountingService = this@BoundCountingService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun startCounting(onCount: (Int) -> Unit, onDone: () -> Unit) {
        isCounting = true
        Thread {
            for (i in 1..100) {
                if (!isCounting) break
                Thread.sleep(100)
                onCount(i)
            }
            isCounting = false
            onDone() // ✅ Notify that counting finished
        }.start()
    }


    fun stopCounting() {
        isCounting = false
    }

}
