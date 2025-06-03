package com.example.networkapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var tvLog: TextView
    private lateinit var etIp: EditText
    private lateinit var etPort: EditText
    private lateinit var etListenPort: EditText // dodane
    private lateinit var etMessage: EditText
    private lateinit var tvDeviceIp: TextView
    private var udpListeningJob: Job? = null

    // Dodane do obsługi utrzymywania połączenia UDP
    private var udpSocket: DatagramSocket? = null
    private var lastUdpIp: String? = null
    private var lastUdpPort: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLog = findViewById(R.id.tvLog)
        etIp = findViewById(R.id.etIp)
        etPort = findViewById(R.id.etPort)
        etListenPort = findViewById(R.id.etListenPort) // dodane
        etMessage = findViewById(R.id.etMessage)
        tvDeviceIp = findViewById(R.id.tvDeviceIp)

        tvDeviceIp.text = "IP urządzenia: ${getDeviceIpAddress()}"

        findViewById<Button>(R.id.btnPing).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString().toIntOrNull() ?: 12345
            pingHost(ip, port)
        }

        findViewById<Button>(R.id.btnUdpSend).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString().toIntOrNull() ?: 12345
            val msg = etMessage.text.toString()
            sendUdpMessage(ip, port, msg)
        }

        findViewById<Button>(R.id.btnUdpListen).setOnClickListener {
            val ip = etIp.text.toString()
            val listenPort = etListenPort.text.toString().toIntOrNull() ?: 12345
            startUdpListening(ip, listenPort)
        }

        findViewById<Button>(R.id.btnApi).setOnClickListener {
            val ip = etIp.text.toString()
            val port = etPort.text.toString().toIntOrNull() ?: 80
            fetchApiData(ip, port)
        }
    }

    private fun log(msg: String) {
        runOnUiThread {
            tvLog.append("\n$msg")
        }
    }

    private fun pingHost(ip: String, port: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            log("Pinging $ip:$port ...")
            try {
                val address = InetAddress.getByName(ip)
                val start = System.currentTimeMillis()
                val reachable = address.isReachable(2000)
                val end = System.currentTimeMillis()
                if (reachable) {
                    log("Host $ip is reachable (time: ${end - start} ms)")
                } else {
                    log("Host $ip is NOT reachable")
                }
            } catch (e: Exception) {
                log("Ping Error: ${e.message}")
            }
        }
    }

    private fun sendUdpMessage(ip: String, port: Int, msg: String) {
        CoroutineScope(Dispatchers.IO).launch {
            log("Sending UDP message to $ip:$port ...")
            try {
                // Sprawdź, czy można użyć istniejącego socketu
                val socket = if (udpSocket != null && lastUdpIp == ip && lastUdpPort == port) {
                    udpSocket!!
                } else {
                    udpSocket?.close()
                    val s = DatagramSocket()
                    udpSocket = s
                    lastUdpIp = ip
                    lastUdpPort = port
                    s
                }
                val address = InetAddress.getByName(ip)
                val buffer = msg.toByteArray()
                val packet = DatagramPacket(buffer, buffer.size, address, port)
                socket.send(packet)
                log("Sent: \"$msg\" (socket open)")
            } catch (e: Exception) {
                log("UDP Send Error: ${e.message}")
            }
        }
    }

    private fun startUdpListening(ip: String, port: Int) {
        udpListeningJob?.cancel()
        udpListeningJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = DatagramSocket(port)
                socket.soTimeout = 0 // brak timeoutu
                val buffer = ByteArray(2048)
                log("Listening for UDP packets on $ip:$port ...")
                while (isActive) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket.receive(packet)
                    val msg = String(packet.data, 0, packet.length)
                    val from = packet.address.hostAddress
                    log("Received from $from:${packet.port}: $msg")
                }
                socket.close()
            } catch (e: Exception) {
                log("UDP listen stopped: ${e.message}")
            }
        }
    }

    private fun fetchApiData(ip: String, port: Int) {
        val baseUrl = "http://$ip:$port/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val posts = api.getPosts()
                val result = StringBuilder()
                result.append("API: Otrzymano ${posts.size} postów:\n")
                posts.forEachIndexed { idx, post ->
                    result.append("#${idx + 1}: ${post.title}\n${post.body}\n\n")
                }
                log(result.toString())
            } catch (e: Exception) {
                log("API Error: ${e.message}")
            }
        }
    }

    private fun getDeviceIpAddress(): String {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress && addr.hostAddress.indexOf(':') < 0) {
                        return addr.hostAddress ?: ""
                    }
                }
            }
        } catch (ex: Exception) {
        }
        return "Nieznany"
    }

    override fun onDestroy() {
        super.onDestroy()
        udpSocket?.close()
        udpSocket = null
    }
}

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}

data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)
