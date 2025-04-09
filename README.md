# Session Chat

**Chat without sharing your number — just scan, connect, and go!**

Session Chat is a privacy-first Android messaging app that lets you chat instantly by scanning a QR code—no phone numbers required. Whether you're at a print shop, event, or just want a quick temporary conversation, Session Chat makes it fast, simple, and secure. Once you're done, end the session and the other user can no longer message you.

---
![{911EA02E-B73D-4BDB-8C9A-67F5BDBCB2D5}](https://github.com/user-attachments/assets/c61dd866-873b-441b-873e-3e6597629224)


## Features

- **No Phone Number Required**: Maintain your privacy—just scan a QR to start chatting.
- **QR Code Scanning**: Use QR codes to create or join temporary chat sessions.
- **Change Display Name**: Hide your real identity with custom usernames.
- **End Session Anytime**: One tap to close the chat; access is removed from both sides.
- **Share Images**: Upload and send images in real-time using Cloudinary.
- **Real-time Messaging**: Fast and reliable chat via Firebase Realtime Database.
- **Modern UI**: Built with Jetpack Compose for a clean and intuitive user experience.
- **Secure and Lightweight**: Simple to use and easy on resources.

---

## Screenshots

![{01703D66-A057-474D-A1EB-9B12CC2BF6C3}](https://github.com/user-attachments/assets/d580efd8-bb8f-4d72-9631-ecaa9a3d1cf1)

![{CF602FB0-B783-4EF5-B6FC-E30DD92C997E}](https://github.com/user-attachments/assets/04ff9985-efb7-4dc1-8439-3dc033e82390)


![{3A175317-DA53-41F5-A631-D07D557E331E}](https://github.com/user-attachments/assets/5da3dfed-9782-45cd-a524-ba70708d8154)

--- 

## Tech Stack

### **Frontend (UI/UX)**
- **Kotlin**: Primary language for Android development.
- **Jetpack Compose**: Modern declarative UI framework.
- **ZXing Library**: For QR code scanning and generation.

### **Backend (Business Logic)**
- **MVVM Architecture**: Clean separation of concerns.
- **Coroutines**: For smooth asynchronous operations.

### **Data & Messaging**
- **Firebase Realtime Database**: For storing and syncing messages in real-time.
- **Cloudinary**: For secure and fast image hosting.

### **Others**
- **ViewModel & LiveData**: Lifecycle-aware reactive components.
- **Material Design Components**: For consistent and responsive UI design.

---

## How It Works

1. **Start a Session**: Open the app and display your QR code.
2. **Scan to Connect**: The other user scans it to start chatting with you.
3. **Chat Freely**: Exchange messages and images securely in real-time.
4. **End the Session**: When you're done, simply end the session. The connection is permanently closed.

---


## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/AnjithSuryaVamshi/QuickChat.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle and build the project.
4. Run the app on an emulator or physical device.
5. Configure your own Firebase and Cloudinary credentials if deploying.

---

Developed by **Anjith Surya Vamshi**  
GitHub: [@AnjithSuryaVamshi](https://github.com/AnjithSuryaVamshi)

