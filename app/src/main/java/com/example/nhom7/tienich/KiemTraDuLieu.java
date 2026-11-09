package com.example.nhom7.tienich;

import android.util.Patterns;

/**
 * Lớp tiện ích kiểm tra dữ liệu đầu vào
 */
public class KiemTraDuLieu {

    // Kiểm tra chuỗi rỗng
    public static boolean kiemTraRong(String text) {
        return text == null || text.trim().isEmpty();
    }

    // Kiểm tra định dạng Email hợp lệ
    public static boolean kiemTraEmail(String email) {
        if (kiemTraRong(email)) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    // Kiểm tra định dạng Số điện thoại (10-11 chữ số)
    public static boolean kiemTraSoDienThoai(String sdt) {
        if (kiemTraRong(sdt)) {
            return false;
        }
        return sdt.trim().matches("^[0-9]{10,11}$");
    }

    // Kiểm tra độ dài mật khẩu (ít nhất 6 ký tự)
    public static boolean kiemTraMatKhau(String matKhau) {
        if (kiemTraRong(matKhau)) {
            return false;
        }
        return matKhau.trim().length() >= 6;
    }

    // Kiểm tra mật khẩu nhập lại có khớp không
    public static boolean kiemTraKhopMatKhau(String matKhau, String xacNhanMatKhau) {
        if (matKhau == null || xacNhanMatKhau == null) {
            return false;
        }
        return matKhau.equals(xacNhanMatKhau);
    }
}
