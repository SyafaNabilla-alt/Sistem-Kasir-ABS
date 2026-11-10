/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos_alambuahsegar;

public class FormDashboardManager extends javax.swing.JFrame {

    public FormDashboardManager() {
        initComponents();
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        loadTabelRiwayat();
        tampilPromo();
        loadPeringatanRak();
        loadRingkasanDashboard();
        com.alambuahsegar.config.Session.setSidebarInfo(jLabelTanggal, jLabelUser);
        
    }
     
    

// Method bantu khusus untuk memunculkan peringatan pada tombol promo
private void gantiPeringatanPromo() {
    for (java.awt.event.ActionListener al : btnTambahPromo.getActionListeners()) {
        btnTambahPromo.removeActionListener(al);
    }
    btnTambahPromo.addActionListener(e -> {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Akses Ditolak!\nFitur tambah/kelola promo hanya dapat diakses oleh Admin.", 
            "Peringatan Hak Akses", 
            javax.swing.JOptionPane.WARNING_MESSAGE);
    });
}
    
    public void loadRingkasanDashboard() {
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        java.sql.Statement stmt = conn.createStatement();

        // 1. Total Penjualan Hari Ini (Menggunakan kolom 'total_belanja')
        String sqlPenjualan = "SELECT SUM(total_belanja) AS total_pendapatan FROM transaksi WHERE DATE(tanggal_transaksi) = CURDATE()";
        java.sql.ResultSet rsPenjualan = stmt.executeQuery(sqlPenjualan);
        if (rsPenjualan.next()) {
            double totalPendapatan = rsPenjualan.getDouble("total_pendapatan");
            String formattedRupiah = String.format("%,.2f", totalPendapatan).replace(',', 'x').replace('.', ',').replace('x', '.');
            
            // Sesuaikan nama label penjualan di form kamu (misal: lblValPenjualanHariIni)
            lblValPenjualan.setText("Rp " + formattedRupiah);
        }

        // 2. Total Transaksi / Struk Hari Ini
        String sqlStruk = "SELECT COUNT(*) AS jml_struk FROM transaksi WHERE DATE(tanggal_transaksi) = CURDATE()";
        java.sql.ResultSet rsStruk = stmt.executeQuery(sqlStruk);
        if (rsStruk.next()) {
            int jmlStruk = rsStruk.getInt("jml_struk");
            lblValStruk.setText(jmlStruk + " Struk");
        }

        // 3. Peringatan Stok
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
        lblTotalPeringatan.setText(totalPeringatan + " Produk");
        lblSubPeringatan.setText(totalHabis + " habis, " + totalSelisih + " selisih, " + totalMenipis + " menipis");

        // 4. Promo Sistem Aktif
        String sqlPromo = "SELECT nama_produk, harga_diskon FROM produk WHERE harga_diskon > 0";
        java.sql.ResultSet rsPromo = stmt.executeQuery(sqlPromo);

        javax.swing.table.DefaultTableModel modelPromo = (javax.swing.table.DefaultTableModel) tblPromo.getModel();
        modelPromo.setRowCount(0);

        int countPromo = 0;

        while (rsPromo.next()) {
            String nama = rsPromo.getString("nama_produk");
            double diskon = rsPromo.getDouble("harga_diskon");
            String diskonFormatted = String.format("%,.0f", diskon).replace(',', '.');

            modelPromo.addRow(new Object[]{
                nama.toUpperCase(), 
                "Rp " + diskonFormatted
            });

            countPromo++;
        }

        lblValPromo.setText(countPromo + " Promo");

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat ringkasan dashboard: " + e.getMessage());
    }
}
    
    public void tampilPromo() {
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        String sql = "SELECT nama_produk, harga_diskon FROM produk WHERE harga_diskon > 0";
        java.sql.Statement stmt = conn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery(sql);

        // Ambil model dari tabel promo (tblPromo) yang ada di desain
        javax.swing.table.DefaultTableModel modelPromo = (javax.swing.table.DefaultTableModel) tblPromo.getModel();
        modelPromo.setRowCount(0); // Kosongkan data lama supaya tidak numpuk

        int countPromo = 0;

        while (rs.next()) {
            String nama = rs.getString("nama_produk");
            double diskon = rs.getDouble("harga_diskon");
            
            String diskonFormatted = String.format("%,.0f", diskon).replace(',', '.');
            
            // 1. Ubah teks jadi huruf awal kapital
            String namaFormatted = "";
            if (nama != null && !nama.isEmpty()) {
                String[] words = nama.toLowerCase().split(" ");
                StringBuilder sb = new StringBuilder();
                for (String w : words) {
                    if (!w.isEmpty()) {
                        sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
                    }
                }
                namaFormatted = sb.toString().trim();
            }
            
            // 2. Masukkan 'namaFormatted' ke dalam tabel (JANGAN pakai .toUpperCase lagi!)
            modelPromo.addRow(new Object[]{
                namaFormatted, 
                "Rp " + diskonFormatted
            });
            
            countPromo++;
        }
        lblValPromo.setText(countPromo + " Promo");
        
        tblPromo.setRowHeight(25); 
        tblPromo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        tblPromo.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat promo: " + e.getMessage());
    }
}
    
    public void loadPeringatanRak() {
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        java.sql.Statement stmt = conn.createStatement();

        String sql = "SELECT * FROM produk";
        java.sql.ResultSet rs = stmt.executeQuery(sql);

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblPeringatanStok.getModel();
        model.setRowCount(0);

        int totalHabis = 0;
        int totalSelisih = 0;
        int totalMenipis = 0;

        while (rs.next()) {
            String namaProduk = rs.getString("nama_produk");
            int stokRak = rs.getInt("stok_di_rak");
            int stokSistem = rs.getInt("total_stok_sistem");

            String status = "Aman";
            
            if (stokRak == 0 && stokSistem == 0) {
                status = "Habis";
                totalHabis++;
            } else if (stokRak != stokSistem) {
                status = "Selisih";
                totalSelisih++;
            } else if (stokRak <= 5) {
                status = "Menipis";
                totalMenipis++;
            } else {
                status = "Aman";
            }

            // Masukkan ke tabel hanya yang statusnya bukan Aman
            if (!status.equals("Aman")) {
                model.addRow(new Object[]{namaProduk, stokRak, stokSistem, status});
            }
        }

        int totalPeringatan = totalHabis + totalSelisih + totalMenipis;

        lblTotalPeringatan.setText(totalPeringatan + " Produk");
        lblSubPeringatan.setText(totalHabis + " habis, " + totalSelisih + " selisih, " + totalMenipis + " menipis");

        tblPeringatanStok.setRowHeight(25); 
        tblPeringatanStok.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        tblPeringatanStok.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 11));
        tblPeringatanStok.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null && (value.toString().equalsIgnoreCase("Selisih") || 
                                      value.toString().equalsIgnoreCase("Menipis") || 
                                      value.toString().equalsIgnoreCase("Habis"))) {
                    c.setForeground(java.awt.Color.RED);
                    setFont(new java.awt.Font("Sans Serif", java.awt.Font.BOLD | java.awt.Font.ITALIC, 12));
                } else {
                    c.setForeground(table.getForeground());
                    setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
                }
                
                setHorizontalAlignment(javax.swing.JLabel.CENTER);
                return c;
            }
        });

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat data peringatan stok: " + e.getMessage());
    }
}

    public void loadTabelRiwayat() {
    // 1. Buat model tabel untuk riwayat transaksi
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
    model.addColumn("KODE TRX");
    model.addColumn("WAKTU");
    model.addColumn("TOTAL");

    try {
        // 2. Ambil koneksi database
        java.sql.Connection conn = (java.sql.Connection) com.alambuahsegar.config.DatabaseConnection.getConnection();
        
        // 3. Query menggunakan nama kolom yang benar: no_struk, tanggal_transaksi, total_belanja[cite: 15]
        String sql = "SELECT no_struk, tanggal_transaksi, total_belanja FROM transaksi ORDER BY id_transaksi DESC LIMIT 10";
        java.sql.Statement stmt = conn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery(sql);

        // 4. Masukkan data ke baris tabel
        while (rs.next()) {
    double totalBelanja = rs.getDouble("total_belanja");
    
    String formatTotal = String.format("Rp %,.0f", totalBelanja)
                           .replace(",", "."); // Mengubah pemisah ribuan jadi titik khas Indonesia

    model.addRow(new Object[]{
        rs.getString("no_struk"),
        rs.getString("tanggal_transaksi"),
        formatTotal 
    });
}
        
        jTableRiwayat.setModel(model);
        jTableRiwayat.getTableHeader().setDefaultRenderer(new javax.swing.table.TableCellRenderer() {
            private final javax.swing.table.TableCellRenderer defaultRenderer = jTableRiwayat.getTableHeader().getDefaultRenderer();
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new java.awt.Color(34, 94, 55)); 
                c.setForeground(java.awt.Color.WHITE);         
                c.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
                if (c instanceof javax.swing.JComponent) {
                    ((javax.swing.JComponent) c).setOpaque(true);
                }
                return c;
            }
        });

        
        javax.swing.table.TableCellRenderer customRenderer = new javax.swing.table.TableCellRenderer() {
            private final javax.swing.JLabel label = new javax.swing.JLabel();

            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                label.setOpaque(true); 
                label.setText(value != null ? value.toString() : "");
                label.setFont(table.getFont());
                label.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 8, 0, 8)); 
                
                if (isSelected) {
                    label.setBackground(new java.awt.Color(180, 220, 190)); 
                    label.setForeground(java.awt.Color.BLACK);
                } else {
                    if (row % 2 == 0) {
                        label.setBackground(new java.awt.Color(245, 250, 245)); 
                    } else {
                        label.setBackground(java.awt.Color.WHITE); 
                    }
                    label.setForeground(new java.awt.Color(50, 50, 50)); 
                }
                return label;
            }
        };

        // Terapkan renderer ke seluruh tipe data kolom tabel riwayat
        jTableRiwayat.setDefaultRenderer(Object.class, customRenderer);
        jTableRiwayat.setDefaultRenderer(String.class, customRenderer);
        jTableRiwayat.setDefaultRenderer(Number.class, customRenderer);
        jTableRiwayat.setDefaultRenderer(Double.class, customRenderer);
        jTableRiwayat.setDefaultRenderer(Integer.class, customRenderer);
        
        jTableRiwayat.setRowHeight(30); 
        jTableRiwayat.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
        jTableRiwayat.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        jTableRiwayat.getTableHeader().setForeground(java.awt.Color.BLACK);
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat riwayat transaksi: " + e.getMessage());
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

        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel28 = new javax.swing.JLabel();
        pnlSidebar = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        btnLaporanPenjualan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelTanggal = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblValPenjualan = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        lblValStruk = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        lblSubPeringatan = new javax.swing.JLabel();
        lblTotalPeringatan = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblValPromo = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel12 = new GrafikPenjualanPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPeringatanStok = new javax.swing.JTable();
        jLabel24 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableRiwayat = new javax.swing.JTable();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnTambahPromo = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPromo = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Rabu 30 Sep 2026");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jLabel28.setText("jLabel28");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setSize(new java.awt.Dimension(1366, 768));
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlSidebar.setBackground(new java.awt.Color(55, 85, 52));
        pnlSidebar.setForeground(new java.awt.Color(255, 255, 255));
        pnlSidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icondash2-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 60, 50));

        btnDashboard.setBackground(new java.awt.Color(204, 255, 204));
        btnDashboard.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(3, 159, 25));
        btnDashboard.setText("Dashboard");
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });
        pnlSidebar.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 230, 50));

        btnLaporanPenjualan.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        btnLaporanPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        btnLaporanPenjualan.setText("Laporan Penjualan");
        btnLaporanPenjualan.setContentAreaFilled(false);
        btnLaporanPenjualan.setFocusPainted(false);
        btnLaporanPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaporanPenjualanActionPerformed(evt);
            }
        });
        pnlSidebar.add(btnLaporanPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 200, 60));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTanggal.setText("Rabu 30 Sep 2026");
        jPanel2.add(jLabelTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 120, 20));

        pnlSidebar.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 620, 130, 20));

        jLabelUser.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUser.setText("User Admin(Admin@gmail.com)");
        pnlSidebar.add(jLabelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 640, 190, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Logo_ABS removebg.png"))); // NOI18N
        pnlSidebar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 160, 110));

        jLabel8.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MENU UTAMA");
        pnlSidebar.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, 15));

        jLabel11.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("DATA MASTER");
        pnlSidebar.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, 15));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconlaporanpenjualan-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 70, 40));

        jLabel23.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("AKUN");
        pnlSidebar.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 40, 20));

        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconkeluar-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 50, 40));

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
        pnlSidebar.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 90, 40));

        getContentPane().add(pnlSidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 750));

        jPanel4.setBackground(new java.awt.Color(79, 139, 91));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("TOTAL PENJUALAN HARI INI");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, -10, 180, 60));

        lblValPenjualan.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); // NOI18N
        lblValPenjualan.setForeground(new java.awt.Color(255, 255, 255));
        lblValPenjualan.setText("Rp 15.250.00");
        jPanel4.add(lblValPenjualan, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 130, 70));

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconpenjualan-removebg-preview.png"))); // NOI18N
        jPanel4.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 50, 60));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 190, 90));

        jPanel5.setBackground(new java.awt.Color(79, 139, 91));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TOTAL TRANSAKSI");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 100, -1));

        lblValStruk.setFont(new java.awt.Font("Sans Serif Collection", 1, 18)); // NOI18N
        lblValStruk.setForeground(new java.awt.Color(255, 255, 255));
        lblValStruk.setText("245 Struk");
        jPanel5.add(lblValStruk, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, -1, 50));

        jLabel12.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Hari ini");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 70, 40));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconstruk-removebg-preview.png"))); // NOI18N
        jPanel5.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 50, 60));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 190, 90));

        jPanel6.setBackground(new java.awt.Color(79, 139, 91));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("PERINGATAN STOK");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 110, -1));

        lblSubPeringatan.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        lblSubPeringatan.setForeground(new java.awt.Color(255, 255, 255));
        lblSubPeringatan.setText("3 habis, 1 selisih,10 menipis");
        jPanel6.add(lblSubPeringatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));

        lblTotalPeringatan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalPeringatan.setForeground(new java.awt.Color(255, 255, 255));
        jPanel6.add(lblTotalPeringatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 70, 20));

        jLabel32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconperingatan-removebg-preview.png"))); // NOI18N
        jPanel6.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 40, 80));

        getContentPane().add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 10, 190, 90));

        jPanel7.setBackground(new java.awt.Color(79, 139, 91));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("PROMO SISTEM AKTIF");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 140, -1));

        lblValPromo.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        lblValPromo.setForeground(new java.awt.Color(255, 255, 255));
        lblValPromo.setText("5 Promo");
        jPanel7.add(lblValPromo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 150, 30));

        jLabel18.setFont(new java.awt.Font("Sans Serif Collection", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Hari ini");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 50, -1));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icondiskon-removebg-preview.png"))); // NOI18N
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 50, 80));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 10, 190, 90));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(3, 159, 25));
        jLabel22.setText("Tren Penjualan Per Jam");
        jPanel10.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 570, 360));

        getContentPane().add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 560, 350));

        tblPeringatanStok.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "PRODUK BUAH", "STOK RAK", "SISTEM ", "STATUS"
            }
        ));
        jScrollPane1.setViewportView(tblPeringatanStok);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 560, 200));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 51));
        jLabel24.setText("Daftar Promo Sistem");
        getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        jLabel19.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(3, 159, 25));
        jLabel19.setText("Peringatan Rak vs Sistem");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 230, 20));

        jTableRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "KODE TRX", "WAKTU", "TOTAL"
            }
        ));
        jScrollPane4.setViewportView(jTableRiwayat);

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 460, 520, 230));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(3, 159, 25));
        jLabel25.setText("Daftar Promo Sistem");
        getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel26.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(3, 159, 25));
        jLabel26.setText("Riwayat Transaksi Terakhir");
        getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 430, -1, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTambahPromo.setBackground(new java.awt.Color(3, 159, 25));
        btnTambahPromo.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnTambahPromo.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahPromo.setText("+Tambah Promo Buah");
        btnTambahPromo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahPromoActionPerformed(evt);
            }
        });
        jPanel3.add(btnTambahPromo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 500, 30));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(3, 159, 25));
        jLabel21.setText("Daftar Promo Sistem");
        jPanel3.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        tblPromo.setForeground(new java.awt.Color(55, 85, 52));
        tblPromo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produk", "Diskon"
            }
        ));
        jScrollPane3.setViewportView(tblPromo);

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 500, 200));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));

        jLabel3.setFont(new java.awt.Font("Serif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(3, 159, 25));
        jLabel3.setText("AKTIF");
        jPanel8.add(jLabel3);

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 60, 20));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 130, 520, 290));
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1710, 140, 720, 230));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
    new FormDashboardManager().setVisible(true); // Kembali ke dashboard manager
    this.dispose();
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnLaporanPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaporanPenjualanActionPerformed
    new FormLaporanPenjualanManager().setVisible(true); // Ubah jadi Form Laporan Manager!
    this.dispose();
    }//GEN-LAST:event_btnLaporanPenjualanActionPerformed

    private void btnTambahPromoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPromoActionPerformed
    String roleLogin = com.alambuahsegar.config.Session.role;
    System.out.println("Role saat ini: [" + roleLogin + "]");
    
    // 1. Validasi Hak Akses: Hanya Admin yang bisa mengakses
    if (roleLogin == null || !roleLogin.trim().equalsIgnoreCase("admin")) {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Akses Ditolak! Fitur Tambah Promo hanya dapat diakses oleh Admin.", 
            "Peringatan Keamanan", 
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return; 
    }
    
    // 2. Jika lolos sebagai Admin, buka Form Produk sebagai shortcut
    // (Pastikan nama class form produk kamu FormProduk, sesuaikan jika berbeda)
    new FormProduk().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnTambahPromoActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
    // Refresh otomatis semua data dashboard saat halaman diaktifkan kembali
    loadTabelRiwayat();
    tampilPromo();
    loadPeringatanRak();
    loadRingkasanDashboard();
    }//GEN-LAST:event_formWindowGainedFocus

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
    new FormLogin().setVisible(true); // Kembali ke halaman login
    this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

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
            java.util.logging.Logger.getLogger(FormDashboardManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormDashboardManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormDashboardManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormDashboardManager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormDashboardManager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLaporanPenjualan;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnTambahPromo;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabelTanggal;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTableRiwayat;
    private javax.swing.JLabel lblSubPeringatan;
    private javax.swing.JLabel lblTotalPeringatan;
    private javax.swing.JLabel lblValPenjualan;
    private javax.swing.JLabel lblValPromo;
    private javax.swing.JLabel lblValStruk;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JTable tblPeringatanStok;
    private javax.swing.JTable tblPromo;
    // End of variables declaration//GEN-END:variables
}
