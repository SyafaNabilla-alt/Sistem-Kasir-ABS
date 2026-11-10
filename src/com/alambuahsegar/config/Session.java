package com.alambuahsegar.config;

public class Session {
    public static int id_pengguna; 
    public static String username;
    public static String role;

    public static void setSidebarInfo(javax.swing.JLabel lblTgl, javax.swing.JLabel lblUsr) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMM yyyy", new java.util.Locale("id", "ID"));
        lblTgl.setText(sdf.format(new java.util.Date()));

        try {
            lblUsr.setText(username + " (" + role + ")");
        } catch (Exception e) {
            lblUsr.setText("User Aktif");
        }
    }
}