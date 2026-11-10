/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pos_alambuahsegar;

/**
 *
 * @author ASUS
 */
public class FormTransaksi extends javax.swing.JFrame {

    /**
     * Creates new form FormTransaksi
     */
    public FormTransaksi() {
    initComponents();
    int maxY = 0;
    for (java.awt.Component comp : pnlIsiBuah.getComponents()) {
    int bottomY = comp.getY() + comp.getHeight();
    if (bottomY > maxY) {
        maxY = bottomY;
    }
    }
    pnlIsiBuah.setPreferredSize(new java.awt.Dimension(pnlIsiBuah.getPreferredSize().width, 780));
    pnlIsiBuah.revalidate();
    pnlIsiBuah.repaint();
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel();
    model.addColumn("NO");
    model.addColumn("NAMA PRODUK");
    model.addColumn("QTY");
    model.addColumn("HARGA PRODUK");
    model.addColumn("DISKON");
    model.addColumn("SUBTOTAL");
    tblKeranjang.setModel(model);
    com.alambuahsegar.config.Session.setSidebarInfo(jLabelTanggal, jLabelUser);
    txtCari.setText("Cari buah...");
    txtCari.setForeground(new java.awt.Color(153, 153, 153)); 

    txtCari.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusGained(java.awt.event.FocusEvent evt) {
        if (txtCari.getText().equals("Cari buah...")) {
            txtCari.setText("");
            txtCari.setForeground(new java.awt.Color(0, 0, 0)); 
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent evt) {
        if (txtCari.getText().trim().isEmpty()) {
            txtCari.setText("Cari buah...");
            txtCari.setForeground(new java.awt.Color(153, 153, 153)); 
        }
    }
    });
    }
    
    
    public void cetakStrukKasir(String kodeTrx, String tanggal, String kasir, javax.swing.table.TableModel modelItem, double totalBelanja, double bayar, double kembalian) {
        java.awt.print.PrinterJob printerJob = java.awt.print.PrinterJob.getPrinterJob();
        
        // ATUR UKURAN KERTAS SECARA DINAMIS BERDASARKAN JUMLAH ITEM
        java.awt.print.PageFormat pageFormat = printerJob.defaultPage();
        java.awt.print.Paper paper = pageFormat.getPaper();
        double width = 200.0; // Lebar tetap untuk struk thermal
        
        // Hitung tinggi kertas otomatis (menyesuaikan tambahan baris alamat)
        int jumlahItem = modelItem.getRowCount();
        double height = 180.0 + (jumlahItem * 25.0); 
        
        paper.setSize(width, height);
        paper.setImageableArea(5, 5, width - 10, height - 10);
        pageFormat.setPaper(paper);

        printerJob.setPrintable(new java.awt.print.Printable() {
            @Override
            public int print(java.awt.Graphics graphics, java.awt.print.PageFormat pf, int pageIndex) throws java.awt.print.PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphics;
                double pageWidth = pf.getWidth();
                double lebarStruk = 200.0;
                double xOffset = (pageWidth > lebarStruk) ? (pageWidth - lebarStruk) / 2 : 0;
                g2d.translate(pf.getImageableX(), pf.getImageableY());

                g2d.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 8));
                int y = 10;
                int x = 2;

                // Kop Struk Toko Alam Buah Segar (Alamat Baru)
                g2d.drawString("    TOKO ALAM BUAH SEGAR    ", x, y); y += 10;
                g2d.drawString(" Tenjolaya, Kec. Pasirjambu ", x, y); y += 10;
                g2d.drawString("  Kab. Bandung, Jabar 40972 ", x, y); y += 10;
                g2d.drawString("=============================", x, y); y += 10;
                g2d.drawString("No Trx  : " + kodeTrx, x, y); y += 10;
                g2d.drawString("Tanggal : " + tanggal, x, y); y += 10;
                g2d.drawString("Kasir   : " + kasir, x, y); y += 10;
                g2d.drawString("-----------------------------", x, y); y += 10;

                // Daftar Belanja Produk dari Tabel Keranjang
                for (int i = 0; i < modelItem.getRowCount(); i++) {
                String nama = modelItem.getValueAt(i, 1).toString();  // Nama Produk
                String qty = modelItem.getValueAt(i, 2).toString();   // Qty
                String hargaNormal = modelItem.getValueAt(i, 3).toString(); // Harga Asli
                String diskon = modelItem.getValueAt(i, 4).toString();      // Diskon
                String subtotal = modelItem.getValueAt(i, 5).toString();    // Subtotal
    
                if (nama.length() > 18) nama = nama.substring(0, 18);
    
                g2d.drawString(nama, x, y); y += 9;
                g2d.drawString("  " + qty + " x Rp " + hargaNormal, x, y); y += 9;
    
                // Kalau ada diskon (tidak 0), cetak info diskonnya di bawah nama produk
                if (!diskon.equals("Rp 0") && !diskon.equals("0")) {
                g2d.drawString("  Diskon: -" + diskon, x, y); y += 9;
                }
    
                g2d.drawString("  Subtotal   = Rp " + subtotal, x, y); y += 10;
                }

                g2d.drawString("-----------------------------", x, y); y += 10;
                g2d.drawString("TOTAL   : Rp " + String.format("%,.0f", totalBelanja), x, y); y += 10;
                g2d.drawString("BAYAR   : Rp " + String.format("%,.0f", bayar), x, y); y += 10;
                g2d.drawString("KEMBALI : Rp " + String.format("%,.0f", kembalian), x, y); y += 10;
                g2d.drawString("-----------------------------", x, y); y += 10;
                g2d.drawString(" TERIMA KASIH TELAH BERBELANJA ", x, y); y += 10;
                g2d.drawString("   DI TOKO ALAM BUAH SEGAR    ", x, y);

                return PAGE_EXISTS;
            }
        }, pageFormat);

        boolean doPrint = printerJob.printDialog();
        if (doPrint) {
            try {
                printerJob.print();
            } catch (java.awt.print.PrinterException e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal mencetak struk: " + e.getMessage());
            }
        }
    }
    
    public void tambahKeKeranjang(String namaProduk, double harga) {
    try {
        // 1. Ambil stok, harga asli (harga_jual), dan harga_diskon langsung dari database
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        String sqlStok = "SELECT stok_di_rak, harga_jual, harga_diskon FROM produk WHERE nama_produk = ?"; 
        java.sql.PreparedStatement pstStok = conn.prepareStatement(sqlStok);
        pstStok.setString(1, namaProduk);
        java.sql.ResultSet rsStok = pstStok.executeQuery();
        
        int stokDatabase = 0;
        double hargaAsli = 0;   // Harga normal murni dari database (misal: 20000)
        double hargaDiskon = 0; // Diskon dari database (misal: 1000)
        
        if (rsStok.next()) {
            stokDatabase = rsStok.getInt("stok_di_rak");
            hargaAsli = rsStok.getDouble("harga_jual");     // Ambil harga asli dari DB
            hargaDiskon = rsStok.getDouble("harga_diskon"); // Ambil diskon dari DB
        }
        
        // 2. Cek jumlah (qty) produk yang sudah ada di keranjang belanja saat ini
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblKeranjang.getModel();
        int qtyDiKeranjang = 0;
        boolean ditemukan = false;
        int barisDitemukan = -1;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String namaTabel = model.getValueAt(i, 1).toString();
            if (namaTabel.equals(namaProduk)) {
                qtyDiKeranjang = Integer.parseInt(model.getValueAt(i, 2).toString());
                ditemukan = true;
                barisDitemukan = i;
                break;
            }
        }
        
        // 3. VALIDASI STOK
        if ((qtyDiKeranjang + 1) > stokDatabase) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Stok produk '" + namaProduk + "' tidak mencukupi!\nSisa stok di sistem saat ini: " + stokDatabase, 
                "Peringatan Stok", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 4. Hitung subtotal yang benar: (Harga Asli - Diskon) * Qty
        double hargaBersih = hargaAsli - hargaDiskon; // Contoh: 20000 - 1000 = 19000
        
        if (ditemukan) {
            int qtyBaru = qtyDiKeranjang + 1;
            int subtotalBaru = (int) (qtyBaru * hargaBersih);
            model.setValueAt(qtyBaru, barisDitemukan, 2);          // Update Qty
            model.setValueAt(subtotalBaru, barisDitemukan, 5);     // Update Subtotal (kolom 5)
        } else {
            int no = model.getRowCount() + 1;
            int subtotalBaru = (int) (1 * hargaBersih);
            model.addRow(new Object[]{
                no,                  // Kolom 0: NO
                namaProduk,          // Kolom 1: NAMA PRODUK
                1,                   // Kolom 2: QTY
                (int) hargaAsli,     // Kolom 3: HARGA PRODUK (Tetap utuh 20000, tidak ikut kepotong)
                (int) hargaDiskon,   // Kolom 4: DISKON (1000)
                (int) subtotalBaru   // Kolom 5: SUBTOTAL (19000)
            });
        }
        
        hitungTotalBelanja(); 
        
    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
    }
}
    
    public void tambahProdukDariDatabase(String namaProduk) {
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        
        // PERHATIKAN BAGIAN INI: Harus 'stok_di_rak', BUKAN 'stok'
        String sql = "SELECT harga_jual, harga_diskon, stok_di_rak FROM produk WHERE nama_produk = ?";
        java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, namaProduk);
        java.sql.ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            double hargaJual = rs.getDouble("harga_jual");
            double hargaDiskon = rs.getDouble("harga_diskon");
            int stokDatabase = rs.getInt("stok_di_rak"); // <--- Pastikan di sini juga stok_di_rak
            
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblKeranjang.getModel();
            int qtyDiKeranjang = 0;
            
            for (int i = 0; i < model.getRowCount(); i++) {
                String namaTabel = model.getValueAt(i, 1).toString();
                if (namaTabel.equals(namaProduk)) {
                    qtyDiKeranjang = Integer.parseInt(model.getValueAt(i, 2).toString());
                    break;
                }
            }
            
            if ((qtyDiKeranjang + 1) > stokDatabase) {
                javax.swing.JOptionPane.showMessageDialog(this, 
                    "Stok produk '" + namaProduk + "' di rak tidak mencukupi!\nSisa stok di rak saat ini: " + stokDatabase, 
                    "Peringatan Stok", 
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return; 
            }
            
            double hargaAkhir = hargaJual;
            if (hargaDiskon > 0) {
                hargaAkhir = hargaJual - hargaDiskon;
            }
            
            tambahKeKeranjang(namaProduk, hargaAkhir);
        }
    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal memproses produk: " + e.getMessage());
    }
}

    public void hitungTotalBelanja() {
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblKeranjang.getModel();
    int totalItem = 0;
    double totalHarga = 0;
    
    for (int i = 0; i < model.getRowCount(); i++) {
        int qty = Integer.parseInt(model.getValueAt(i, 2).toString());
        
        // UBAH ANGKA 4 JADI 5 (karena Subtotal ada di kolom indeks ke-5)
        double subtotal = Double.parseDouble(model.getValueAt(i, 5).toString());
        
        totalItem += qty;
        totalHarga += subtotal;
    }
    
    txtTotalItem.setText(String.valueOf(totalItem));
    txtTotalHarga.setText("Rp " + (int) totalHarga);
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
        jButton4 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabelTanggal = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabelUser = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlIsiBuah = new javax.swing.JPanel();
        panelApel = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        panelMelon = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        panelPir = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        panelMatoa = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        panelPisang = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        panelManggis = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        panelAnggur = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        panelSemangka = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        panelPepaya = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jButton25 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jButton26 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        panelLeci = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jButton20 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        panelBuahNaga = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        panelManggaHarum = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jButton21 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jButton22 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        panelDurian = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        panelNanas = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        panelJeruk = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKeranjang = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnBatal = new javax.swing.JButton();
        btnBayar = new javax.swing.JButton();
        txtTotalItem = new javax.swing.JTextField();
        txtBayar = new javax.swing.JTextField();
        txtKembalian = new javax.swing.JTextField();
        txtTotalHarga = new javax.swing.JTextField();
        txtCari = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1200, 750));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(55, 85, 52));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton4.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Dashboard");
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setFocusPainted(false);
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 190, -1));

        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icontransaksi2-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 80, 40));

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setFont(new java.awt.Font("Alte Haas Grotesk", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(3, 159, 25));
        jButton1.setText("Transaksi");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 230, 40));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 270, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(101, 270, -1, -1));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTanggal.setText("Rabu 30 Sep 2026");
        jPanel5.add(jLabelTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 20));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 630, 118, 22));

        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 270, 120, -1));

        jLabelUser.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jLabelUser.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUser.setText("User Admin(Admin@gmail.com)");
        jPanel1.add(jLabelUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 650, 150, 31));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/Logo_ABS removebg.png"))); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 160, 110));

        jLabel11.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("MENU UTAMA");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, 15));

        jLabel12.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("DATA MASTER");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 15));

        jLabel53.setFont(new java.awt.Font("Alte Haas Grotesk", 1, 10)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("AKUN");
        jPanel1.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, 40, 20));

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
        jPanel1.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 90, 40));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/icondash-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 60, 50));

        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/iconkeluar-removebg-preview.png"))); // NOI18N
        jPanel1.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 50, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 750));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panelApel.setBackground(new java.awt.Color(255, 255, 255));
        panelApel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 102, 102));
        jLabel27.setText("Rp 38.000");
        panelApel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 70, -1));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(3, 159, 25));
        jLabel28.setText("Apel ");
        panelApel.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 40, 30));

        jButton10.setBackground(new java.awt.Color(3, 159, 25));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("+Tambah");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        panelApel.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto apel.jpeg"))); // NOI18N
        panelApel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 80));

        panelMelon.setBackground(new java.awt.Color(255, 255, 255));
        panelMelon.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setText("Rp 30.000");
        panelMelon.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(3, 159, 25));
        jLabel24.setText("Melon");
        panelMelon.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 50, 30));

        jButton8.setBackground(new java.awt.Color(3, 159, 25));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("+Tambah");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        panelMelon.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto melon.jpeg"))); // NOI18N
        panelMelon.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        panelPir.setBackground(new java.awt.Color(255, 255, 255));
        panelPir.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(102, 102, 102));
        jLabel39.setText("Rp 28.000");
        panelPir.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(3, 159, 25));
        jLabel40.setText("Pir");
        panelPir.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, 30));

        jButton16.setBackground(new java.awt.Color(3, 159, 25));
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("+Tambah");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        panelPir.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 110, 110, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto pir.jpeg"))); // NOI18N
        panelPir.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 80));

        panelMatoa.setBackground(new java.awt.Color(255, 255, 255));
        panelMatoa.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setText("Rp 58.000");
        panelMatoa.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(3, 159, 25));
        jLabel38.setText("Matoa");
        panelMatoa.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 30));

        jButton15.setBackground(new java.awt.Color(3, 159, 25));
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("+Tambah");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        panelMatoa.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 110, 110, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto matoa.jpeg"))); // NOI18N
        panelMatoa.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 80));

        panelPisang.setBackground(new java.awt.Color(255, 255, 255));
        panelPisang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(102, 102, 102));
        jLabel33.setText("Rp 22.000");
        panelPisang.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(3, 159, 25));
        jLabel34.setText("Pisang");
        panelPisang.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 30));

        jButton13.setBackground(new java.awt.Color(3, 159, 25));
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("+Tambah");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        panelPisang.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 110, 110, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto pisang.jpeg"))); // NOI18N
        panelPisang.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        panelManggis.setBackground(new java.awt.Color(255, 255, 255));
        panelManggis.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(102, 102, 102));
        jLabel45.setText("Rp 34.000");
        panelManggis.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(3, 159, 25));
        jLabel46.setText("Manggis");
        panelManggis.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 30));

        jButton19.setBackground(new java.awt.Color(3, 159, 25));
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setText("+Tambah");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        panelManggis.add(jButton19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto manggis.jpeg"))); // NOI18N
        panelManggis.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        panelAnggur.setBackground(new java.awt.Color(255, 255, 255));
        panelAnggur.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(102, 102, 102));
        jLabel31.setText("Rp 90.000");
        panelAnggur.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(3, 159, 25));
        jLabel32.setText(" Anggur Muscat");
        panelAnggur.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, -1, 30));

        jButton12.setBackground(new java.awt.Color(3, 159, 25));
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("+Tambah");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        panelAnggur.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 110, 110, -1));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto anggur muscat.jpeg"))); // NOI18N
        panelAnggur.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 80));

        panelSemangka.setBackground(new java.awt.Color(255, 255, 255));
        panelSemangka.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Rp 20.000");
        panelSemangka.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(3, 159, 25));
        jLabel22.setText("Semangka");
        panelSemangka.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 30));

        jButton7.setBackground(new java.awt.Color(3, 159, 25));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("+Tambah");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        panelSemangka.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto semangka.jpeg"))); // NOI18N
        panelSemangka.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        panelPepaya.setBackground(new java.awt.Color(255, 255, 255));
        panelPepaya.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(102, 102, 102));
        jLabel57.setText("Rp 12.000");
        panelPepaya.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 20));

        jLabel58.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(3, 159, 25));
        jLabel58.setText("Pepaya");
        panelPepaya.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 30));

        jButton25.setBackground(new java.awt.Color(3, 159, 25));
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.setText("+Tambah");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });
        panelPepaya.add(jButton25, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton26.setBackground(new java.awt.Color(3, 159, 25));
        jButton26.setForeground(new java.awt.Color(255, 255, 255));
        jButton26.setText("+Tambah");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel27.add(jButton26, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 64, -1, -1));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 750, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 1000, 750));

        panelPepaya.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 460, 110, 100));

        jLabel59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto pepaya.jpeg"))); // NOI18N
        panelPepaya.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        panelLeci.setBackground(new java.awt.Color(255, 255, 255));
        panelLeci.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(102, 102, 102));
        jLabel41.setText("Rp 67.000");
        panelLeci.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(3, 159, 25));
        jLabel42.setText("Leci");
        panelLeci.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, -1, 30));

        jButton17.setBackground(new java.awt.Color(3, 159, 25));
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("+Tambah");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        panelLeci.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 110, 110, -1));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(102, 102, 102));
        jLabel47.setText("Rp 15.000");
        jPanel21.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(3, 159, 25));
        jLabel48.setText("Manggis");
        jPanel21.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jButton20.setBackground(new java.awt.Color(3, 159, 25));
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setText("+Tambah");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 64, -1, -1));

        panelLeci.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 460, 110, 100));

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto leci.jpeg"))); // NOI18N
        panelLeci.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 80));

        panelBuahNaga.setBackground(new java.awt.Color(255, 255, 255));
        panelBuahNaga.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(102, 102, 102));
        jLabel29.setText("Rp 32.000");
        panelBuahNaga.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, 20));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(3, 159, 25));
        jLabel30.setText("Buah Naga");
        panelBuahNaga.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 30));

        jButton11.setBackground(new java.awt.Color(3, 159, 25));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("+Tambah");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        panelBuahNaga.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto buah naga.jpeg"))); // NOI18N
        panelBuahNaga.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(-7, -4, 120, 80));

        panelManggaHarum.setBackground(new java.awt.Color(255, 255, 255));
        panelManggaHarum.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(102, 102, 102));
        jLabel49.setText("Rp 30.000");
        panelManggaHarum.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(3, 159, 25));
        jLabel50.setText("Mangga Harum");
        panelManggaHarum.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 110, 30));

        jButton21.setBackground(new java.awt.Color(3, 159, 25));
        jButton21.setForeground(new java.awt.Color(255, 255, 255));
        jButton21.setText("+Tambah");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        panelManggaHarum.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(102, 102, 102));
        jLabel51.setText("Rp 15.000");
        jPanel23.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(3, 159, 25));
        jLabel52.setText("Manggis");
        jPanel23.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jButton22.setBackground(new java.awt.Color(3, 159, 25));
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setText("+Tambah");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 64, -1, -1));

        panelManggaHarum.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 460, 110, 100));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto mangga harum manis.jpeg"))); // NOI18N
        panelManggaHarum.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, -4, -1, 80));

        panelDurian.setBackground(new java.awt.Color(255, 255, 255));
        panelDurian.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(102, 102, 102));
        jLabel35.setText("Rp 75.000");
        panelDurian.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(3, 159, 25));
        jLabel36.setText("Durian");
        panelDurian.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 70, 50, 30));

        jButton14.setBackground(new java.awt.Color(3, 159, 25));
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("+Tambah");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        panelDurian.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto durian.jpeg"))); // NOI18N
        panelDurian.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 120, 80));

        panelNanas.setBackground(new java.awt.Color(255, 255, 255));
        panelNanas.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(102, 102, 102));
        jLabel43.setText("Rp 15.000");
        panelNanas.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(3, 159, 25));
        jLabel44.setText("Nanas");
        panelNanas.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 30));

        jButton18.setBackground(new java.awt.Color(3, 159, 25));
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setText("+Tambah");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        panelNanas.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 120, -1));

        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto nanas.jpeg"))); // NOI18N
        panelNanas.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 80));

        panelJeruk.setBackground(new java.awt.Color(255, 255, 255));
        panelJeruk.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Rp 20.000");
        panelJeruk.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 80, 20));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(3, 159, 25));
        jLabel26.setText("Jeruk Medan");
        panelJeruk.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 90, 30));

        jButton9.setBackground(new java.awt.Color(3, 159, 25));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("+Tambah");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        panelJeruk.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 110, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gambar/foto jeruk medan.jpeg"))); // NOI18N
        panelJeruk.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 120, 100));

        javax.swing.GroupLayout pnlIsiBuahLayout = new javax.swing.GroupLayout(pnlIsiBuah);
        pnlIsiBuah.setLayout(pnlIsiBuahLayout);
        pnlIsiBuahLayout.setHorizontalGroup(
            pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIsiBuahLayout.createSequentialGroup()
                .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlIsiBuahLayout.createSequentialGroup()
                        .addComponent(panelApel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(panelJeruk, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panelMelon, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlIsiBuahLayout.createSequentialGroup()
                        .addComponent(panelPir, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(panelMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(panelPisang, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlIsiBuahLayout.createSequentialGroup()
                        .addComponent(panelManggis, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(panelAnggur, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(panelSemangka, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlIsiBuahLayout.createSequentialGroup()
                        .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelManggaHarum, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelPepaya, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelLeci, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelBuahNaga, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelDurian, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelNanas, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(23, 23, 23))
        );
        pnlIsiBuahLayout.setVerticalGroup(
            pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlIsiBuahLayout.createSequentialGroup()
                .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelApel, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelJeruk, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelMelon, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelPir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelPisang, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSemangka, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelManggis, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelAnggur, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelLeci, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelManggaHarum, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelDurian, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlIsiBuahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelPepaya, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelBuahNaga, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelNanas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(107, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(pnlIsiBuah);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 410, 610));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 51));
        jLabel20.setText("Keranjang Belanja");

        tblKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "NO", "NAMA PRODUK", "QYT", "HARGA", "SUBTOTAL"
            }
        ));
        jScrollPane1.setViewportView(tblKeranjang);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Total Item:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Total Harga:");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Bayar:");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("kembalian:");

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnBayar.setBackground(new java.awt.Color(3, 159, 25));
        btnBayar.setForeground(new java.awt.Color(255, 255, 255));
        btnBayar.setText("Bayar");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });
        btnBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                btnBayarKeyReleased(evt);
            }
        });

        txtTotalItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalItemActionPerformed(evt);
            }
        });

        txtTotalHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalHargaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel20))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(txtTotalItem, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel20)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtTotalItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBatal)
                        .addComponent(btnBayar)))
                .addGap(40, 40, 40))
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 20, -1, 650));

        txtCari.setForeground(new java.awt.Color(204, 204, 204));
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
        getContentPane().add(txtCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(242, 20, 396, 30));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String role = com.alambuahsegar.config.Session.role;
    
    if (role != null && role.equalsIgnoreCase("Admin")) {
        new FormDashboardAdmin().setVisible(true);
    } 
    else if (role != null && role.equalsIgnoreCase("Kasir")) {
        new FormDashboardKasir().setVisible(true); // Ini khusus untuk Kasir, kembali ke Dashboard Kasir!
    } 
    else if (role != null && role.equalsIgnoreCase("Manager")) {
        new FormDashboardManager().setVisible(true);
    } 
    else {
        new FormLogin().setVisible(true);
    }
    
    this.dispose(); // Menutup Form Transaksi
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        try {
        // 1. Kosongkan baris di tabel keranjang belanja
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tblKeranjang.getModel();
        model.setRowCount(0);

        // 2. Reset teks total item, total harga, bayar, dan kembalian ke awal
        txtTotalItem.setText("0");
        txtTotalHarga.setText("Rp 0");
        txtBayar.setText("");
        txtKembalian.setText("Rp 0");

        // Opsional: Berikan konfirmasi lembut atau biarkan langsung bersih
        // javax.swing.JOptionPane.showMessageDialog(this, "Transaksi dibatalkan, keranjang dikosongkan.");

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
    }
    }//GEN-LAST:event_btnBatalActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed
    try {
        java.sql.Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
        
        // Ambil model keranjang
        javax.swing.table.DefaultTableModel modelKeranjang = (javax.swing.table.DefaultTableModel) tblKeranjang.getModel(); 
        if (modelKeranjang.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Keranjang belanja masih kosong!");
            return;
        }

        // VALIDASI: Cek apakah kolom bayar kosong agar tidak error empty String
        if (txtBayar.getText().trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Nominal uang pembayaran belum diisi!", "Peringatan", javax.swing.JOptionPane.WARNING_MESSAGE);
            txtBayar.requestFocus();
            return;
        }

        // Validasi: Cek apakah stok di rak mencukupi sebelum transaksi diproses
        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            String namaProduk = modelKeranjang.getValueAt(i, 1).toString();
            int qyt = Integer.parseInt(modelKeranjang.getValueAt(i, 2).toString());

            String sqlCekStok = "SELECT stok_di_rak FROM produk WHERE nama_produk = ?";
            java.sql.PreparedStatement pstCek = conn.prepareStatement(sqlCekStok);
            pstCek.setString(1, namaProduk);
            java.sql.ResultSet rsCek = pstCek.executeQuery();
            
            if (rsCek.next()) {
                int stokRak = rsCek.getInt("stok_di_rak");
                int stokTampil = Math.max(0, stokRak); 
                
                if (stokRak < qyt) {
                    javax.swing.JOptionPane.showMessageDialog(this, 
                        "Transaksi Gagal! Stok untuk produk '" + namaProduk + "' di rak tidak mencukupi.\nSisa stok di rak saat ini: " + stokTampil, 
                        "Peringatan Stok", 
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                    return; // Batalkan proses transaksi
                }
            }
        }

        // 1. Buat Nomor Struk otomatis
        String noStruk = "TRX-" + System.currentTimeMillis();
        
        // Ambil total belanja dan uang bayar, lalu hitung kembalian
        double totalBelanja = Double.parseDouble(txtTotalHarga.getText().replaceAll("[^0-9]", ""));
        double uangBayar = Double.parseDouble(txtBayar.getText().replaceAll("[^0-9]", ""));
        double kembalian = uangBayar - totalBelanja; 
        
        // AMBIL ID USER YANG SEDANG LOGIN SECARA DINAMIS
        int idUser = com.alambuahsegar.config.Session.id_pengguna;

        // 2. Simpan ke tabel utama 'transaksi' (Menyertakan uang_bayar dan kembalian)
        String sqlTrx = "INSERT INTO transaksi (no_struk, tanggal_transaksi, total_belanja, id_pengguna, uang_bayar, kembalian) VALUES (?, NOW(), ?, ?, ?, ?)";
        java.sql.PreparedStatement pstTrx = conn.prepareStatement(sqlTrx, java.sql.Statement.RETURN_GENERATED_KEYS);
        pstTrx.setString(1, noStruk);
        pstTrx.setDouble(2, totalBelanja);
        pstTrx.setInt(3, idUser);
        pstTrx.setDouble(4, uangBayar);
        pstTrx.setDouble(5, kembalian);
        pstTrx.executeUpdate();

        // Ambil ID transaksi yang baru saja dibuat
        java.sql.ResultSet rsKey = pstTrx.getGeneratedKeys();
        int idTrxBaru = 0;
        if (rsKey.next()) {
            idTrxBaru = rsKey.getInt(1);
        }

        // 3. Simpan setiap item barang ke tabel 'detail_transaksi' & KURANGI STOK RAK & STOK SISTEM
        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            String namaProduk = modelKeranjang.getValueAt(i, 1).toString(); // Nama produk ada di kolom 1
            int qyt = Integer.parseInt(modelKeranjang.getValueAt(i, 2).toString()); // QTY (Jumlah) di kolom 2
            double hargaSatuan = Double.parseDouble(modelKeranjang.getValueAt(i, 3).toString().replaceAll("[^0-9]", "")); // Harga satuan di kolom 3
            double subtotal = Double.parseDouble(modelKeranjang.getValueAt(i, 5).toString().replaceAll("[^0-9]", "")); 

            // Cari id_produk otomatis berdasarkan nama produk di database
            String sqlCariId = "SELECT id_produk FROM produk WHERE nama_produk = ?";
            java.sql.PreparedStatement pstCari = conn.prepareStatement(sqlCariId);
            pstCari.setString(1, namaProduk);
            java.sql.ResultSet rsCari = pstCari.executeQuery();
            
            String idProduk = "1"; // Default jika tidak ketemu
            if (rsCari.next()) {
                idProduk = rsCari.getString("id_produk");
            }

            // Simpan ke detail_transaksi
            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, harga_satuan, qyt, subtotal) VALUES (?, ?, ?, ?, ?)";
            java.sql.PreparedStatement pstDetail = conn.prepareStatement(sqlDetail);
            pstDetail.setInt(1, idTrxBaru);
            pstDetail.setString(2, idProduk);
            pstDetail.setDouble(3, hargaSatuan);
            pstDetail.setInt(4, qyt);
            pstDetail.setDouble(5, subtotal);
            pstDetail.executeUpdate();

            // Kurangi stok_di_rak DAN total_stok_sistem secara bersamaan
            String sqlUpdateStok = "UPDATE produk SET stok_di_rak = stok_di_rak - ?, total_stok_sistem = total_stok_sistem - ? WHERE id_produk = ?";
            java.sql.PreparedStatement pstStok = conn.prepareStatement(sqlUpdateStok);
            pstStok.setInt(1, qyt);
            pstStok.setInt(2, qyt);
            pstStok.setString(3, idProduk);
            pstStok.executeUpdate();
        }

        // 4. Catat otomatis ke tabel 'riwayat_aktivitas' (id_pengguna sekarang terisi dinamis!)
        String sqlLog = "INSERT INTO riwayat_aktivitas (id_pengguna, waktu_aktivitas, deskripsi_aktivitas) VALUES (?, NOW(), ?)";
        java.sql.PreparedStatement pstLog = conn.prepareStatement(sqlLog);
        pstLog.setInt(1, idUser);
        pstLog.setString(2, "Melakukan transaksi penjualan baru dengan nomor struk: " + noStruk);
        pstLog.executeUpdate();

        // 5. TAMPILKAN KEMBALIAN DAN PESAN SUKSES DULUAN (SESUAI LOGIKA KASIR)
        txtKembalian.setText(String.format("%,.0f", kembalian));
        javax.swing.JOptionPane.showMessageDialog(this, "Transaksi Berhasil Disimpan!\nUang Kembalian: Rp " + String.format("%,.0f", kembalian));
        
        // 6. BARU SETELAH DI-KLIK OK, PROSES CETAK STRUK BERJALAN
        String tanggalTrx = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String namaKasir = jLabelUser.getText(); 
        cetakStrukKasir(noStruk, tanggalTrx, namaKasir, modelKeranjang, totalBelanja, uangBayar, kembalian);

        // Kosongkan keranjang dan field setelah semuanya selesai
        modelKeranjang.setRowCount(0);
        txtTotalHarga.setText("0");
        txtBayar.setText("");
        txtKembalian.setText("");

    } catch (Exception e) {
        e.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
    }
    }//GEN-LAST:event_btnBayarActionPerformed

    private void txtTotalItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalItemActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
                                      
    tambahProdukDariDatabase("Apel"); 
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
       tambahProdukDariDatabase("Jeruk Medan");
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        tambahProdukDariDatabase("Semangka");
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        tambahProdukDariDatabase("Pisang");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        tambahProdukDariDatabase("Anggur Muscat");
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        tambahProdukDariDatabase("Melon");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        tambahProdukDariDatabase("Durian");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        tambahProdukDariDatabase("Matoa");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        tambahProdukDariDatabase("Buah Naga");
    }//GEN-LAST:event_jButton11ActionPerformed

    private void btnBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBayarKeyReleased
                                   
    try {
        String totalStr = txtTotalHarga.getText().replace("Rp ", "").replace(",", "").trim();
        if (totalStr.isEmpty()) return;
        
        double totalBelanja = Double.parseDouble(totalStr);
        
        if (txtBayar.getText().trim().isEmpty()) {
            txtKembalian.setText("Rp 0");
            return;
        }
        
        double uangBayar = Double.parseDouble(txtBayar.getText().trim());
        double kembalian = uangBayar - totalBelanja;
        
        if (kembalian >= 0) {
            txtKembalian.setText("Rp " + kembalian);
        } else {
            txtKembalian.setText("Uang Kurang!");
        }
    } catch (Exception e) {
        txtKembalian.setText("Rp 0");
    }
    }//GEN-LAST:event_btnBayarKeyReleased

    private void txtTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalHargaActionPerformed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        new FormDashboardAdmin().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        tambahProdukDariDatabase("Pir");
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        tambahProdukDariDatabase("Leci");
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        tambahProdukDariDatabase("Nanas");
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        tambahProdukDariDatabase("Manggis");
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        tambahProdukDariDatabase("Mangga Harum");
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        tambahProdukDariDatabase("Pepaya");
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton26ActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
    String keyword = txtCari.getText().trim().toLowerCase();

    // Jika kotak pencarian kosong, tampilkan semua buah kembali
    if (keyword.isEmpty() || keyword.equals("cari buah...") || keyword.equals("cari produk...")) {
        panelApel.setVisible(true);
        panelJeruk.setVisible(true);
        panelMelon.setVisible(true);
        panelPir.setVisible(true);
        panelMatoa.setVisible(true);
        panelPisang.setVisible(true);
        panelManggis.setVisible(true);
        panelAnggur.setVisible(true);
        panelSemangka.setVisible(true);
        panelPepaya.setVisible(true);
        panelBuahNaga.setVisible(true);
        panelLeci.setVisible(true);
        panelManggaHarum.setVisible(true);
        panelDurian.setVisible(true);
        panelNanas.setVisible(true);
    } else {
        // Filter pencarian otomatis merapat ke pojok kiri atas berkat FlowLayout
        panelApel.setVisible("apel".contains(keyword));
        panelJeruk.setVisible("jeruk medan".contains(keyword));
        panelMelon.setVisible("melon".contains(keyword));
        panelPir.setVisible("pir".contains(keyword));
        panelMatoa.setVisible("matoa".contains(keyword));
        panelPisang.setVisible("pisang".contains(keyword));
        panelManggis.setVisible("manggis".contains(keyword));
        panelAnggur.setVisible("anggur muscat".contains(keyword));
        panelSemangka.setVisible("semangka".contains(keyword));
        panelPepaya.setVisible("pepaya".contains(keyword));
        panelBuahNaga.setVisible("buah naga".contains(keyword));
        panelLeci.setVisible("leci".contains(keyword));
        panelManggaHarum.setVisible("mangga harum".contains(keyword));
        panelDurian.setVisible("durian".contains(keyword));
        panelNanas.setVisible("nanas".contains(keyword));
    }

    // Wajib ada agar FlowLayout memperbarui posisi secara instan
    pnlIsiBuah.revalidate();
    pnlIsiBuah.repaint();
    }//GEN-LAST:event_txtCariKeyReleased

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        new FormLogin().setVisible(true); // Kembali ke halaman login
        this.dispose();
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void txtCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCariActionPerformed
    String keyword = txtCari.getText().trim().toLowerCase();

    // 1. Jika kotak pencarian kosong / placeholder, kembalikan semua buah ke posisi awal desain
    if (keyword.isEmpty() || keyword.equals("cari buah...") || keyword.equals("cari produk...")) {
        panelApel.setVisible(true); panelApel.setBounds(15, 15, panelApel.getWidth(), panelApel.getHeight());
        panelJeruk.setVisible(true); panelJeruk.setBounds(180, 15, panelJeruk.getWidth(), panelJeruk.getHeight());
        panelMelon.setVisible(true); panelMelon.setBounds(345, 15, panelMelon.getWidth(), panelMelon.getHeight());
        panelPir.setVisible(true); panelPir.setBounds(15, 230, panelPir.getWidth(), panelPir.getHeight());
        panelMatoa.setVisible(true); panelMatoa.setBounds(180, 230, panelMatoa.getWidth(), panelMatoa.getHeight());
        panelPisang.setVisible(true); panelPisang.setBounds(345, 230, panelPisang.getWidth(), panelPisang.getHeight());
        panelManggis.setVisible(true); panelManggis.setBounds(15, 455, panelManggis.getWidth(), panelManggis.getHeight());
        panelAnggur.setVisible(true); panelAnggur.setBounds(180, 455, panelAnggur.getWidth(), panelAnggur.getHeight());
        panelSemangka.setVisible(true); panelSemangka.setBounds(345, 455, panelSemangka.getWidth(), panelSemangka.getHeight());
        panelPepaya.setVisible(true); panelPepaya.setBounds(15, 680, panelPepaya.getWidth(), panelPepaya.getHeight());
        panelBuahNaga.setVisible(true); panelBuahNaga.setBounds(180, 680, panelBuahNaga.getWidth(), panelBuahNaga.getHeight());
        panelLeci.setVisible(true); panelLeci.setBounds(345, 680, panelLeci.getWidth(), panelLeci.getHeight());
        panelManggaHarum.setVisible(true); panelManggaHarum.setBounds(15, 905, panelManggaHarum.getWidth(), panelManggaHarum.getHeight());
        panelDurian.setVisible(true); panelDurian.setBounds(180, 905, panelDurian.getWidth(), panelDurian.getHeight());
        panelNanas.setVisible(true); panelNanas.setBounds(345, 905, panelNanas.getWidth(), panelNanas.getHeight());
        
        pnlIsiBuah.revalidate();
        pnlIsiBuah.repaint();
        return;
    }

    // 2. Filter pencarian: jika ketemu, WAJIB pindah ke pojok kiri atas (15, 15)
    boolean adaApel = "apel".contains(keyword);
    panelApel.setVisible(adaApel); if(adaApel) panelApel.setBounds(15, 15, panelApel.getWidth(), panelApel.getHeight());

    boolean adaJeruk = "jeruk medan".contains(keyword);
    panelJeruk.setVisible(adaJeruk); if(adaJeruk) panelJeruk.setBounds(15, 15, panelJeruk.getWidth(), panelJeruk.getHeight());

    boolean adaMelon = "melon".contains(keyword);
    panelMelon.setVisible(adaMelon); if(adaMelon) panelMelon.setBounds(15, 15, panelMelon.getWidth(), panelMelon.getHeight());

    boolean adaPir = "pir".contains(keyword);
    panelPir.setVisible(adaPir); if(adaPir) panelPir.setBounds(15, 15, panelPir.getWidth(), panelPir.getHeight());

    boolean adaMatoa = "matoa".contains(keyword);
    panelMatoa.setVisible(adaMatoa); if(adaMatoa) panelMatoa.setBounds(15, 15, panelMatoa.getWidth(), panelMatoa.getHeight());

    boolean adaPisang = "pisang".contains(keyword);
    panelPisang.setVisible(adaPisang); if(adaPisang) panelPisang.setBounds(15, 15, panelPisang.getWidth(), panelPisang.getHeight());

    boolean adaManggis = "manggis".contains(keyword);
    panelManggis.setVisible(adaManggis); if(adaManggis) panelManggis.setBounds(15, 15, panelManggis.getWidth(), panelManggis.getHeight());

    boolean adaAnggur = "anggur muscat".contains(keyword);
    panelAnggur.setVisible(adaAnggur); if(adaAnggur) panelAnggur.setBounds(15, 15, panelAnggur.getWidth(), panelAnggur.getHeight());

    boolean adaSemangka = "semangka".contains(keyword);
    panelSemangka.setVisible(adaSemangka); if(adaSemangka) panelSemangka.setBounds(15, 15, panelSemangka.getWidth(), panelSemangka.getHeight());

    boolean adaPepaya = "pepaya".contains(keyword);
    panelPepaya.setVisible(adaPepaya); if(adaPepaya) panelPepaya.setBounds(15, 15, panelPepaya.getWidth(), panelPepaya.getHeight());

    boolean adaBuahNaga = "buah naga".contains(keyword);
    panelBuahNaga.setVisible(adaBuahNaga); if(adaBuahNaga) panelBuahNaga.setBounds(15, 15, panelBuahNaga.getWidth(), panelBuahNaga.getHeight());

    boolean adaLeci = "leci".contains(keyword);
    panelLeci.setVisible(adaLeci); if(adaLeci) panelLeci.setBounds(15, 15, panelLeci.getWidth(), panelLeci.getHeight());

    boolean adaMangga = "mangga harum".contains(keyword);
    panelManggaHarum.setVisible(adaMangga); if(adaMangga) panelManggaHarum.setBounds(15, 15, panelManggaHarum.getWidth(), panelManggaHarum.getHeight());

    boolean adaDurian = "durian".contains(keyword);
    panelDurian.setVisible(adaDurian); if(adaDurian) panelDurian.setBounds(15, 15, panelDurian.getWidth(), panelDurian.getHeight());

    boolean adaNanas = "nanas".contains(keyword);
    panelNanas.setVisible(adaNanas); if(adaNanas) panelNanas.setBounds(15, 15, panelNanas.getWidth(), panelNanas.getHeight());

    pnlIsiBuah.revalidate();
    pnlIsiBuah.repaint();
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
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelTanggal;
    private javax.swing.JLabel jLabelUser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelAnggur;
    private javax.swing.JPanel panelApel;
    private javax.swing.JPanel panelBuahNaga;
    private javax.swing.JPanel panelDurian;
    private javax.swing.JPanel panelJeruk;
    private javax.swing.JPanel panelLeci;
    private javax.swing.JPanel panelManggaHarum;
    private javax.swing.JPanel panelManggis;
    private javax.swing.JPanel panelMatoa;
    private javax.swing.JPanel panelMelon;
    private javax.swing.JPanel panelNanas;
    private javax.swing.JPanel panelPepaya;
    private javax.swing.JPanel panelPir;
    private javax.swing.JPanel panelPisang;
    private javax.swing.JPanel panelSemangka;
    private javax.swing.JPanel pnlIsiBuah;
    private javax.swing.JTable tblKeranjang;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtTotalHarga;
    private javax.swing.JTextField txtTotalItem;
    // End of variables declaration//GEN-END:variables
}


