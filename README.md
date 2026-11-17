#  Sistem Kasir POS – Alam Buah Segar (ABS)

[![Release](https://img.shields.io/badge/Release-v1.0.0--rc-0D9488?style=for-the-badge)](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS/releases)
[![Build Status](https://img.shields.io/badge/Build-Passing-10B981?style=for-the-badge)](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS)
[![Security Audit](https://img.shields.io/badge/Security-Protected-3B82F6?style=for-the-badge)](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS)
[![License](https://img.shields.io/badge/License-MIT-slate?style=for-the-badge)](LICENSE)

> Proyek Uji Kompetensi Keahlian (UKK) Rekayasa Perangkat Lunak / PPLG — Tahun Ajaran 2025/2026
> **Tema:** Local Digital Solution • **Mitra:** Toko Alam Buah Segar (ABS) Ciwidey, Tenjolaya, Kec. Pasirjambu Kab. Bandung, Jawa Barat 40972

Repository GitHub: https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS

---

## 1. Latar Belakang Masalah & Solusi
Proses transaksi penjualan di toko **Alam Buah Segar (ABS)** sering menghadapi kendala operasional, yaitu sistem sering *lag* yang membuat antrian panjang, saat transaksi sistem selalu *double scan* dan tidak terdeteksi oleh sistem yang menyebabkan kasir jadi sering menombok, harga diskon di rak tidak sesuai dengan di sistem, lalu stok di rak dan di sistem sering tidak sinkron.

**Team XavierForce** hadir sebagai solusi aplikasi desktop terintegrasi yang menyediakan:
* **Performa Anti-Lag & Transaksi Cepat:** Aplikasi berbasis Java Swing yang ringan, dilengkapi perhitungan total belanja otomatis dan pencegahan *double scan/diplikasi* barang.
* **Sinkronisasi Rak & Stok Real-time:** Menyelaraskan data inventaris fisik di rak dengan sistem database secara akurat agar tidak ada lagi selisih.
* **Sinkronasi Harga Diskon Di Sistem:** Menyelaraskan harga diskon di sistem agar sesuai dengan harga yang tertera di rak.

---

## 2. Tech Stack & Architecture
* **Frontend / GUI:** Java Swing (NetBeans GUI Builder, Form-based Design).
* **Backend Engine:** Java Native (Apache Ant Build System, JDBC Driver).
* **Database:** MySQL / MariaDB (InnoDB Engine, XAMPP Server port 3306).
* **Security & Access:** Role-Based Access Control (Admin, Kasir, & Manager).

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
Gunakan kredensial berikut untuk menguji hak akses multi-role pada aplikasi:

| Role Pengguna | Username | Password | Hak Akses Utama |
| :--- | :--- | :--- | :--- |
| **Admin** | `julianAdmin` | `julianAdmin123` | Kelola master produk, kelola user, & rekap laporan global. |
| **Kasir** | `chapaKasir` | `chapaKasir123` | Melakukan transaksi kasir & cetak struk belanja. |
| **Manager** | `messaManager` | `messaManager123` | Memantau laporan penjualan saja. |

---

## 5. Tim Pengembang & Pembimbing
Proyek Uji Kompetensi Keahlian (UKK) PPLG — **SMK Budi Bakti Ciwidey (2026)**

* **Team:** XavierForce
* **Syafa Nabilla Maharani** — Backend Developer & Database Administrator
* **Messa Lutfia** — Frontend Developer & UI/UX

*Guru Pembimbing:* Bapak Nurdiansyah Permana
