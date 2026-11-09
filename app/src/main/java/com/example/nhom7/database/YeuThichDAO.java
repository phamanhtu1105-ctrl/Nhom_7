package com.example.nhom7.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom7.model.ViecLam;
import com.example.nhom7.model.ViecYeuThich;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý danh sách Việc làm yêu thích
 */
public class YeuThichDAO {
    private final QuanLyCSDL dbHelper;

    public YeuThichDAO(Context context) {
        dbHelper = new QuanLyCSDL(context);
    }

    // Thêm công việc vào danh sách yêu thích
    public long themViecYeuThich(int sinhVienId, int viecLamId, String ngayThem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sinh_vien_id", sinhVienId);
        cv.put("viec_lam_id", viecLamId);
        cv.put("ngay_them", ngayThem);

        long res = db.insert(QuanLyCSDL.BANG_VIEC_YEU_THICH, null, cv);
        db.close();
        return res;
    }

    // Xóa công việc khỏi danh sách yêu thích
    public boolean xoaViecYeuThich(int sinhVienId, int viecLamId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(QuanLyCSDL.BANG_VIEC_YEU_THICH, "sinh_vien_id = ? AND viec_lam_id = ?",
                new String[]{String.valueOf(sinhVienId), String.valueOf(viecLamId)});
        db.close();
        return rows > 0;
    }

    // Kiểm tra công việc đã nằm trong danh sách yêu thích chưa
    public boolean kiemTraYeuThich(int sinhVienId, int viecLamId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT viec_yeu_thich_id FROM " + QuanLyCSDL.BANG_VIEC_YEU_THICH +
                " WHERE sinh_vien_id = ? AND viec_lam_id = ?", new String[]{String.valueOf(sinhVienId), String.valueOf(viecLamId)});
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isFavorite;
    }

    // Lấy danh sách các việc làm yêu thích của sinh viên
    public List<ViecYeuThich> layDanhSachYeuThichTheoSinhVien(int sinhVienId) {
        List<ViecYeuThich> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT yt.*, vl.ten_viec_lam, vl.mo_ta, vl.muc_luong, vl.dia_diem, vl.thoi_gian_lam_viec, vl.hinh_thuc_lam_viec, ntd.ten_nha_tuyen_dung, dm.ten_danh_muc " +
                "FROM " + QuanLyCSDL.BANG_VIEC_YEU_THICH + " yt " +
                "INNER JOIN " + QuanLyCSDL.BANG_VIEC_LAM + " vl ON yt.viec_lam_id = vl.viec_lam_id " +
                "LEFT JOIN " + QuanLyCSDL.BANG_NHA_TUYEN_DUNG + " ntd ON vl.nha_tuyen_dung_id = ntd.nha_tuyen_dung_id " +
                "LEFT JOIN " + QuanLyCSDL.BANG_DANH_MUC + " dm ON vl.danh_muc_id = dm.danh_muc_id " +
                "WHERE yt.sinh_vien_id = ? " +
                "ORDER BY yt.viec_yeu_thich_id DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(sinhVienId)});

        if (cursor.moveToFirst()) {
            do {
                ViecYeuThich yt = new ViecYeuThich();
                yt.setViecYeuThichId(cursor.getInt(cursor.getColumnIndexOrThrow("viec_yeu_thich_id")));
                yt.setSinhVienId(cursor.getInt(cursor.getColumnIndexOrThrow("sinh_vien_id")));
                yt.setViecLamId(cursor.getInt(cursor.getColumnIndexOrThrow("viec_lam_id")));
                yt.setNgayThem(cursor.getString(cursor.getColumnIndexOrThrow("ngay_them")));

                ViecLam vl = new ViecLam();
                vl.setViecLamId(yt.getViecLamId());
                vl.setTenViecLam(cursor.getString(cursor.getColumnIndexOrThrow("ten_viec_lam")));
                vl.setMoTa(cursor.getString(cursor.getColumnIndexOrThrow("mo_ta")));
                vl.setMucLuong(cursor.getDouble(cursor.getColumnIndexOrThrow("muc_luong")));
                vl.setDiaDiem(cursor.getString(cursor.getColumnIndexOrThrow("dia_diem")));
                vl.setThoiGianLamViec(cursor.getString(cursor.getColumnIndexOrThrow("thoi_gian_lam_viec")));
                vl.setHinhThucLamViec(cursor.getString(cursor.getColumnIndexOrThrow("hinh_thuc_lam_viec")));
                vl.setTenNhaTuyenDung(cursor.getString(cursor.getColumnIndexOrThrow("ten_nha_tuyen_dung")));
                vl.setTenDanhMuc(cursor.getString(cursor.getColumnIndexOrThrow("ten_danh_muc")));

                yt.setViecLam(vl);
                list.add(yt);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
