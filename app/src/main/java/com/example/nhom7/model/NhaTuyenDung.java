package com.example.nhom7.model;

/**
 * Model đại diện cho thông tin Nhà tuyển dụng
 */
public class NhaTuyenDung {
    private int nhaTuyenDungId;
    private String tenNhaTuyenDung;
    private String email;
    private String soDienThoai;
    private String diaChi;

    public NhaTuyenDung() {
    }

    public NhaTuyenDung(int nhaTuyenDungId, String tenNhaTuyenDung, String email, String soDienThoai, String diaChi) {
        this.nhaTuyenDungId = nhaTuyenDungId;
        this.tenNhaTuyenDung = tenNhaTuyenDung;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    public NhaTuyenDung(String tenNhaTuyenDung, String email, String soDienThoai, String diaChi) {
        this.tenNhaTuyenDung = tenNhaTuyenDung;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    public int getNhaTuyenDungId() {
        return nhaTuyenDungId;
    }

    public void setNhaTuyenDungId(int nhaTuyenDungId) {
        this.nhaTuyenDungId = nhaTuyenDungId;
    }

    public String getTenNhaTuyenDung() {
        return tenNhaTuyenDung;
    }

    public void setTenNhaTuyenDung(String tenNhaTuyenDung) {
        this.tenNhaTuyenDung = tenNhaTuyenDung;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}
