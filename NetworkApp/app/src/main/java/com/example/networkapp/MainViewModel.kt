// MainViewModel.kt
package com.example.networkapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive

class MainViewModel(private val api: ApiService) : ViewModel() {

    private val _log = MutableStateFlow("")
    val log = _log.asStateFlow()

    private var udpSocket: DatagramSocket? = null
    private var lastUdpIp: String? = null
    private var lastUdpPort: Int? = null
    private var udpListenJob: Job? = null
    private var udpListenSocket: DatagramSocket? = null // dodane

    fun pingHost(ip: String, port: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            appendLog("Pinging $ip:$port ...")
            try {
                val address = InetAddress.getByName(ip)
                val start = System.currentTimeMillis()
                val reachable = address.isReachable(2000)
                val end = System.currentTimeMillis()
                appendLog(
                    if (reachable) "Host reachable in ${end - start}ms"
                    else "Host NOT reachable"
                )
            } catch (e: Exception) {
                appendLog("Ping Error: ${e.message}")
            }
        }
    }

    fun sendUdpMessage(ip: String, port: Int, msg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val socket = if (udpSocket != null && lastUdpIp == ip && lastUdpPort == port) {
                    udpSocket!!
                } else {
                    udpSocket?.close()
                    DatagramSocket().apply {
                        udpSocket = this
                        lastUdpIp = ip
                        lastUdpPort = port
                    }
                }
                val packet = DatagramPacket(msg.toByteArray(), msg.length, InetAddress.getByName(ip), port)
                socket.send(packet)
                appendLog("UDP Sent: \"$msg\" to $ip:$port")
            } catch (e: Exception) {
                appendLog("UDP Send Error: ${e.message}")
            }
        }
    }

    fun listenUdp(ip: String, port: Int) {
        udpListenJob?.cancel()
        udpListenSocket?.close()
        udpListenSocket = null // upewnij się, że referencja jest wyzerowana przed nowym bindem
        udpListenJob = viewModelScope.launch(Dispatchers.IO) {
            appendLog("Nasłuchiwanie UDP na $port ...")
            var socket: DatagramSocket? = null
            try {
                socket = DatagramSocket(null)
                socket.reuseAddress = true
                socket.bind(InetSocketAddress(port))
                udpListenSocket = socket
                socket.soTimeout = 0
                val buf = ByteArray(1024)
                try {
                    while (isActive) {
                        val packet = DatagramPacket(buf, buf.size)
                        socket.receive(packet)
                        val msg = String(packet.data, 0, packet.length)
                        appendLog("UDP odebrano z ${packet.address.hostAddress}:${packet.port}: $msg")
                    }
                } catch (e: Exception) {
                    appendLog("UDP Listen Error: ${e.message}")
                } finally {
                    socket.close()
                    udpListenSocket = null
                }
            } catch (e: Exception) {
                appendLog("UDP Listen Error: ${e.message}")
                socket?.close()
                udpListenSocket = null
            }
        }
    }

    fun fetchApiData(ip: String, port: Int) {
        val dynamicRetrofit = Retrofit.Builder()
            .baseUrl("http://$ip:$port/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = dynamicRetrofit.getPosts()
                val result = buildString {
                    append("Received ${posts.size} posts:\n")
                    posts.forEach { append("${it.title}: ${it.body}\n") }
                }
                appendLog(result)
            } catch (e: Exception) {
                appendLog("API Error: ${e.message}")
            }
        }
    }

    fun appendLog(message: String) {
        val newLog = _log.value + "\n" + message.trim()
        val lines = newLog.lines().filter { it.isNotEmpty() }
        val trimmedLog = if (lines.size > 20) {
            lines.drop(lines.size - 20).joinToString("\n")
        } else {
            lines.joinToString("\n")
        }
        _log.value = trimmedLog
    }

    override fun onCleared() {
        super.onCleared()
        udpSocket?.close()
        udpListenJob?.cancel()
        udpListenSocket?.close() // zamknij socket nasłuchujący przy czyszczeniu VM
    }
}
