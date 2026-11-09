package com.example.nhom7.model;

/**
 * Model đại diện cho Danh mục công việc
 */
public class DanhMuc {
    private int danhMucId;
    private String tenDanhMuc;

    public DanhMuc() {
    }

    public DanhMuc(int danhMucId, String tenDanhMuc) {
        this.danhMucId = danhMucId;
        this.tenDanhMuc = tenDanhMuc;
    }

    public DanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public int getDanhMucId() {
        return danhMucId;
    }

    public void setDanhMucId(int danhMucId) {
        this.danhMucId = danhMucId;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }
}
