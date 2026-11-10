package com.alambuahsegar.model;

public class Product {
    private int idProduk;
    private String barcode;
    private String namaProduk;
    private double hargaJual;
    private int stokDiRak;
    private String gambar;

    // Constructor
    public Product(int idProduk, String barcode, String namaProduk, double hargaJual, int stokDiRak, String gambar) {
        this.idProduk = idProduk;
        this.barcode = barcode;
        this.namaProduk = namaProduk;
        this.hargaJual = hargaJual;
        this.stokDiRak = stokDiRak;
        this.gambar = gambar;
    }

    // Getter dan Setter
    public int getIdProduk() { 
        return idProduk; 
    }
    public String getBarcode() { 
        return barcode; 
    }
    public String getNamaProduk() { 
        return namaProduk; 
    }
    public double getHargaJual() { 
        return hargaJual; 
    }
    public int getStokDiRak() { 
        return stokDiRak; 
    }
    public String getGambar() { 
        return gambar; 
    }
}