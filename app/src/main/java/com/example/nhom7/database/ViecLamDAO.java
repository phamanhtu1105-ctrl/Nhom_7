package com.example.nhom7.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom7.model.DanhMuc;
import com.example.nhom7.model.ViecLam;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý dữ liệu liên quan đến Việc làm và Danh mục
 */
public class ViecLamDAO {
    private final QuanLyCSDL dbHelper;

    public ViecLamDAO(Context context) {
        dbHelper = new QuanLyCSDL(context);
    }

    // Lấy danh sách tất cả Danh mục công việc
    public List<DanhMuc> layDanhSachDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuanLyCSDL.BANG_DANH_MUC + " ORDER BY danh_muc_id ASC", null);

        if (cursor.moveToFirst()) {
            do {
                DanhMuc dm = new DanhMuc();
                dm.setDanhMucId(cursor.getInt(cursor.getColumnIndexOrThrow("danh_muc_id")));
                dm.setTenDanhMuc(cursor.getString(cursor.getColumnIndexOrThrow("ten_danh_muc")));
                list.add(dm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Lấy tất cả việc làm kèm tên Nhà tuyển dụng và tên Danh mục
    public List<ViecLam> layTatCaViecLam() {
        return timKiemVaLocViecLam("", 0, "", 0.0, "moi_nhat");
    }

    // Lấy việc làm mới nhất
    public List<ViecLam> layViecLamMoiNhat(int limit) {
        List<ViecLam> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT vl.*, ntd.ten_nha_tuyen_dung, dm.ten_danh_muc " +
                "FROM " + QuanLyCSDL.BANG_VIEC_LAM + " vl " +
                "LEFT JOIN " + QuanLyCSDL.BANG_NHA_TUYEN_DUNG + " ntd ON vl.nha_tuyen_dung_id = ntd.nha_tuyen_dung_id " +
                "LEFT JOIN " + QuanLyCSDL.BANG_DANH_MUC + " dm ON vl.danh_muc_id = dm.danh_muc_id " +
                "ORDER BY vl.viec_lam_id DESC LIMIT " + limit;

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(docCursorViecLam(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Lấy chi tiết một việc làm theo ID
    public ViecLam layViecLamTheoId(int viecLamId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ViecLam vl = null;
        String sql = "SELECT vl.*, ntd.ten_nha_tuyen_dung, dm.ten_danh_muc " +
                "FROM " + QuanLyCSDL.BANG_VIEC_LAM + " vl " +
                "LEFT JOIN " + QuanLyCSDL.BANG_NHA_TUYEN_DUNG + " ntd ON vl.nha_tuyen_dung_id = ntd.nha_tuyen_dung_id " +
                "LEFT JOIN " + QuanLyCSDL.BANG_DANH_MUC + " dm ON vl.danh_muc_id = dm.danh_muc_id " +
                "WHERE vl.viec_lam_id = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(viecLamId)});
        if (cursor.moveToFirst()) {
            vl = docCursorViecLam(cursor);
        }
        cursor.close();
        db.close();
        return vl;
    }

    // Tìm kiếm, Lọc và Sắp xếp việc làm linh hoạt
    public List<ViecLam> timKiemVaLocViecLam(String tuKhoa, int danhMucId, String diaDiem, double luongToiThieu, String sapXep) {
        List<ViecLam> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder sql = new StringBuilder(
                "SELECT vl.*, ntd.ten_nha_tuyen_dung, dm.ten_danh_muc " +
                        "FROM " + QuanLyCSDL.BANG_VIEC_LAM + " vl " +
                        "LEFT JOIN " + QuanLyCSDL.BANG_NHA_TUYEN_DUNG + " ntd ON vl.nha_tuyen_dung_id = ntd.nha_tuyen_dung_id " +
                        "LEFT JOIN " + QuanLyCSDL.BANG_DANH_MUC + " dm ON vl.danh_muc_id = dm.danh_muc_id " +
                        "WHERE 1=1 "
        );

        List<String> params = new ArrayList<>();

        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            sql.append("AND (vl.ten_viec_lam LIKE ? OR ntd.ten_nha_tuyen_dung LIKE ?) ");
            String searchPattern = "%" + tuKhoa.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }

        if (danhMucId > 0) {
            sql.append("AND vl.danh_muc_id = ? ");
            params.add(String.valueOf(danhMucId));
        }

        if (diaDiem != null && !diaDiem.trim().isEmpty() && !diaDiem.equalsIgnoreCase("Tất cả")) {
            sql.append("AND vl.dia_diem LIKE ? ");
            params.add("%" + diaDiem.trim() + "%");
        }

        if (luongToiThieu > 0) {
            sql.append("AND vl.muc_luong >= ? ");
            params.add(String.valueOf(luongToiThieu));
        }

        if ("luong_tang".equalsIgnoreCase(sapXep)) {
            sql.append("ORDER BY vl.muc_luong ASC ");
        } else if ("luong_giam".equalsIgnoreCase(sapXep)) {
            sql.append("ORDER BY vl.muc_luong DESC ");
        } else {
            sql.append("ORDER BY vl.viec_lam_id DESC ");
        }

        Cursor cursor = db.rawQuery(sql.toString(), params.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                list.add(docCursorViecLam(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Đọc đối tượng ViecLam từ Cursor
    private ViecLam docCursorViecLam(Cursor cursor) {
        ViecLam vl = new ViecLam();
        vl.setViecLamId(cursor.getInt(cursor.getColumnIndexOrThrow("viec_lam_id")));
        vl.setNhaTuyenDungId(cursor.getInt(cursor.getColumnIndexOrThrow("nha_tuyen_dung_id")));
        vl.setDanhMucId(cursor.getInt(cursor.getColumnIndexOrThrow("danh_muc_id")));
        vl.setTenViecLam(cursor.getString(cursor.getColumnIndexOrThrow("ten_viec_lam")));
        vl.setMoTa(cursor.getString(cursor.getColumnIndexOrThrow("mo_ta")));
        vl.setMucLuong(cursor.getDouble(cursor.getColumnIndexOrThrow("muc_luong")));
        vl.setDiaDiem(cursor.getString(cursor.getColumnIndexOrThrow("dia_diem")));
        vl.setThoiGianLamViec(cursor.getString(cursor.getColumnIndexOrThrow("thoi_gian_lam_viec")));
        vl.setHinhThucLamViec(cursor.getString(cursor.getColumnIndexOrThrow("hinh_thuc_lam_viec")));
        vl.setYeuCau(cursor.getString(cursor.getColumnIndexOrThrow("yeu_cau")));
        vl.setQuyenLoi(cursor.getString(cursor.getColumnIndexOrThrow("quyen_loi")));
        vl.setHanUngTuyen(cursor.getString(cursor.getColumnIndexOrThrow("han_ung_tuyen")));
        vl.setNgayDang(cursor.getString(cursor.getColumnIndexOrThrow("ngay_dang")));

        vl.setTenNhaTuyenDung(cursor.getString(cursor.getColumnIndexOrThrow("ten_nha_tuyen_dung")));
        vl.setTenDanhMuc(cursor.getString(cursor.getColumnIndexOrThrow("ten_danh_muc")));
        return vl;
    }
}
