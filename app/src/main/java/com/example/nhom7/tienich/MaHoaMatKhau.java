package com.example.nhom7.tienich;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Lớp tiện ích mã hóa mật khẩu bằng SHA-256
 */
public class MaHoaMatKhau {

    public static String maHoaSHA256(String matKhauGoc) {
        if (matKhauGoc == null || matKhauGoc.isEmpty()) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(matKhauGoc.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return matKhauGoc; // Trả về mật khẩu gốc nếu không thể mã hóa
        }
    }
}
