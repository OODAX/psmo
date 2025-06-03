
# 📱 psmo  
**Programowanie Systemów Mobilnych – LATO 2024/2025**

This repository contains coursework and materials for the *Programowanie Systemów Mobilnych* (Mobile Systems Programming) course for the Summer semester 2024/2025.

## 🔗 Useful Links
- [Node.js Official Website](https://nodejs.org/)
- [Android Security Best Practices](https://developer.android.com/privacy-and-security/security-config)

## 🧪 Laboratoria – kolejność i foldery
1. [Laboratorium 1 – Widgets](./FiveWidgetsApp)
2. [Laboratorium 2 – Activities](./ThreeActivityApp)
3. [Laboratorium 3 – Sensors](./ThreeSensorsApp)
4. [Laboratorium 4 – Data Base](./simpleDbApp)
5. [Laboratorium 5 – Threads](./ConcurrentApp)
6. [Laboratorium 6 – MVVM](./mvvmDbapp)
7. [Laboratorium 7 – Networking - App](./NetworkApp)
   [Laboratorium 7 – Networking - Server](./post-api-server)

## 🛠️ Bash Commands

### Clone the repository
```bash
git clone https://github.com/yourusername/psmo.git
cd psmo
```

### ADB connection to device
```bash
adb connect <device_ip>:5555
```

### Listen on UDP port with ncat
```bash
ncat -u -l <port>
```

### Send UDP message with ncat
```bash
echo "message" | ncat -u <target_ip> <port>
```

### Start Node.js server
```bash
node server.js
```

## 📄 License
This project is licensed under the [LICENSE](./LICENSE) file.
