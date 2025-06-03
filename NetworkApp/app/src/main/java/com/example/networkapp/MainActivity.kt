// MainActivity.kt
package com.example.networkapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var tvLog: TextView
    private lateinit var etIp: EditText
    private lateinit var etPort: EditText
    private lateinit var etMessage: EditText
    private lateinit var tvDeviceIp: TextView // dodane
    private lateinit var etListenPort: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLog = findViewById(R.id.tvLog)
        etIp = findViewById(R.id.etIp)
        etPort = findViewById(R.id.etPort)
        etMessage = findViewById(R.id.etMessage)
        tvDeviceIp = findViewById(R.id.tvDeviceIp)
        etListenPort = findViewById(R.id.etListenPort)

        tvDeviceIp.text = "IP urządzenia: ${getLocalIpAddress()}"

        findViewById<Button>(R.id.btnPing).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString().toIntOrNull() ?: 12345
            viewModel.pingHost(ip, port)
        }

        findViewById<Button>(R.id.btnUdpSend).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString().toIntOrNull() ?: 12345
            val msg = etMessage.text.toString()
            viewModel.sendUdpMessage(ip, port, msg)
        }

        findViewById<Button>(R.id.btnUdpListen).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etListenPort.text.toString().toIntOrNull() ?: 12345
            viewModel.listenUdp(ip, port)
        }

        findViewById<Button>(R.id.btnApi).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString().toIntOrNull() ?: 80
            viewModel.fetchApiData(ip, port)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.log.collectLatest { log ->
                    tvLog.text = log
                }
            }
        }
    }

    private fun getLocalIpAddress(): String {
        try {
            val en = java.net.NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is java.net.Inet4Address) {
                        return inetAddress.hostAddress ?: ""
                    }
                }
            }
        } catch (ex: Exception) {
            //
        }
        return ""
    }
}
