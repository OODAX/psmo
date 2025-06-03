
# 📱 psmo  
**Programowanie Systemów Mobilnych – LATO 2024/2025**

This repository contains coursework and materials for the *Programowanie Systemów Mobilnych* (Mobile Systems Programming) course for the Summer semester 2024/2025.

## 🔗 Useful Links
- [Download Node.js](https://nodejs.org/)
- [Download Android Studio](https://developer.android.com/studio)
- [Android Security Best Practices](https://developer.android.com/privacy-and-security/security-config)

## 🧪 Laboratoria – kolejność i foldery
- Laboratorium 1 – [Widgets](./FiveWidgetsApp)
- Laboratorium 2 – [Activities](./ThreeActivityApp)
- Laboratorium 3 – [Sensors](./ThreeSensorsApp)
- Laboratorium 4 – [Data Base](./simpleDbApp)
- Laboratorium 5 – [Threads](./ConcurrentApp)
- Laboratorium 6 – [MVVM](./mvvmDbapp)
- Laboratorium 7 – Networking  
   - [App](./NetworkApp)  
   - [Server](./post-api-server)

## 🛠️ Command Line Instructions

### Clone the repository
```bash
git clone https://github.com/OODAX/psmo.git
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
