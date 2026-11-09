package com.example.nhom7.hoatdong;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom7.R;
import com.example.nhom7.database.SinhVienDAO;
import com.example.nhom7.model.SinhVien;
import com.example.nhom7.tienich.KiemTraDuLieu;
import com.example.nhom7.tienich.MaHoaMatKhau;

/**
 * Màn hình Đăng ký tài khoản Sinh viên
 */
public class DangKyActivity extends AppCompatActivity {

    private EditText edtMaSinhVien, edtHoTen, edtEmail, edtSoDienThoai, edtTenTruong, edtMatKhau, edtXacNhanMatKhau;
    private Button btnDangKy;
    private TextView tvChuyenDangNhap;

    private SinhVienDAO sinhVienDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        sinhVienDAO = new SinhVienDAO(this);

        anhXaGiaoDien();
        caiDatSuKien();
    }

    private void anhXaGiaoDien() {
        edtMaSinhVien = findViewById(R.id.edtMaSinhVien);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtTenTruong = findViewById(R.id.edtTenTruong);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtXacNhanMatKhau = findViewById(R.id.edtXacNhanMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        tvChuyenDangNhap = findViewById(R.id.tvChuyenDangNhap);
    }

    private void caiDatSuKien() {
        btnDangKy.setOnClickListener(v -> xuLyDangKy());

        tvChuyenDangNhap.setOnClickListener(v -> finish());
    }

    private void xuLyDangKy() {
        String maSV = edtMaSinhVien.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String sdt = edtSoDienThoai.getText().toString().trim();
        String tenTruong = edtTenTruong.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String xacNhanMatKhau = edtXacNhanMatKhau.getText().toString().trim();

        // 1. Kiểm tra các trường bắt buộc không rỗng
        if (KiemTraDuLieu.kiemTraRong(maSV)) {
            edtMaSinhVien.setError("Vui lòng nhập Mã sinh viên");
            edtMaSinhVien.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(hoTen)) {
            edtHoTen.setError("Vui lòng nhập Họ và tên");
            edtHoTen.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(email)) {
            edtEmail.setError("Vui lòng nhập Email");
            edtEmail.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraEmail(email)) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(sdt)) {
            edtSoDienThoai.setError("Vui lòng nhập Số điện thoại");
            edtSoDienThoai.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraSoDienThoai(sdt)) {
            edtSoDienThoai.setError("Số điện thoại không hợp lệ (10-11 chữ số)");
            edtSoDienThoai.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(tenTruong)) {
            edtTenTruong.setError("Vui lòng nhập Tên trường học");
            edtTenTruong.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraMatKhau(matKhau)) {
            edtMatKhau.setError("Mật khẩu phải từ 6 ký tự trở lên");
            edtMatKhau.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraKhopMatKhau(matKhau, xacNhanMatKhau)) {
            edtXacNhanMatKhau.setError("Xác nhận mật khẩu không trùng khớp");
            edtXacNhanMatKhau.requestFocus();
            return;
        }

        // 2. Kiểm tra trùng lặp trong CSDL
        if (sinhVienDAO.kiemTraMaSinhVienTonTai(maSV)) {
            edtMaSinhVien.setError("Mã sinh viên này đã được đăng ký!");
            edtMaSinhVien.requestFocus();
            return;
        }

        if (sinhVienDAO.kiemTraEmailTonTai(email)) {
            edtEmail.setError("Email này đã được sử dụng!");
            edtEmail.requestFocus();
            return;
        }

        // 3. Mã hóa mật khẩu và tạo đối tượng
        String matKhauHash = MaHoaMatKhau.maHoaSHA256(matKhau);
        SinhVien sv = new SinhVien(maSV, hoTen, email, sdt, matKhauHash, tenTruong, "");

        // 4. Lưu vào SQLite
        long result = sinhVienDAO.themSinhVien(sv);
        if (result > 0) {
            Toast.makeText(this, "Đăng ký tài khoản thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
            finish(); // Trở về màn hình Đăng nhập
        } else {
            Toast.makeText(this, "Đăng ký thất bại, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
        }
    }
}
