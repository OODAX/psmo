
# 📱 psmo  
**Programowanie Systemów Mobilnych – LATO 2024/2025**

This repository contains coursework and materials for the *Programowanie Systemów Mobilnych* (Mobile Systems Programming) course for the Summer semester 2024/2025.

## 🔗 Useful Links
- [Node.js Official Website](https://nodejs.org/)
- [Android Security Best Practices](https://developer.android.com/privacy-and-security/security-config)

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
