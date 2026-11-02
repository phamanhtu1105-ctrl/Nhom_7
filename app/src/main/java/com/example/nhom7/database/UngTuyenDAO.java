package com.example.nhom7.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom7.model.DonUngTuyen;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý Đơn ứng tuyển của sinh viên
 */
public class UngTuyenDAO {
    private final QuanLyCSDL dbHelper;

    public UngTuyenDAO(Context context) {
        dbHelper = new QuanLyCSDL(context);
    }

    // Nộp đơn ứng tuyển công việc
    public long themDonUngTuyen(DonUngTuyen don) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sinh_vien_id", don.getSinhVienId());
        cv.put("viec_lam_id", don.getViecLamId());
        cv.put("loi_gioi_thieu", don.getLoiGioiThieu());
        cv.put("ngay_ung_tuyen", don.getNgayUngTuyen());
        cv.put("trang_thai", don.getTrangThai() != null ? don.getTrangThai() : "Chờ xét duyệt");

        long res = db.insert(QuanLyCSDL.BANG_DON_UNG_TUYEN, null, cv);
        db.close();
        return res;
    }

    // Kiểm tra sinh viên đã ứng tuyển công việc này chưa
    public boolean kiemTraDaUngTuyen(int sinhVienId, int viecLamId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT don_ung_tuyen_id FROM " + QuanLyCSDL.BANG_DON_UNG_TUYEN +
                " WHERE sinh_vien_id = ? AND viec_lam_id = ?", new String[]{String.valueOf(sinhVienId), String.valueOf(viecLamId)});
        boolean daUngTuyen = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return daUngTuyen;
    }

    // Lấy danh sách các đơn ứng tuyển của một sinh viên
    public List<DonUngTuyen> layDanhSachDonUngTuyenTheoSinhVien(int sinhVienId) {
        List<DonUngTuyen> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT ut.*, vl.ten_viec_lam, vl.muc_luong, ntd.ten_nha_tuyen_dung " +
                "FROM " + QuanLyCSDL.BANG_DON_UNG_TUYEN + " ut " +
                "INNER JOIN " + QuanLyCSDL.BANG_VIEC_LAM + " vl ON ut.viec_lam_id = vl.viec_lam_id " +
                "LEFT JOIN " + QuanLyCSDL.BANG_NHA_TUYEN_DUNG + " ntd ON vl.nha_tuyen_dung_id = ntd.nha_tuyen_dung_id " +
                "WHERE ut.sinh_vien_id = ? " +
                "ORDER BY ut.don_ung_tuyen_id DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(sinhVienId)});
        if (cursor.moveToFirst()) {
            do {
                DonUngTuyen ut = new DonUngTuyen();
                ut.setDonUngTuyenId(cursor.getInt(cursor.getColumnIndexOrThrow("don_ung_tuyen_id")));
                ut.setSinhVienId(cursor.getInt(cursor.getColumnIndexOrThrow("sinh_vien_id")));
                ut.setViecLamId(cursor.getInt(cursor.getColumnIndexOrThrow("viec_lam_id")));
                ut.setLoiGioiThieu(cursor.getString(cursor.getColumnIndexOrThrow("loi_gioi_thieu")));
                ut.setNgayUngTuyen(cursor.getString(cursor.getColumnIndexOrThrow("ngay_ung_tuyen")));
                ut.setTrangThai(cursor.getString(cursor.getColumnIndexOrThrow("trang_thai")));

                ut.setTenViecLam(cursor.getString(cursor.getColumnIndexOrThrow("ten_viec_lam")));
                ut.setMucLuong(cursor.getDouble(cursor.getColumnIndexOrThrow("muc_luong")));
                ut.setTenNhaTuyenDung(cursor.getString(cursor.getColumnIndexOrThrow("ten_nha_tuyen_dung")));

                list.add(ut);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Hủy đơn ứng tuyển
    public boolean huyDonUngTuyen(int donUngTuyenId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(QuanLyCSDL.BANG_DON_UNG_TUYEN, "don_ung_tuyen_id = ?", new String[]{String.valueOf(donUngTuyenId)});
        db.close();
        return rows > 0;
    }
}
