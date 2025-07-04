# IoT-Based Smart Vehicle Security System

## Deskripsi
*Proyek ini mengembangkan sistem keamanan kendaraan cerdas berbasis Internet of Things (IoT). Sistem dirancang untuk mendeteksi aktivitas mencurigakan seperti getaran atau pembukaan jok kendaraan dengan menggunakan sensor SW-420 dan reed switch yang dihubungkan ke mikrokontroler ESP32. 
Ketika ada potensi gangguan, sistem akan mengirimkan peringatan melalui notifikasi ke aplikasi Android pengguna, dan dapat memicu alarm otomatis.

*Backend dari sistem ini dibangun menggunakan FastAPI dan terhubung ke MongoDB Atlas untuk menyimpan data deteksi sensor secara real-time. Komunikasi antara perangkat dan server menggunakan protokol MQTT melalui broker EMQX.
Selain itu, Firebase Cloud Messaging digunakan untuk mengirimkan notifikasi langsung ke perangkat Android. Aplikasi Android dikembangkan menggunakan Java, dengan tampilan interaktif yang dilengkapi fitur login, pengaturan mode keamanan, dan pengendalian alarm.

*Proyek ini terdiri dari tiga komponen utama: kode ESP32 di PlatformIO, backend FastAPI, dan aplikasi Android. Setiap komponen terintegrasi untuk menciptakan sistem keamanan kendaraan yang terhubung, responsif, dan dapat dikendalikan dari jarak jauh.

## Teknologi
## Teknologi
- ESP32 (PlatformIO)
- FastAPI (Backend)
- MongoDB Atlas
- MQTT Broker (EMQX)
- Firebase Cloud Messaging
- Android Studio

## Catatan
*File rahasia seperti `.env`, 'serviceAccountKey.json dan' `google-services.json` tidak disertakan demi keamanan.
