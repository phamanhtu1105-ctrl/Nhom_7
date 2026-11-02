package com.example.nhom7.model;

/**
 * Model đại diện cho thông tin Sinh viên
 */
public class SinhVien {
    private int sinhVienId;
    private String maSinhVien;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String matKhauHash;
    private String tenTruong;
    private String gioiThieu;

    public SinhVien() {
    }

    public SinhVien(int sinhVienId, String maSinhVien, String hoTen, String email, String soDienThoai, String matKhauHash, String tenTruong, String gioiThieu) {
        this.sinhVienId = sinhVienId;
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhauHash = matKhauHash;
        this.tenTruong = tenTruong;
        this.gioiThieu = gioiThieu;
    }

    public SinhVien(String maSinhVien, String hoTen, String email, String soDienThoai, String matKhauHash, String tenTruong, String gioiThieu) {
        this.maSinhVien = maSinhVien;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhauHash = matKhauHash;
        this.tenTruong = tenTruong;
        this.gioiThieu = gioiThieu;
    }

    public int getSinhVienId() {
        return sinhVienId;
    }

    public void setSinhVienId(int sinhVienId) {
        this.sinhVienId = sinhVienId;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
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

    public String getMatKhauHash() {
        return matKhauHash;
    }

    public void setMatKhauHash(String matKhauHash) {
        this.matKhauHash = matKhauHash;
    }

    public String getTenTruong() {
        return tenTruong;
    }

    public void setTenTruong(String tenTruong) {
        this.tenTruong = tenTruong;
    }

    public String getGioiThieu() {
        return gioiThieu;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }
}
