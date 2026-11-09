package com.example.nhom7.model;

/**
 * Model đại diện cho Đơn ứng tuyển của sinh viên
 */
public class DonUngTuyen {
    private int donUngTuyenId;
    private int sinhVienId;
    private int viecLamId;
    private String loiGioiThieu;
    private String ngayUngTuyen;
    private String trangThai; // "Chờ xét duyệt", "Đã chấp nhận", "Đã từ chối"

    // Các trường hỗ trợ hiển thị chi tiết
    private String tenViecLam;
    private String tenNhaTuyenDung;
    private double mucLuong;
    private String hoTenSinhVien;

    public DonUngTuyen() {
    }

    public DonUngTuyen(int donUngTuyenId, int sinhVienId, int viecLamId, String loiGioiThieu, String ngayUngTuyen, String trangThai) {
        this.donUngTuyenId = donUngTuyenId;
        this.sinhVienId = sinhVienId;
        this.viecLamId = viecLamId;
        this.loiGioiThieu = loiGioiThieu;
        this.ngayUngTuyen = ngayUngTuyen;
        this.trangThai = trangThai;
    }

    public DonUngTuyen(int sinhVienId, int viecLamId, String loiGioiThieu, String ngayUngTuyen, String trangThai) {
        this.sinhVienId = sinhVienId;
        this.viecLamId = viecLamId;
        this.loiGioiThieu = loiGioiThieu;
        this.ngayUngTuyen = ngayUngTuyen;
        this.trangThai = trangThai;
    }

    public int getDonUngTuyenId() {
        return donUngTuyenId;
    }

    public void setDonUngTuyenId(int donUngTuyenId) {
        this.donUngTuyenId = donUngTuyenId;
    }

    public int getSinhVienId() {
        return sinhVienId;
    }

    public void setSinhVienId(int sinhVienId) {
        this.sinhVienId = sinhVienId;
    }

    public int getViecLamId() {
        return viecLamId;
    }

    public void setViecLamId(int viecLamId) {
        this.viecLamId = viecLamId;
    }

    public String getLoiGioiThieu() {
        return loiGioiThieu;
    }

    public void setLoiGioiThieu(String loiGioiThieu) {
        this.loiGioiThieu = loiGioiThieu;
    }

    public String getNgayUngTuyen() {
        return ngayUngTuyen;
    }

    public void setNgayUngTuyen(String ngayUngTuyen) {
        this.ngayUngTuyen = ngayUngTuyen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenViecLam() {
        return tenViecLam;
    }

    public void setTenViecLam(String tenViecLam) {
        this.tenViecLam = tenViecLam;
    }

    public String getTenNhaTuyenDung() {
        return tenNhaTuyenDung;
    }

    public void setTenNhaTuyenDung(String tenNhaTuyenDung) {
        this.tenNhaTuyenDung = tenNhaTuyenDung;
    }

    public double getMucLuong() {
        return mucLuong;
    }

    public void setMucLuong(double mucLuong) {
        this.mucLuong = mucLuong;
    }

    public String getHoTenSinhVien() {
        return hoTenSinhVien;
    }

    public void setHoTenSinhVien(String hoTenSinhVien) {
        this.hoTenSinhVien = hoTenSinhVien;
    }
}
