package com.example.nhom7.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom7.model.SinhVien;

/**
 * Lớp DAO xử lý dữ liệu liên quan đến Sinh viên
 */
public class SinhVienDAO {
    private final QuanLyCSDL dbHelper;

    public SinhVienDAO(Context context) {
        dbHelper = new QuanLyCSDL(context);
    }

    // Đăng ký tài khoản Sinh viên mới
    public long themSinhVien(SinhVien sv) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ma_sinh_vien", sv.getMaSinhVien());
        cv.put("ho_ten", sv.getHoTen());
        cv.put("email", sv.getEmail());
        cv.put("so_dien_thoai", sv.getSoDienThoai());
        cv.put("mat_khau_hash", sv.getMatKhauHash());
        cv.put("ten_truong", sv.getTenTruong());
        cv.put("gioi_thieu", sv.getGioiThieu());

        long result = db.insert(QuanLyCSDL.BANG_SINH_VIEN, null, cv);
        db.close();
        return result;
    }

    // Kiểm tra Email đã tồn tại chưa
    public boolean kiemTraEmailTonTai(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sinh_vien_id FROM " + QuanLyCSDL.BANG_SINH_VIEN + " WHERE email = ?", new String[]{email});
        boolean tonTai = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return tonTai;
    }

    // Kiểm tra Mã sinh viên đã tồn tại chưa
    public boolean kiemTraMaSinhVienTonTai(String maSV) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sinh_vien_id FROM " + QuanLyCSDL.BANG_SINH_VIEN + " WHERE ma_sinh_vien = ?", new String[]{maSV});
        boolean tonTai = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return tonTai;
    }

    // Đăng nhập: Kiểm tra email và mật khẩu đã mã hóa
    public SinhVien dangNhap(String email, String matKhauHash) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SinhVien sv = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuanLyCSDL.BANG_SINH_VIEN + " WHERE email = ? AND mat_khau_hash = ?", new String[]{email, matKhauHash});

        if (cursor.moveToFirst()) {
            sv = docCursorSinhVien(cursor);
        }
        cursor.close();
        db.close();
        return sv;
    }

    // Lấy thông tin Sinh viên theo ID
    public SinhVien laySinhVienTheoId(int sinhVienId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SinhVien sv = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuanLyCSDL.BANG_SINH_VIEN + " WHERE sinh_vien_id = ?", new String[]{String.valueOf(sinhVienId)});

        if (cursor.moveToFirst()) {
            sv = docCursorSinhVien(cursor);
        }
        cursor.close();
        db.close();
        return sv;
    }

    // Cập nhật thông tin cá nhân
    public boolean capNhatThongTin(int sinhVienId, String hoTen, String soDienThoai, String tenTruong, String gioiThieu) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ho_ten", hoTen);
        cv.put("so_dien_thoai", soDienThoai);
        cv.put("ten_truong", tenTruong);
        cv.put("gioi_thieu", gioiThieu);

        int rows = db.update(QuanLyCSDL.BANG_SINH_VIEN, cv, "sinh_vien_id = ?", new String[]{String.valueOf(sinhVienId)});
        db.close();
        return rows > 0;
    }

    // Đổi mật khẩu
    public boolean doiMatKhau(int sinhVienId, String matKhauHashMoi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mat_khau_hash", matKhauHashMoi);

        int rows = db.update(QuanLyCSDL.BANG_SINH_VIEN, cv, "sinh_vien_id = ?", new String[]{String.valueOf(sinhVienId)});
        db.close();
        return rows > 0;
    }

    // Hàm đọc Cursor chuyển thành SinhVien
    private SinhVien docCursorSinhVien(Cursor cursor) {
        SinhVien sv = new SinhVien();
        sv.setSinhVienId(cursor.getInt(cursor.getColumnIndexOrThrow("sinh_vien_id")));
        sv.setMaSinhVien(cursor.getString(cursor.getColumnIndexOrThrow("ma_sinh_vien")));
        sv.setHoTen(cursor.getString(cursor.getColumnIndexOrThrow("ho_ten")));
        sv.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        sv.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow("so_dien_thoai")));
        sv.setMatKhauHash(cursor.getString(cursor.getColumnIndexOrThrow("mat_khau_hash")));
        sv.setTenTruong(cursor.getString(cursor.getColumnIndexOrThrow("ten_truong")));
        sv.setGioiThieu(cursor.getString(cursor.getColumnIndexOrThrow("gioi_thieu")));
        return sv;
    }
}
