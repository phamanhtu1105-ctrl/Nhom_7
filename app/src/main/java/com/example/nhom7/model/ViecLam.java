package com.example.nhom7.model;

/**
 * Model đại diện cho thông tin Việc làm
 */
public class ViecLam {
    private int viecLamId;
    private int nhaTuyenDungId;
    private int danhMucId;
    private String tenViecLam;
    private String moTa;
    private double mucLuong;
    private String diaDiem;
    private String thoiGianLamViec;
    private String hinhThucLamViec;
    private String yeuCau;
    private String quyenLoi;
    private String hanUngTuyen;
    private String ngayDang;

    // Các trường hỗ trợ hiển thị thông tin mở rộng
    private String tenNhaTuyenDung;
    private String tenDanhMuc;

    public ViecLam() {
    }

    public ViecLam(int viecLamId, int nhaTuyenDungId, int danhMucId, String tenViecLam, String moTa, double mucLuong, String diaDiem, String thoiGianLamViec, String hinhThucLamViec, String yeuCau, String quyenLoi, String hanUngTuyen, String ngayDang) {
        this.viecLamId = viecLamId;
        this.nhaTuyenDungId = nhaTuyenDungId;
        this.danhMucId = danhMucId;
        this.tenViecLam = tenViecLam;
        this.moTa = moTa;
        this.mucLuong = mucLuong;
        this.diaDiem = diaDiem;
        this.thoiGianLamViec = thoiGianLamViec;
        this.hinhThucLamViec = hinhThucLamViec;
        this.yeuCau = yeuCau;
        this.quyenLoi = quyenLoi;
        this.hanUngTuyen = hanUngTuyen;
        this.ngayDang = ngayDang;
    }

    public int getViecLamId() {
        return viecLamId;
    }

    public void setViecLamId(int viecLamId) {
        this.viecLamId = viecLamId;
    }

    public int getNhaTuyenDungId() {
        return nhaTuyenDungId;
    }

    public void setNhaTuyenDungId(int nhaTuyenDungId) {
        this.nhaTuyenDungId = nhaTuyenDungId;
    }

    public int getDanhMucId() {
        return danhMucId;
    }

    public void setDanhMucId(int danhMucId) {
        this.danhMucId = danhMucId;
    }

    public String getTenViecLam() {
        return tenViecLam;
    }

    public void setTenViecLam(String tenViecLam) {
        this.tenViecLam = tenViecLam;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getMucLuong() {
        return mucLuong;
    }

    public void setMucLuong(double mucLuong) {
        this.mucLuong = mucLuong;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getThoiGianLamViec() {
        return thoiGianLamViec;
    }

    public void setThoiGianLamViec(String thoiGianLamViec) {
        this.thoiGianLamViec = thoiGianLamViec;
    }

    public String getHinhThucLamViec() {
        return hinhThucLamViec;
    }

    public void setHinhThucLamViec(String hinhThucLamViec) {
        this.hinhThucLamViec = hinhThucLamViec;
    }

    public String getYeuCau() {
        return yeuCau;
    }

    public void setYeuCau(String yeuCau) {
        this.yeuCau = yeuCau;
    }

    public String getQuyenLoi() {
        return quyenLoi;
    }

    public void setQuyenLoi(String quyenLoi) {
        this.quyenLoi = quyenLoi;
    }

    public String getHanUngTuyen() {
        return hanUngTuyen;
    }

    public void setHanUngTuyen(String hanUngTuyen) {
        this.hanUngTuyen = hanUngTuyen;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getTenNhaTuyenDung() {
        return tenNhaTuyenDung;
    }

    public void setTenNhaTuyenDung(String tenNhaTuyenDung) {
        this.tenNhaTuyenDung = tenNhaTuyenDung;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }
}
