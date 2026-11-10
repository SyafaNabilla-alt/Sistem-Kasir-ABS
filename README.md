# 🍎 POS Alam Buah Segar (ABS)
Sistem Kasir Desktop Berbasis Java Swing untuk Toko Buah Ciwidey.

Repository GitHub: https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS

---

## 1. Latar Belakang Masalah & Solusi
Pencatatan transaksi penjualan dan pengelolaan inventaris buah di toko **Alam Buah Segar (ABS)** sebelumnya masih manual, sehingga sistemnya sering *lag*, *double scan* yang bikin kasir jadi nombok, terus rak sama sistemnya sering gak sinkron.

**POS ABS** hadir sebagai solusi aplikasi desktop terintegrasi yang menyediakan:
* **Performa Anti-Lag & Transaksi Cepat:** Aplikasi berbasis Java Swing yang ringan, dilengkapi perhitungan total belanja otomatis dan pencegahan *double scan* barang.
* **Sinkronisasi Rak & Stok Real-time:** Menyelaraskan data inventaris fisik di rak dengan sistem database secara akurat agar tidak ada lagi selisih.
* **Cetak Struk Thermal:** Integrasi pencetakan bukti pembayaran belanja secara instan untuk pelanggan.

---

## 2. Tech Stack & Architecture
* **Frontend / GUI:** Java Swing (NetBeans GUI Builder, Form-based Design).
* **Backend Engine:** Java Native (Apache Ant Build System, JDBC Driver).
* **Database:** MySQL / MariaDB (InnoDB Engine, XAMPP Server port 3306).
* **Security & Access:** Role-Based Access Control (Admin & Kasir).

---

## 3. Panduan Instalasi Lokal (Quick Local Setup)
Ikuti langkah berikut untuk menjalankan aplikasi di lingkungan lokal:

* **Prasyarat Sistem:** Java Development Kit (JDK / JRE 17+), XAMPP Server (MySQL), dan NetBeans IDE.
* **Langkah Menjalankan:**
  1. Kloning repositori: `git clone https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS.git`
  2. Nyalakan XAMPP MySQL (Port `3306`) dan gunakan database project POS ABS yang sudah ada.
  3. Buka folder `dist` dan jalankan file *executable* **`POS_AlamBuahSegar.jar`** (atau jalankan *Run* melalui NetBeans IDE).

---

## 4. Akun Pengujian Default (Credentials)
Gunakan kredensial berikut untuk menguji hak akses pada aplikasi:

| Role Pengguna | Username | Password | Hak Akses Utama |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` | Kelola master produk, kelola user, & rekap laporan global. |
| **Kasir** | `kasir` | `kasir123` | Melakukan transaksi kasir & cetak struk belanja. |

---

## 5. Tim Pengembang & Pembimbing
Proyek Uji Kompetensi Keahlian (UKK) PPLG — **SMK Budi Bakti Ciwidey (2026)**

* **Kelompok:** XavierForce
* **Syafa Nabilla Maharani** — Backend Developer & Database Administrator
* **Messa Lutfia** — Frontend Developer & UI/UX

*Guru Pembimbing:* Bapak Nurdiansyah Permana
