/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos_alambuahsegar;

/**
 *
 * @author ASUS
 */
public class FormPengguna extends javax.swing.JFrame {
    private int idSelected = 0;
    /**
     * Creates new form FormPengguna
     */
    public FormPengguna() {
        initComponents();
        com.alambuahsegar.config.Session.setSidebarInfo(jLabelTanggal, jLabelUser);
        loadData();
        txtCari.setText("Cari nama pengguna, username, atau role...");
    txtCari.setForeground(new java.awt.Color(153, 153, 153));
    txtCari.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (txtCari.getText().equals("Cari nama pengguna, username, atau role...")) {
                txtCari.setText("");
                txtCari.setForeground(new java.awt.Color(0, 0, 0));
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (txtCari.getText().trim().isEmpty()) {
                txtCari.setText("Cari nama pengguna, username, atau role...");
                txtCari.setForeground(new java.awt.Color(153, 153, 153));
            }
        }
    });
    }
    
    private void clearForm() {
    idSelected = 0;
    txtNama.setText("");
    txtUsername.setText("");
    txtPassword.setText("");
    cmbRole.setSelectedIndex(0);
    cmbStatus.setSelectedIndex(0);
    txtNama.requestFocus();
}
    
    public void loadData() {
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
    model.addColumn("No");
    model.addColumn("ID"); // Kolom ID disembunyikan
    model.addColumn("Nama");
    model.addColumn("Username");
    model.addColumn("Role");
    model.addColumn("Status");
    model.addColumn("Terakhir Login");
    model.addColumn("Aksi");

    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        
        String cari = txtCari.getText().trim();
        String filterRole = cmbFilterRole.getSelectedItem().toString();
        String filterStatus = cmbFilterStatus.getSelectedItem().toString();

        String sql = "SELECT * FROM pengguna WHERE (nama_pengguna LIKE ? OR username LIKE ?)";
        
        if (!filterRole.equals("Semua Role")) {
            sql += " AND role = '" + filterRole + "'";
        }
        if (!filterStatus.equals("Semua Status")) {
            sql += " AND status = '" + filterStatus + "'";
        }

        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + cari + "%");
        pst.setString(2, "%" + cari + "%");
        java.sql.ResultSet rs = pst.executeQuery();

        int no = 1;
        while (rs.next()) {
            String terakhirLogin = rs.getString("terakhir_login");
            if (terakhirLogin == null || terakhirLogin.isEmpty()) {
                terakhirLogin = "Belum pernah";
            }

            model.addRow(new Object[]{
                no++,
                rs.getInt("id_pengguna"),
                rs.getString("nama_pengguna"),
                rs.getString("username"),
                rs.getString("role"),
                rs.getString("status"),
                terakhirLogin,
                "Hapus" // Teks di kolom aksi
            });
        }
        tblPengguna.setModel(model);
        
        // Sembunyikan kolom ID (kolom ke-1)
        tblPengguna.getColumnModel().getColumn(1).setMinWidth(0);
        tblPengguna.getColumnModel().getColumn(1).setMaxWidth(0);
        tblPengguna.getColumnModel().getColumn(1).setWidth(0);

        // Atur tinggi baris dan font SansSerif
        tblPengguna.setRowHeight(35);
        tblPengguna.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 13));
        tblPengguna.getTableHeader().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 13));

        // Membuat teks pada kolom "Aksi" (kolom ke-7) berwarna merah tebal
        tblPengguna.getColumnModel().getColumn(7).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(java.awt.Color.RED);
                setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 13));
                setHorizontalAlignment(javax.swing.JLabel.CENTER);
                return c;
            }
        });

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memuat data pengguna: " + e.getMessage());
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

        pnlSidebar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnProduk = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        btnPengguna = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelUser = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelTanggal = new javax.swing.JLabel();
        pnlKontenUtama = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cmbFilterRole = new javax.swing.JComboBox<>();
        cmbFilterStatus = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPengguna = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        cmbRole = new javax.swing.JComboBox<>();
        btnResetPassword = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        btnSimpan1 = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlSidebar.setBackground(new java.awt.Color(55, 85, 52));
        pnlSidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("MENU UTAMA");
        pnlSidebar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, 15));

        btnDashboard.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setContentAreaFilled(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });
        pnlSidebar.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 140, 40));

        jLabel3.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DATA MASTER");
        pnlSidebar.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, 15));

        btnProduk.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
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
        pnlSidebar.add(btnProduk, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, 100, 40));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconpengguna2-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 60, 50));

        btnPengguna.setBackground(new java.awt.Color(204, 255, 204));
        btnPengguna.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        btnPengguna.setForeground(new java.awt.Color(55, 85, 52));
        btnPengguna.setText("Pengguna");
        btnPengguna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenggunaActionPerformed(evt);
            }
        });
        pnlSidebar.add(btnPengguna, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 280, 260, 50));

        jLabel4.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("LAPORAN");
        pnlSidebar.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, 15));

        jButton5.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Laporan Penjualan");
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);
        jButton5.setFocusPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        pnlSidebar.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 260, 60));

        jLabel5.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("AKUN");
        pnlSidebar.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 410, -1, 20));

        jLabel7.setBackground(new java.awt.Color(153, 153, 153));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Logo_ABS removebg.png"))); // NOI18N
        pnlSidebar.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 160, 110));

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
        pnlSidebar.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 90, 40));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icondash-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 60, 30));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconptoduk-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 60, 40));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconlaporanpenjualan-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, -1, 40));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconkeluar-removebg-preview.png"))); // NOI18N
        pnlSidebar.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 50, 40));

        jLabelUser.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUser.setText("User Admin(Admin@gmail.com)");
        pnlSidebar.add(jLabelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 640, 160, 40));

        jLabelTanggal.setText("Rabu 30 Sep 2026");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTanggal)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabelTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSidebar.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 620, 120, 20));

        pnlKontenUtama.setBackground(new java.awt.Color(204, 204, 204));
        pnlKontenUtama.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Century751 SeBd BT", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(55, 85, 52));
        jLabel2.setText("Daftar Pengguna");
        pnlKontenUtama.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 430, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconpengguna2_-_Copy-removebg-preview.png"))); // NOI18N
        pnlKontenUtama.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 60, 50));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Kelola data pengguna yang dapat mengakses sistem");
        pnlKontenUtama.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, 30));

        cmbFilterRole.setFont(new java.awt.Font("Century751 BT", 0, 14)); // NOI18N
        cmbFilterRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Role", "Admin", "Kasir", "Manager" }));
        cmbFilterRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFilterRoleActionPerformed(evt);
            }
        });
        pnlKontenUtama.add(cmbFilterRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 110, 170, 30));

        cmbFilterStatus.setFont(new java.awt.Font("Century751 BT", 0, 14)); // NOI18N
        cmbFilterStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Status", "Aktif", "Nonaktif" }));
        cmbFilterStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFilterStatusActionPerformed(evt);
            }
        });
        pnlKontenUtama.add(cmbFilterStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 170, 30));

        tblPengguna.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Nama", "Username", "Role", "Status", "Terakhir Login", "Aksi"
            }
        ));
        tblPengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPenggunaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPengguna);

        pnlKontenUtama.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 760, 510));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icontambahpengguna-removebg-preview.png"))); // NOI18N
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 60, 38));

        jLabel16.setFont(new java.awt.Font("Century751 SeBd BT", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(55, 85, 52));
        jLabel16.setText("Tambah Pengguna");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 180, 30));

        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Isi data pengguna baru dengan lengkap.");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 230, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Status");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 100, 20));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Nama Lengkap");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 100, 20));
        jPanel2.add(txtNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 290, 30));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Username");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 100, 20));
        jPanel2.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 290, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Password");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 100, 20));
        jPanel2.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 290, 30));

        cmbStatus.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aktif", "Nonaktif" }));
        jPanel2.add(cmbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 290, 30));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Role");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 100, 20));

        cmbRole.setFont(new java.awt.Font("Franklin Gothic Medium", 0, 14)); // NOI18N
        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih role", "Admin", "Kasir", "Manager" }));
        jPanel2.add(cmbRole, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 290, 30));

        btnResetPassword.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        btnResetPassword.setText("Reset Password");
        btnResetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetPasswordActionPerformed(evt);
            }
        });
        jPanel2.add(btnResetPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 484, 290, 30));

        btnBatal.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        btnBatal.setForeground(new java.awt.Color(55, 85, 52));
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });
        jPanel2.add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, 130, 30));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(55, 85, 52));
        jLabel23.setText("Keterangan Role");
        jPanel3.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 140, 30));

        jLabel24.setFont(new java.awt.Font("Constantia", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(51, 153, 0));
        jLabel24.setText(": Kelola Transaksi Penjualan");
        jPanel3.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        jLabel25.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 153, 0));
        jLabel25.setText("Kasir");
        jPanel3.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 20));

        jLabel26.setFont(new java.awt.Font("Constantia", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 153, 0));
        jLabel26.setText(":  Kelola Laporan Penjualan");
        jPanel3.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, -1, 20));

        jLabel27.setFont(new java.awt.Font("Constantia", 0, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 153, 0));
        jLabel27.setText(":  Akses Penuh Kecuali Transaksi");
        jPanel3.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 210, 30));

        jLabel28.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 153, 0));
        jLabel28.setText("Admin");
        jPanel3.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 50, 30));

        jLabel29.setFont(new java.awt.Font("Yu Gothic", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 153, 0));
        jLabel29.setText("Manager");
        jPanel3.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 40));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, 290, 110));

        btnSimpan1.setBackground(new java.awt.Color(3, 159, 25));
        btnSimpan1.setFont(new java.awt.Font("Serif", 0, 14)); // NOI18N
        btnSimpan1.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan1.setText("Simpan");
        btnSimpan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan1ActionPerformed(evt);
            }
        });
        jPanel2.add(btnSimpan1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 450, 130, 30));

        pnlKontenUtama.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 20, 340, 650));

        txtCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCariActionPerformed(evt);
            }
        });
        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });
        pnlKontenUtama.add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 330, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlKontenUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 1195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlKontenUtama, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(321, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPenggunaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenggunaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPenggunaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        new FormLaporanPenjualanAdmin().setVisible(true); // Sesuaikan nama class form laporanmu
    this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        new FormLogin().setVisible(true); // Sesuaikan nama class form loginmu
    this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdukActionPerformed
        new FormProduk().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnProdukActionPerformed

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // Cek role yang sedang login untuk mengarahkan ke dashboard yang tepat
    String role = com.alambuahsegar.config.Session.role;
    if (role != null && role.equalsIgnoreCase("Manager")) {
        new FormDashboardManager().setVisible(true);
    } else {
        new FormDashboardAdmin().setVisible(true);
    }
    this.dispose(); // Menutup form pengguna saat ini
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnResetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetPasswordActionPerformed
    if (idSelected == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Pilih dulu data pengguna dari tabel yang ingin di-reset password-nya!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    String namaUser = txtNama.getText();
    String passwordBaru = javax.swing.JOptionPane.showInputDialog(this, "Masukkan Password Baru untuk " + namaUser + ":", "Reset Password", javax.swing.JOptionPane.QUESTION_MESSAGE);

    if (passwordBaru != null && !passwordBaru.trim().isEmpty()) {
        try {
            java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
            String sql = "UPDATE pengguna SET password = ? WHERE id_pengguna = ?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, passwordBaru.trim());
            pst.setInt(2, idSelected);
            pst.executeUpdate();

            javax.swing.JOptionPane.showMessageDialog(this, "Password untuk " + namaUser + " berhasil di-reset!");
            clearForm();
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Gagal mereset password: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnResetPasswordActionPerformed

    private void btnSimpan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan1ActionPerformed
    String nama = txtNama.getText().trim();
    String username = txtUsername.getText().trim();
    String password = txtPassword.getText().trim();
    String role = cmbRole.getSelectedItem().toString();
    String status = cmbStatus.getSelectedItem().toString();

    if (nama.isEmpty() || username.isEmpty() || role.equals("Pilih role")) {
        javax.swing.JOptionPane.showMessageDialog(this, "Nama, Username, dan Role wajib diisi!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();

        if (idSelected == 0) {
            // Tambah Data Baru
            if (password.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Password wajib diisi untuk pengguna baru!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Perhatikan penggunaan 'nama_pengguna' sesuai database kamu
            String sql = "INSERT INTO pengguna (nama_pengguna, username, password, role, status) VALUES (?, ?, ?, ?, ?)";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, role);
            pst.setString(5, status);
            pst.executeUpdate();

            javax.swing.JOptionPane.showMessageDialog(this, "Data pengguna berhasil ditambahkan!");
        } else {
            // Edit Data
            String sql;
            java.sql.PreparedStatement pst;
            
            if (!password.isEmpty()) {
                sql = "UPDATE pengguna SET nama_pengguna = ?, username = ?, password = ?, role = ?, status = ? WHERE id_pengguna = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, nama);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.setString(4, role);
                pst.setString(5, status);
                pst.setInt(6, idSelected);
            } else {
                sql = "UPDATE pengguna SET nama_pengguna = ?, username = ?, role = ?, status = ? WHERE id_pengguna = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, nama);
                pst.setString(2, username);
                pst.setString(3, role);
                pst.setString(4, status);
                pst.setInt(5, idSelected);
            }
            pst.executeUpdate();

            javax.swing.JOptionPane.showMessageDialog(this, "Data pengguna berhasil diperbarui!");
        }

        clearForm();
        loadData();

    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSimpan1ActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
    clearForm();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tblPenggunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPenggunaMouseClicked
    int row = tblPengguna.getSelectedRow();
    int col = tblPengguna.getSelectedColumn();
    
    if (row != -1) {
        idSelected = Integer.parseInt(tblPengguna.getValueAt(row, 1).toString());
        
        // Cek jika yang diklik adalah kolom "Aksi" (kolom ke-7 / tulisan Hapus)
        if (col == 7) {
            String namaUser = tblPengguna.getValueAt(row, 2).toString();
            int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus pengguna \"" + namaUser + "\"?", 
                "Konfirmasi Hapus", 
                javax.swing.JOptionPane.YES_NO_OPTION, 
                javax.swing.JOptionPane.WARNING_MESSAGE);
                
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                try {
                    java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
                    String sql = "DELETE FROM pengguna WHERE id_pengguna = ?";
                    java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, idSelected);
                    pst.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(this, "Pengguna berhasil dihapus!");
                    clearForm();
                    loadData();
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Gagal menghapus pengguna: " + e.getMessage());
                }
            }
        } else {
            // Jika klik selain kolom aksi, data naik ke form sebelah kanan untuk diedit
            txtNama.setText(tblPengguna.getValueAt(row, 2).toString());
            txtUsername.setText(tblPengguna.getValueAt(row, 3).toString());
            cmbRole.setSelectedItem(tblPengguna.getValueAt(row, 4).toString());
            cmbStatus.setSelectedItem(tblPengguna.getValueAt(row, 5).toString());
            txtPassword.setText(""); 
        }
    }
    }//GEN-LAST:event_tblPenggunaMouseClicked

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
    loadData();
    }//GEN-LAST:event_txtCariKeyReleased

    private void cmbFilterRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilterRoleActionPerformed
    loadData();
    }//GEN-LAST:event_cmbFilterRoleActionPerformed

    private void cmbFilterStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFilterStatusActionPerformed
    loadData();
    }//GEN-LAST:event_cmbFilterStatusActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
    String keyword = txtCari.getText().trim();
    
    // Jika isinya masih placeholder atau kosong, muat ulang semua data seperti biasa
    if (keyword.equals("Cari nama pengguna, username, atau role...") || keyword.isEmpty()) {
        loadData(); // Sesuaikan dengan nama fungsi load data di form kamu
        return;
    }
    
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        
        // Query pencarian berdasarkan nama, username, atau role
        String sql = "SELECT * FROM pengguna WHERE nama_pengguna LIKE ? OR username LIKE ? OR role LIKE ?";
        java.sql.PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, "%" + keyword + "%");
        pst.setString(2, "%" + keyword + "%");
        pst.setString(3, "%" + keyword + "%");
        
        java.sql.ResultSet rs = pst.executeQuery();
        
        // Masukkan hasil pencarian ke dalam JTable (sesuaikan nama variabel tabel kamu, misal tblPengguna)
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblPengguna.getModel();
        model.setRowCount(0); // Kosongkan tabel sebelum diisi data baru
        
        int no = 1;
        while (rs.next()) {
            model.addRow(new Object[]{
                no++,
                rs.getString("nama_pengguna"),
                rs.getString("username"),
                rs.getString("role"),
                rs.getString("status"),
                rs.getString("terakhir_login")
            });
        }
        
    } catch (Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal melakukan pencarian: " + e.getMessage());
    }
    }//GEN-LAST:event_txtCariActionPerformed

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
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPengguna.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormPengguna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPengguna;
    private javax.swing.JButton btnProduk;
    private javax.swing.JButton btnResetPassword;
    private javax.swing.JButton btnSimpan1;
    private javax.swing.JComboBox<String> cmbFilterRole;
    private javax.swing.JComboBox<String> cmbFilterStatus;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelTanggal;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlKontenUtama;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JTable tblPengguna;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
