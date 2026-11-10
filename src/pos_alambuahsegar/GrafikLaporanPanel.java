package pos_alambuahsegar;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GrafikLaporanPanel extends JPanel {

    private static class DataGrafik {
        String tanggal;
        double pendapatan;
        String buahTerlaris;

        public DataGrafik(String tanggal, double pendapatan, String buahTerlaris) {
            this.tanggal = tanggal;
            this.pendapatan = pendapatan;
            this.buahTerlaris = (buahTerlaris != null && !buahTerlaris.isEmpty()) ? buahTerlaris : "-";
        }
    }

    private List<DataGrafik> dataList = new ArrayList<>();

    public GrafikLaporanPanel() {
        setBackground(Color.WHITE);
        loadDataGrafik();
    }

    public void loadDataGrafik() {
        dataList.clear();
        try {
            Connection conn = com.alambuahsegar.config.DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();

            // Menggunakan DESC LIMIT 7 agar mengambil 7 hari terbaru (termasuk hari ini)
            String sql = "SELECT DATE(t.tanggal_transaksi) AS tanggal, SUM(t.total_belanja) AS total_pendapatan, " +
                         "(SELECT p.nama_produk FROM detail_transaksi dt " +
                         " JOIN produk p ON dt.id_produk = p.id_produk " +
                         " JOIN transaksi tr ON dt.id_transaksi = tr.id_transaksi " +
                         " WHERE DATE(tr.tanggal_transaksi) = DATE(t.tanggal_transaksi) " +
                         " GROUP BY p.id_produk ORDER BY SUM(dt.qyt) DESC LIMIT 1) AS produk_terlaris " +
                         "FROM transaksi t " +
                         "GROUP BY DATE(t.tanggal_transaksi) " +
                         "ORDER BY tanggal DESC LIMIT 7";

            ResultSet rs = stmt.executeQuery(sql);
            
            List<DataGrafik> tempList = new ArrayList<>();
            while (rs.next()) {
                String tgl = rs.getString("tanggal");
                double omset = rs.getDouble("total_pendapatan");
                String buah = rs.getString("produk_terlaris");

                tempList.add(new DataGrafik(tgl, omset, buah));
            }

            // Dibalik agar urutan grafiknya dari tanggal lama ke tanggal terbaru (kiri ke kanan)
            for (int i = tempList.size() - 1; i >= 0; i--) {
                dataList.add(tempList.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dataList.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("Belum ada data grafik transaksi...", 20, 50);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int labelPadding = 40;

        double maxOmset = 100000; 
        for (DataGrafik d : dataList) {
            if (d.pendapatan > maxOmset) maxOmset = d.pendapatan;
        }

        int usableWidth = width - (2 * padding);
        int usableHeight = height - (2 * padding) - labelPadding;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(padding, height - padding - labelPadding, width - padding, height - padding - labelPadding);

        int n = dataList.size();
        int xInterval = n > 1 ? usableWidth / (n - 1) : usableWidth;

        int[] xPoints = new int[n];
        int[] yPoints = new int[n];

        for (int i = 0; i < n; i++) {
            DataGrafik d = dataList.get(i);
            int x = padding + (i * xInterval);
            int y = (int) (height - padding - labelPadding - ((d.pendapatan / maxOmset) * usableHeight));

            xPoints[i] = x;
            yPoints[i] = y;
        }

        g2d.setColor(new Color(40, 140, 70));
        g2d.setStroke(new BasicStroke(2f));
        for (int i = 0; i < n - 1; i++) {
            g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
        }

        for (int i = 0; i < n; i++) {
            DataGrafik d = dataList.get(i);
            int x = xPoints[i];
            int y = yPoints[i];

            g2d.setColor(Color.WHITE);
            g2d.fillOval(x - 5, y - 5, 10, 10);
            g2d.setColor(new Color(40, 140, 70));
            g2d.drawOval(x - 5, y - 5, 10, 10);

            int textXOffset = -25;
            if (i == n - 1 && n > 1) { 
                textXOffset = -90; 
            }

            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            String omsetStr = String.format("Rp %,.0f", d.pendapatan).replace(',', '.');
            g2d.drawString(omsetStr, x + textXOffset, y - 22);

            g2d.setFont(new Font("Arial", Font.ITALIC, 9));
            g2d.setColor(new Color(180, 50, 50));
            g2d.drawString("Terlaris: " + d.buahTerlaris, x + textXOffset, y - 10);

            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            g2d.drawString(d.tanggal, x - 15, height - padding - labelPadding + 20);
        }
    }
}