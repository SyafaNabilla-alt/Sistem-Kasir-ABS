/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos_alambuahsegar;
import pos_alambuahsegar.GrafikLaporanPanel;
/**
 *
 * @author ASUS
 */
public class FormLaporanPenjualanAdmin extends javax.swing.JFrame {

    /**
     * Creates new form FormLaporan
     */
    public FormLaporanPenjualanAdmin() {
    initComponents();
    com.alambuahsegar.config.Session.setSidebarInfo(jLabelTanggal, jLabelUser);
    
    // 1. Load data transaksi agar tabel terisi otomatis
    loadDataLaporan();
    // Cek role untuk menyesuaikan tampilan sidebar atau judul form laporan
    String role = com.alambuahsegar.config.Session.role;
    if (role != null && role.equalsIgnoreCase("Admin")) {
        // Jika Admin yang buka, ubah teks judul/sidebar jika perlu
        // Contoh: lblJudulPanel.setText("LAPORAN PENJUALAN - ADMIN");
    } else if (role != null && role.equalsIgnoreCase("Manager")) {
        // Jika Manager yang buka
        // Contoh: lblJudulPanel.setText("LAPORAN PENJUALAN - MANAGER");
    }
    
    // 2. Pasang kembali panel grafik ke dalam kotak panel di bawah tabel
    // (Pastikan "panelGrafikLaporan" disesuaikan dengan nama variabel panel putih di jendela Navigator NetBeans kamu)
    panelGrafikLaporan.setLayout(new java.awt.BorderLayout());
panelGrafikLaporan.removeAll(); // Bersihkan dulu jika ada komponen lama
panelGrafikLaporan.add(new GrafikLaporanPanel(), java.awt.BorderLayout.CENTER);
panelGrafikLaporan.revalidate(); // Refresh layout panel
panelGrafikLaporan.repaint();    // Gambar ulang panel
    
    // 3. Atur warna teks "Berhasil" pada kolom status menjadi hijau tebal
    tblRiwayat.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 5 && "Berhasil".equals(value)) {
                c.setForeground(new java.awt.Color(40, 167, 69)); // Warna Hijau Segar
                setFont(getFont().deriveFont(java.awt.Font.BOLD));
            } else {
                c.setForeground(table.getForeground());
                setFont(getFont().deriveFont(java.awt.Font.PLAIN));
            }
            return c;
        }
    });
}
    
    
   public void loadDataLaporan() {
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        java.sql.Statement stmt = conn.createStatement();

        // 1. Ringkasan kartu atas: Omset & Jumlah Struk KHUSUS HARI INI
        String sqlSummary = "SELECT SUM(total_belanja) AS total_omset, COUNT(*) AS jumlah_transaksi FROM transaksi WHERE DATE(tanggal_transaksi) = CURDATE()";
        java.sql.ResultSet rsSummary = stmt.executeQuery(sqlSummary);

        double omset = 0;
        int jumlTrx = 0;
        if (rsSummary.next()) {
            omset = rsSummary.getDouble("total_omset");
            jumlTrx = rsSummary.getInt("jumlah_transaksi");
        }

        String formattedRupiah = String.format("%,.2f", omset).replace(',', 'x').replace('.', ',').replace('x', '.');
        lblTotalOmset.setText("Rp " + formattedRupiah);
        lblTotalTransaksi.setText(jumlTrx + " Struk");

        // 2. Peringatan Stok
String sqlStok = "SELECT stok_di_rak, total_stok_sistem FROM produk";
java.sql.ResultSet rsStokData = stmt.executeQuery(sqlStok);

int totalHabis = 0;
int totalSelisih = 0;
int totalMenipis = 0;

while (rsStokData.next()) {
    int stokRak = rsStokData.getInt("stok_di_rak");
    int stokSistem = rsStokData.getInt("total_stok_sistem");
    
    if (stokRak == 0 && stokSistem == 0) {
        totalHabis++;
    } else if (stokRak != stokSistem) {
        totalSelisih++;
    } else if (stokRak <= 5) {
        totalMenipis++;
    }
}

int totalPeringatan = totalHabis + totalSelisih + totalMenipis;

// Bagian tengah (menampilkan angka total produk)
lblPeringatan.setText(totalPeringatan + " Produk");

// Bagian bawah / subtitle (menampilkan rincian detail)
lblSubPeringatan.setText(totalHabis + " habis, " + totalSelisih + " selisih, " + totalMenipis + " menipis");

        // 3. Promo Aktif
        String sqlPromo = "SELECT COUNT(*) AS jml_promo FROM produk WHERE harga_diskon > 0";
        java.sql.ResultSet rsPromo = stmt.executeQuery(sqlPromo);
        if (rsPromo.next()) {
            int countPromo = rsPromo.getInt("jml_promo");
            lblPromo.setText(countPromo + " Promo");
        } else {
            lblPromo.setText("0 Promo");
        }

        // 4. Query untuk mengisi JTable riwayat transaksi
        String sqlTable = "SELECT t.*, p.nama_pengguna AS nama_kasir FROM transaksi t LEFT JOIN pengguna p ON t.id_pengguna = p.id_pengguna ORDER BY t.tanggal_transaksi DESC";
        java.sql.ResultSet rsTable = stmt.executeQuery(sqlTable);
        
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblRiwayat.getModel();
        model.setRowCount(0);

        while (rsTable.next()) {
            String tanggal = rsTable.getString("tanggal_transaksi");
            
            String kodeTrx = rsTable.getString("no_struk");
            if (kodeTrx == null) kodeTrx = "TRX-001";

            String kasir = "Kasir Toko";
            try {
                String namaKasir = rsTable.getString("nama_kasir");
                if (namaKasir != null && !namaKasir.isEmpty()) {
                    kasir = namaKasir;
                }
            } catch (Exception e) {}

            double total = rsTable.getDouble("total_belanja");
            String metode = "Tunai";

            model.addRow(new Object[]{
                tanggal,
                kodeTrx,
                kasir,
                "Rp " + String.format("%,.0f", total).replace(",", "."),
                metode,
                "Berhasil"
            });
        }
        
        tblRiwayat.setRowHeight(35); 
        tblRiwayat.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblRiwayat.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        tblRiwayat.getTableHeader().setForeground(java.awt.Color.BLACK);
        
    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat data tabel: " + e.getMessage());
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelTanggal = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        btnPengguna = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        btnProduk = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        btnExportExcel = new javax.swing.JButton();
        btnCetakLaporan = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRiwayat = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        panelGrafikLaporan = new pos_alambuahsegar.GrafikPenjualanPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblTotalOmset = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lblTotalTransaksi = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblSubPeringatan = new javax.swing.JLabel();
        lblPeringatan = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblPromo = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(13660, 768));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(55, 85, 52));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconlaporan2-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 80, 50));

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(3, 159, 25));
        jButton1.setText("Laporan Penjualan");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 280, 50));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTanggal.setText("Rabu 30 Sep 2026");
        jPanel2.add(jLabelTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 110, 20));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 630, 120, 20));

        jLabelUser.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUser.setText("User Admin(Admin@gmail.com)");
        jPanel1.add(jLabelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 640, 190, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Logo_ABS removebg.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 160, 110));

        jLabel8.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MENU UTAMA");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, 15));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icondash-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 60, 50));

        jLabel13.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("DATA MASTER");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, 15));

        btnDashboard.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setContentAreaFilled(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDashboardMouseClicked(evt);
            }
        });
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });
        jPanel1.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, 40));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconkeluar-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 470, 50, 40));

        btnLogout.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("Logout");
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, 90, 40));

        jLabel23.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("AKUN");
        jPanel1.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, 40, 20));

        btnPengguna.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPengguna.setForeground(new java.awt.Color(255, 255, 255));
        btnPengguna.setText("Pengguna");
        btnPengguna.setBorderPainted(false);
        btnPengguna.setContentAreaFilled(false);
        btnPengguna.setFocusPainted(false);
        btnPengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenggunaActionPerformed(evt);
            }
        });
        jPanel1.add(btnPengguna, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, 110, 40));

        jLabel24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconpengguna3-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 60, 40));

        jLabel17.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("LAPORAN");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, -1, 30));

        btnProduk.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnProduk.setForeground(new java.awt.Color(255, 255, 255));
        btnProduk.setText("Produk");
        btnProduk.setBorderPainted(false);
        btnProduk.setContentAreaFilled(false);
        btnProduk.setFocusPainted(false);
        btnProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdukActionPerformed(evt);
            }
        });
        jPanel1.add(btnProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, -1, 30));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconptoduk-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 50, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 750));

        btnExportExcel.setText("Expor Excel");
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });
        getContentPane().add(btnExportExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 120, -1, -1));

        btnCetakLaporan.setBackground(new java.awt.Color(3, 159, 25));
        btnCetakLaporan.setForeground(new java.awt.Color(255, 255, 255));
        btnCetakLaporan.setText("Cetak Laporan");
        btnCetakLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakLaporanActionPerformed(evt);
            }
        });
        getContentPane().add(btnCetakLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 120, -1, -1));

        jLabel21.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(3, 159, 25));
        jLabel21.setText("Grafik Penjualan");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, -1, -1));

        tblRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "TANGGAL", "KODE TRANSAKSI", "KASIR", "TOTAL", "METODE PEMBAYARAN ", "STATUS"
            }
        ));
        jScrollPane1.setViewportView(tblRiwayat);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 1090, 250));

        jLabel22.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(3, 159, 25));
        jLabel22.setText("Tabel Riwayat Transaksi Penjualan");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, -1, -1));

        panelGrafikLaporan.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelGrafikLaporanLayout = new javax.swing.GroupLayout(panelGrafikLaporan);
        panelGrafikLaporan.setLayout(panelGrafikLaporanLayout);
        panelGrafikLaporanLayout.setHorizontalGroup(
            panelGrafikLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelGrafikLaporanLayout.setVerticalGroup(
            panelGrafikLaporanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );

        getContentPane().add(panelGrafikLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 450, 1090, 230));

        jPanel8.setBackground(new java.awt.Color(79, 139, 91));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TOTAL PENJUALAN HARI INI");
        jPanel8.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -10, 180, 60));

        lblTotalOmset.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); // NOI18N
        lblTotalOmset.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalOmset.setText("Rp 15.250.00");
        jPanel8.add(lblTotalOmset, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 130, 70));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconpenjualan-removebg-preview.png"))); // NOI18N
        jPanel8.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 50, 60));

        getContentPane().add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 190, 90));

        jPanel9.setBackground(new java.awt.Color(79, 139, 91));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("TOTAL TRANSAKSI");
        jPanel9.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 100, -1));

        lblTotalTransaksi.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); // NOI18N
        lblTotalTransaksi.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalTransaksi.setText("245 Struk");
        jPanel9.add(lblTotalTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 50));

        jLabel12.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Hari ini");
        jPanel9.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 70, 40));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconstruk-removebg-preview.png"))); // NOI18N
        jPanel9.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 50, 60));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 190, 90));

        jPanel10.setBackground(new java.awt.Color(79, 139, 91));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("PERINGATAN STOK");
        jPanel10.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 110, -1));

        lblSubPeringatan.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        lblSubPeringatan.setForeground(new java.awt.Color(255, 255, 255));
        lblSubPeringatan.setText("3 habis, 1 selisih,10 menipis");
        jPanel10.add(lblSubPeringatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        lblPeringatan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPeringatan.setForeground(new java.awt.Color(255, 255, 255));
        jPanel10.add(lblPeringatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 170, 20));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconperingatan-removebg-preview.png"))); // NOI18N
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 40, 80));

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 10, 190, 90));

        jPanel7.setBackground(new java.awt.Color(79, 139, 91));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("PROMO SISTEM AKTIF");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 140, -1));

        lblPromo.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblPromo.setForeground(new java.awt.Color(255, 255, 255));
        lblPromo.setText("5 Promo");
        jPanel7.add(lblPromo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 140, 30));

        jLabel18.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Hari ini");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 50, -1));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icondiskon-removebg-preview.png"))); // NOI18N
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 80));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 10, 190, 90));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnCetakLaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakLaporanActionPerformed
    try {
        // 1. Wajib Pilih Tanggal Periode Cetak di Awal
        String tglMulai = javax.swing.JOptionPane.showInputDialog(this, 
            "Masukkan Tanggal Awal Laporan (Format: YYYY-MM-DD, contoh: 2026-11-01):", 
            "Filter Tanggal Cetak", 
            javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (tglMulai == null || tglMulai.trim().isEmpty()) { return; }
        
        String tglSelesai = javax.swing.JOptionPane.showInputDialog(this, 
            "Masukkan Tanggal Akhir Laporan (Format: YYYY-MM-DD, contoh: 2026-11-30):", 
            "Filter Tanggal Cetak", 
            javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (tglSelesai == null || tglSelesai.trim().isEmpty()) { return; }

        // 2. Buat file HTML sementara untuk cetak laporan profesional
        java.io.File htmlFile = new java.io.File(System.getProperty("java.io.tmpdir") + "Cetak_Laporan_AlamBuahSegar.html");
        java.io.FileWriter writer = new java.io.FileWriter(htmlFile);
        
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        
        // Query Data Transaksi Utama
        String sqlTrx = "SELECT t.no_struk, t.tanggal_transaksi, t.total_belanja, t.uang_bayar, t.kembalian, p.nama_pengguna " +
                        "FROM transaksi t JOIN pengguna p ON t.id_pengguna = p.id_pengguna " +
                        "WHERE DATE(t.tanggal_transaksi) BETWEEN ? AND ? " +
                        "ORDER BY t.tanggal_transaksi DESC";
        java.sql.PreparedStatement pstTrx = conn.prepareStatement(sqlTrx);
        pstTrx.setString(1, tglMulai);
        pstTrx.setString(2, tglSelesai);
        java.sql.ResultSet rsTrx = pstTrx.executeQuery();

        // Query Data Produk Terlaris
        String sqlTop = "SELECT p.nama_produk, SUM(dt.qyt) AS total_terjual, SUM(dt.subtotal) AS total_pendapatan " +
                        "FROM detail_transaksi dt JOIN produk p ON dt.id_produk = p.id_produk " +
                        "JOIN transaksi t ON dt.id_transaksi = t.id_transaksi " + 
                        "WHERE DATE(t.tanggal_transaksi) BETWEEN ? AND ? " +
                        "GROUP BY p.id_produk, p.nama_produk " +
                        "ORDER BY total_terjual DESC";
        java.sql.PreparedStatement pstTop = conn.prepareStatement(sqlTop);
        pstTop.setString(1, tglMulai);
        pstTop.setString(2, tglSelesai);
        java.sql.ResultSet rsTop = pstTop.executeQuery();

        // Format HTML dengan Header Hijau (#2e7d32) & Logo
        writer.write("<html>\n<head>\n<style>\n");
        writer.write("  body { font-family: Arial, sans-serif; margin: 30px; }\n");
        writer.write("  .header { font-size: 20pt; font-weight: bold; color: #2e7d32; text-align: center; }\n");
        writer.write("  .sub-header { font-size: 10pt; color: #555; text-align: center; }\n");
        writer.write("  .title { font-size: 14pt; font-weight: bold; margin-top: 20px; margin-bottom: 5px; }\n");
        writer.write("  table { border-collapse: collapse; width: 100%; margin-top: 10px; }\n");
        writer.write("  th { background-color: #2e7d32; color: white; border: 1px solid #ddd; padding: 8px; text-align: center; }\n");
        writer.write("  td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
        writer.write("</style>\n</head>\n<body>\n");

        // Kop Surat dengan Logo ABS
        writer.write("<table style='border: none; width: 100%;'>\n");
        writer.write("<tr>\n");
        writer.write("  <td style='width: 90px; border: none; padding: 0px; text-align: center;'><img src='file:///C:/projek%20ukk/POS_AlamBuahSegar/src/gambar/Logo_ABS%20removebg.png' width='70' height='70'></td>\n");
        writer.write("  <td style='border: none; padding: 0px; text-align: center;'>\n");
        writer.write("    <div class='header'>Alam Buah Segar</div>\n");
        writer.write("    <div class='sub-header' style='margin-top: 4px;'>Telepon: 083821516343</div>\n");
        writer.write("    <div class='sub-header' style='margin-top: 2px;'>Alamat: Tenjolaya, Kec. Pasirjambu, Kabupaten Bandung, Jawa Barat 40972</div>\n");
        writer.write("  </td>\n");
        writer.write("</tr>\n</table>\n");
        writer.write("<hr style='border: 1px solid #2e7d32; margin-top: 8px;'>\n");

        // Bagian 1: Tabel Riwayat Transaksi
        writer.write("<div class='title'>LAPORAN RIWAYAT PENJUALAN</div>\n");
        writer.write("<div class='sub-header' style='text-align: left; font-weight: bold;'>Periode: " + tglMulai + " s/d " + tglSelesai + "</div>\n");
        
        writer.write("<table>\n<tr>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>No Struk</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Tanggal Transaksi</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Total Belanja (Rp)</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Uang Bayar (Rp)</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Kembalian (Rp)</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Metode</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Kasir</th>\n");
        writer.write("</tr>\n");

        while (rsTrx.next()) {
            writer.write("<tr>\n");
            writer.write("  <td>" + rsTrx.getString("no_struk") + "</td>\n");
            writer.write("  <td>" + rsTrx.getString("tanggal_transaksi") + "</td>\n");
            writer.write("  <td>Rp " + String.format("%,.0f", rsTrx.getDouble("total_belanja")) + "</td>\n");
            writer.write("  <td>Rp " + String.format("%,.0f", rsTrx.getDouble("uang_bayar")) + "</td>\n");
            writer.write("  <td>Rp " + String.format("%,.0f", rsTrx.getDouble("kembalian")) + "</td>\n");
            writer.write("  <td>Tunai</td>\n");
            writer.write("  <td>" + rsTrx.getString("nama_pengguna") + "</td>\n");
            writer.write("</tr>\n");
        }
        writer.write("</table>\n");

        // Bagian 2: Tabel Ringkasan Produk Terlaris
        writer.write("<br><br>\n<div class='title'>RINGKASAN PRODUK TERLARIS</div>\n");
        writer.write("<div class='sub-header' style='text-align: left;'>Periode: " + tglMulai + " s/d " + tglSelesai + "</div>\n");
        
        writer.write("<table>\n<tr>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>No</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Nama Produk (Buah)</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Total Terjual (Qty)</th>\n");
        writer.write("  <th style='background-color: #2e7d32; color: white;'>Total Pendapatan (Rp)</th>\n");
        writer.write("</tr>\n");

        int no = 1;
        while (rsTop.next()) {
            writer.write("<tr>\n");
            writer.write("  <td style='text-align: center;'>" + (no++) + "</td>\n");
            writer.write("  <td>" + rsTop.getString("nama_produk") + "</td>\n");
            writer.write("  <td style='text-align: center;'>" + rsTop.getInt("total_terjual") + " pcs</td>\n");
            writer.write("  <td>Rp " + String.format("%,.0f", rsTop.getDouble("total_pendapatan")) + "</td>\n");
            writer.write("</tr>\n");
        }
        writer.write("</table>\n");
        
        // Script otomatis membuka kotak dialog print di browser saat file terbuka
        writer.write("<script>window.onload = function() { window.print(); }</script>\n");
        writer.write("</body>\n</html>\n");

        writer.flush();
        writer.close();

        // Buka file laporan secara otomatis di browser default
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
        }

        javax.swing.JOptionPane.showMessageDialog(this, "Berhasil menyiapkan cetak laporan periode " + tglMulai + " s/d " + tglSelesai + "!");

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnCetakLaporanActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
       new FormLaporanPenjualanAdmin().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton1MouseClicked

    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportExcelActionPerformed
    try {
        // 1. Pilih Tanggal Periode Export di Awal
        String tglMulai = javax.swing.JOptionPane.showInputDialog(this, 
            "Masukkan Tanggal Awal Laporan (Format: YYYY-MM-DD, contoh: 2026-11-01):", 
            "Filter Tanggal Export", 
            javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (tglMulai == null || tglMulai.trim().isEmpty()) { return; }
        
        String tglSelesai = javax.swing.JOptionPane.showInputDialog(this, 
            "Masukkan Tanggal Akhir Laporan (Format: YYYY-MM-DD, contoh: 2026-11-30):", 
            "Filter Tanggal Export", 
            javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (tglSelesai == null || tglSelesai.trim().isEmpty()) { return; }

        // 2. Jendela Pilih Folder Penyimpanan Excel
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan Penjualan ke Excel");
        fileChooser.setSelectedFile(new java.io.File("Laporan_Penjualan_AlamBuahSegar.xls"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".xls")) {
                filePath += ".xls";
            }

            java.io.FileWriter writer = new java.io.FileWriter(filePath);
            java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
            
            // Query Data Transaksi Utama dengan Filter Tanggal
            String sqlTrx = "SELECT t.no_struk, t.tanggal_transaksi, t.total_belanja, t.uang_bayar, t.kembalian, p.nama_pengguna " +
                            "FROM transaksi t JOIN pengguna p ON t.id_pengguna = p.id_pengguna " +
                            "WHERE DATE(t.tanggal_transaksi) BETWEEN ? AND ? " +
                            "ORDER BY t.tanggal_transaksi DESC";
            java.sql.PreparedStatement pstTrx = conn.prepareStatement(sqlTrx);
            pstTrx.setString(1, tglMulai);
            pstTrx.setString(2, tglSelesai);
            java.sql.ResultSet rsTrx = pstTrx.executeQuery();

            // Query Data Produk Terlaris dengan Filter Tanggal (Struktur kolom sesuai database)
            String sqlTop = "SELECT p.nama_produk, SUM(dt.qyt) AS total_terjual, SUM(dt.subtotal) AS total_pendapatan " +
                            "FROM detail_transaksi dt JOIN produk p ON dt.id_produk = p.id_produk " +
                            "JOIN transaksi t ON dt.id_transaksi = t.id_transaksi " + 
                            "WHERE DATE(t.tanggal_transaksi) BETWEEN ? AND ? " +
                            "GROUP BY p.id_produk, p.nama_produk " +
                            "ORDER BY total_terjual DESC";
            java.sql.PreparedStatement pstTop = conn.prepareStatement(sqlTop);
            pstTop.setString(1, tglMulai);
            pstTop.setString(2, tglSelesai);
            java.sql.ResultSet rsTop = pstTop.executeQuery();

            // Format HTML untuk Excel dengan Header Hijau (#2e7d32)
            writer.write("<html>\n<head>\n<style>\n");
            writer.write("  body { font-family: Arial, sans-serif; }\n");
            writer.write("  .header { font-size: 20pt; font-weight: bold; color: #2e7d32; text-align: center; }\n");
            writer.write("  .sub-header { font-size: 10pt; color: #555; text-align: center; }\n");
            writer.write("  .title { font-size: 14pt; font-weight: bold; margin-top: 15px; margin-bottom: 5px; }\n");
            writer.write("  table { border-collapse: collapse; width: 100%; margin-top: 10px; }\n");
            writer.write("  th { background-color: #2e7d32; color: white; border: 1px solid #ddd; padding: 8px; text-align: center; }\n");
            writer.write("  td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("</style>\n</head>\n<body>\n");

            // Kop Surat
            writer.write("<table style='border: none; width: 100%;'>\n");
            writer.write("<tr>\n");
            writer.write("  <td style='width: 90px; border: none; padding: 0px; text-align: center;'><img src='file:///C:/projek%20ukk/POS_AlamBuahSegar/src/gambar/Logo_ABS%20removebg.png' width='70' height='70'></td>\n");
            writer.write("  <td style='border: none; padding: 0px; text-align: center;'>\n");
            writer.write("    <div class='header'>Alam Buah Segar</div>\n");
            writer.write("    <div class='sub-header' style='margin-top: 4px;'>Telepon: 083821516343</div>\n");
            writer.write("    <div class='sub-header' style='margin-top: 2px;'>Alamat: Tenjolaya, Kec. Pasirjambu, Kabupaten Bandung, Jawa Barat 40972</div>\n");
            writer.write("  </td>\n");
            writer.write("</tr>\n</table>\n");
            writer.write("<hr style='border: 1px solid #2e7d32; margin-top: 8px;'>\n");

            // Bagian 1: Tabel Riwayat Transaksi
            writer.write("<div class='title'>LAPORAN PENJUALAN</div>\n");
            writer.write("<div class='sub-header' style='text-align: left; font-weight: bold;'>Periode: " + tglMulai + " s/d " + tglSelesai + "</div>\n");
            writer.write("<div class='sub-header' style='text-align: left;'>Tanggal Export: " + java.time.LocalDate.now() + "</div>\n");

            writer.write("<table>\n<tr>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>No Struk</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Tanggal Transaksi</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Total Belanja (Rp)</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Uang Bayar (Rp)</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Kembalian (Rp)</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Metode Pembayaran</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Kasir</th>\n");
            writer.write("</tr>\n");

            while (rsTrx.next()) {
                writer.write("<tr>\n");
                writer.write("  <td>" + rsTrx.getString("no_struk") + "</td>\n");
                writer.write("  <td>" + rsTrx.getString("tanggal_transaksi") + "</td>\n");
                writer.write("  <td>Rp " + String.format("%,.0f", rsTrx.getDouble("total_belanja")) + "</td>\n");
                writer.write("  <td>Rp " + String.format("%,.0f", rsTrx.getDouble("uang_bayar")) + "</td>\n");
                writer.write("  <td>Rp " + String.format("%,.0f", rsTrx.getDouble("kembalian")) + "</td>\n");
                writer.write("  <td>Tunai</td>\n");
                writer.write("  <td>" + rsTrx.getString("nama_pengguna") + "</td>\n");
                writer.write("</tr>\n");
            }
            writer.write("</table>\n");

            // Bagian 2: Tabel Ringkasan Produk Terlaris
            writer.write("<br><br>\n<div class='title'>RINGKASAN PRODUK TERLARIS</div>\n");
            writer.write("<div class='sub-header' style='text-align: left;'>Periode: " + tglMulai + " s/d " + tglSelesai + "</div>\n");
            
            writer.write("<table>\n<tr>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>No</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Nama Produk (Buah)</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Total Terjual (Qty)</th>\n");
            writer.write("  <th style='background-color: #2e7d32; color: white;'>Total Pendapatan (Rp)</th>\n");
            writer.write("</tr>\n");

            int no = 1;
            while (rsTop.next()) {
                writer.write("<tr>\n");
                writer.write("  <td style='text-align: center;'>" + (no++) + "</td>\n");
                writer.write("  <td>" + rsTop.getString("nama_produk") + "</td>\n");
                writer.write("  <td style='text-align: center;'>" + rsTop.getInt("total_terjual") + " pcs</td>\n");
                writer.write("  <td>Rp " + String.format("%,.0f", rsTop.getDouble("total_pendapatan")) + "</td>\n");
                writer.write("</tr>\n");
            }
            writer.write("</table>\n</body>\n</html>\n");

            writer.flush();
            writer.close();

            javax.swing.JOptionPane.showMessageDialog(this, "Berhasil! Laporan periode " + tglMulai + " s/d " + tglSelesai + " berhasil diexport ke Excel.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal mengexport data: " + e.getMessage());
    }
    }//GEN-LAST:event_btnExportExcelActionPerformed

    private void btnDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDashboardMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboardMouseClicked

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
    new FormDashboardAdmin().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        new FormLogin().setVisible(true); // Kembali ke halaman login
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenggunaActionPerformed
        new FormPengguna().setVisible(true); // Membuka Form Pengguna yang benar
        this.dispose();
    }//GEN-LAST:event_btnPenggunaActionPerformed

    private void btnProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdukActionPerformed
        new FormProduk().setVisible(true); // Membuka Form Produk
        this.dispose();
    }//GEN-LAST:event_btnProdukActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FormLaporanPenjualanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormLaporanPenjualanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormLaporanPenjualanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormLaporanPenjualanAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormLaporanPenjualanAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCetakLaporan;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPengguna;
    private javax.swing.JButton btnProduk;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelTanggal;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPeringatan;
    private javax.swing.JLabel lblPromo;
    private javax.swing.JLabel lblSubPeringatan;
    private javax.swing.JLabel lblTotalOmset;
    private javax.swing.JLabel lblTotalTransaksi;
    private javax.swing.JPanel panelGrafikLaporan;
    private javax.swing.JTable tblRiwayat;
    // End of variables declaration//GEN-END:variables
}
