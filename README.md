# Sistem Kasir POS – Alam Buah Segar (ABS)

[![Release](https://img.shields.io/badge/Release-v1.0.0--rc-0D9488?style=for-the-badge)](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS/releases)
[![Build Status](https://img.shields.io/badge/Build-Passing-10B981?style=for-the-badge)](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS)
[![Security Audit](https://img.shields.io/badge/Security-Protected-3B82F6?style=for-the-badge)](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS)
[![License](https://img.shields.io/badge/License-MIT-slate?style=for-the-badge)](LICENSE)

> Proyek Uji Kompetensi Keahlian (UKK) Rekayasa Perangkat Lunak / PPLG — Tahun Ajaran 2025/2026
> **Tema:** Local Digital Solution • **Mitra:** Toko Buah Alam Buah Segar (ABS) Ciwidey

---

##  1. Latar Belakang Masalah & Solusi
Pencatatan transaksi penjualan dan pengelolaan inventaris buah di toko **Alam Buah Segar (ABS)** sebelumnya masih menggunakan cara konvensional, sehingga rentan terhadap kesalahan hitung manual, kesulitan rekap laporan harian, dan antrean kasir yang lambat.

**POS ABS** hadir sebagai solusi aplikasi desktop terintegrasi yang menyediakan:
1. **Manajemen Transaksi Cepat:** Perhitungan total belanja otomatis dan fitur pencegahan *double scan* barang.
2. **Kontrol Stok Buah Real-time:** Memantau ketersediaan stok produk buah secara akurat.
3. **Cetak Struk Thermal:** Integrasi pencetakan bukti pembayaran belanja secara instan untuk pelanggan.

---

##  2. Tech Stack & Architecture
* **Frontend / GUI:** Java Swing (NetBeans GUI Builder, Form-based Design).
* **Backend Engine:** Java Native (Apache Ant Build System, JDBC Driver).
* **Database:** MySQL / MariaDB (InnoDB Engine, XAMPP Server port 3306).
* **Security & Access:** Role-Based Access Control (Admin, Kasir, Petugas).

---

##  3. Panduan Instalasi Lokal (Quick Local Setup)
Ikuti langkah berikut untuk menjalankan aplikasi di lingkungan lokal:

### Prasyarat Sistem:
* Java Development Kit (JDK / JRE 17+)
* XAMPP Server (MySQL)
* NetBeans IDE (Apache NetBeans)

### Langkah Menjalankan:
1. **Kloning Repositori:**
   ```bash
   git clone [https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS.git](https://github.com/SyafaNabilla-alt/Sistem-Kasir-ABS.git)
