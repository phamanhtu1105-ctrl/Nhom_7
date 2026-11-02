package com.example.nhom7.model;

/**
 * Model đại diện cho Công việc yêu thích
 */
public class ViecYeuThich {
    private int viecYeuThichId;
    private int sinhVienId;
    private int viecLamId;
    private String ngayThem;

    // Đối tượng việc làm liên kết để dễ hiển thị
    private ViecLam viecLam;

    public ViecYeuThich() {
    }

    public ViecYeuThich(int viecYeuThichId, int sinhVienId, int viecLamId, String ngayThem) {
        this.viecYeuThichId = viecYeuThichId;
        this.sinhVienId = sinhVienId;
        this.viecLamId = viecLamId;
        this.ngayThem = ngayThem;
    }

    public ViecYeuThich(int sinhVienId, int viecLamId, String ngayThem) {
        this.sinhVienId = sinhVienId;
        this.viecLamId = viecLamId;
        this.ngayThem = ngayThem;
    }

    public int getViecYeuThichId() {
        return viecYeuThichId;
    }

    public void setViecYeuThichId(int viecYeuThichId) {
        this.viecYeuThichId = viecYeuThichId;
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

    public String getNgayThem() {
        return ngayThem;
    }

    public void setNgayThem(String ngayThem) {
        this.ngayThem = ngayThem;
    }

    public ViecLam getViecLam() {
        return viecLam;
    }

    public void setViecLam(ViecLam viecLam) {
        this.viecLam = viecLam;
    }
}
