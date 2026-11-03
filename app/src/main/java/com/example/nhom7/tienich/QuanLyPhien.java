package com.example.nhom7.tienich;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Lớp quản lý trạng thái đăng nhập của sinh viên bằng SharedPreferences
 */
public class QuanLyPhien {
    private static final String PREF_NAME = "PartTimeJobSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_SINH_VIEN_ID = "sinhVienId";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_HO_TEN = "hoTen";
    private static final String KEY_MA_SINH_VIEN = "maSinhVien";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public QuanLyPhien(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Lưu thông tin khi đăng nhập thành công
    public void taoPhienDangNhap(int sinhVienId, String maSinhVien, String hoTen, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_SINH_VIEN_ID, sinhVienId);
        editor.putString(KEY_MA_SINH_VIEN, maSinhVien);
        editor.putString(KEY_HO_TEN, hoTen);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Kiểm tra sinh viên đã đăng nhập chưa
    public boolean daDangNhap() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Kiểm tra phiên đăng nhập hiện tại có phải tài khoản Admin không
    public boolean laAdmin() {
        String email = layEmail();
        String maSV = layMaSinhVien();
        return "admin@gmail.com".equalsIgnoreCase(email) || "ADMIN001".equalsIgnoreCase(maSV);
    }

    // Lấy ID sinh viên hiện tại
    public int laySinhVienId() {
        return pref.getInt(KEY_SINH_VIEN_ID, -1);
    }

    // Lấy Mã sinh viên hiện tại
    public String layMaSinhVien() {
        return pref.getString(KEY_MA_SINH_VIEN, "");
    }

    // Lấy Họ tên sinh viên
    public String layHoTen() {
        return pref.getString(KEY_HO_TEN, "");
    }

    // Lấy Email sinh viên
    public String layEmail() {
        return pref.getString(KEY_EMAIL, "");
    }

    // Cập nhật thông tin phiên nếu đổi tên
    public void capNhatHoTen(String hoTenMoi) {
        editor.putString(KEY_HO_TEN, hoTenMoi);
        editor.apply();
    }

    // Đăng xuất - Xóa dữ liệu phiên
    public void dangXuat() {
        editor.clear();
        editor.apply();
    }
}
