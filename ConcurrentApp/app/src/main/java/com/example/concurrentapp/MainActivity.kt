package com.example.concurrentapp

import android.annotation.SuppressLint
import android.content.*
import android.os.*
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var threadAdapter: CountAdapter
    private lateinit var coroutineAdapter: CountAdapter
    private lateinit var serviceAdapter: CountAdapter

    private var boundService: BoundCountingService? = null
    private var isServiceBound = false

    private var threadRunning = false
    private var thread: Thread? = null

    private var coroutineJob: Job? = null

    private var isServiceCounting = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BoundCountingService.LocalBinder
            boundService = binder.getService()
            isServiceBound = true
            // startServiceCounting()
            Toast.makeText(this@MainActivity, "Service Connected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
            Toast.makeText(this@MainActivity, "Service Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        threadAdapter = CountAdapter()
        coroutineAdapter = CountAdapter()
        serviceAdapter = CountAdapter()

        findViewById<RecyclerView>(R.id.rvThread).apply {
            adapter = threadAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<RecyclerView>(R.id.rvCoroutine).apply {
            adapter = coroutineAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<RecyclerView>(R.id.rvService).apply {
            adapter = serviceAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val threadButton = findViewById<Button>(R.id.btnStartThread)
        threadButton.setOnClickListener {
            if (threadRunning) {
                threadRunning = false
                threadButton.text = "Start Thread"
                threadButton.setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
            } else {
                threadRunning = true
                threadButton.apply {
                    text = "Stop Thread"
                    setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
                }

                thread = Thread {
                    for (i in 1..100) {
                        if (!threadRunning) break
                        Thread.sleep(100)
                        runOnUiThread {
                            threadAdapter.addCount(i)
                            findViewById<RecyclerView>(R.id.rvThread).post {
                                findViewById<RecyclerView>(R.id.rvThread).scrollToPosition(threadAdapter.itemCount - 1)
                            }
                        }
                    }
                    runOnUiThread {
                        threadRunning = false
                        threadButton.text = "Start Thread"
                        threadButton.setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                    }
                }
                thread?.start()
            }
        }

        val coroutineButton = findViewById<Button>(R.id.btnStartCoroutine)
        coroutineButton.setOnClickListener {
            if (coroutineJob?.isActive == true) {
                coroutineJob?.cancel()
                coroutineButton.apply {
                    text = "Start Coroutine"
                    setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                }

            } else {
                coroutineButton.apply {
                    text = "Stop Coroutine"
                    setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
                }
                coroutineJob = lifecycleScope.launch {
                    for (i in 1..100) {
                        delay(100)
                        coroutineAdapter.addCount(i)
                        findViewById<RecyclerView>(R.id.rvCoroutine).post {
                            findViewById<RecyclerView>(R.id.rvCoroutine).scrollToPosition(coroutineAdapter.itemCount - 1)
                        }
                    }
                    coroutineButton.apply {
                        text = "Start Coroutine"
                        setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                    }
                }
            }
        }

        val serviceButton = findViewById<Button>(R.id.btnStartService)
        serviceButton.setOnClickListener {
            if (!isServiceBound) return@setOnClickListener

            if (isServiceCounting) {
                boundService?.stopCounting()
                serviceButton.apply {
                    text = "Start Serviece"
                    setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                }
                isServiceCounting = false
            } else {
                isServiceCounting = true
                serviceButton.apply {
                    text = "Stop Serviece"
                    setBackgroundColor(resources.getColor(android.R.color.holo_red_light, null))
                }
                boundService?.startCounting(
                    onCount = { count ->
                        runOnUiThread {
                            serviceAdapter.addCount(count)
                            findViewById<RecyclerView>(R.id.rvService).post {
                                findViewById<RecyclerView>(R.id.rvService).scrollToPosition(serviceAdapter.itemCount - 1)
                            }
                        }
                    },
                    onDone = {
                        runOnUiThread {
                            isServiceCounting = false
                            serviceButton.apply {
                                text = "Start Serviece"
                                setBackgroundColor(resources.getColor(android.R.color.holo_green_light, null))
                            }
                        }
                    }
                )
            }
        }

        bindService(Intent(this, BoundCountingService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun startCoroutineCounting() {
        if (coroutineJob?.isActive == true) return
        coroutineJob = lifecycleScope.launch {
            for (i in 1..100) {
                delay(100)
                coroutineAdapter.addCount(i)
                findViewById<RecyclerView>(R.id.rvCoroutine).post {
                    findViewById<RecyclerView>(R.id.rvCoroutine).scrollToPosition(coroutineAdapter.itemCount - 1)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isServiceBound) {
            unbindService(serviceConnection)
            isServiceBound = false
        }
    }
}
