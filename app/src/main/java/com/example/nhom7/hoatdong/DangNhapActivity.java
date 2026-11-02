package com.example.nhom7.hoatdong;

import android.content.Intent;
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
import com.example.nhom7.tienich.QuanLyPhien;

/**
 * Màn hình Đăng nhập
 */
public class DangNhapActivity extends AppCompatActivity {

    private EditText edtEmail, edtMatKhau;
    private Button btnDangNhap;
    private TextView tvChuyenDangKy;

    private SinhVienDAO sinhVienDAO;
    private QuanLyPhien quanLyPhien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quanLyPhien = new QuanLyPhien(this);

        // Kiểm tra nếu đã đăng nhập trước đó thì chuyển thẳng đến Trang Chu
        if (quanLyPhien.daDangNhap()) {
            chuyenTrangChu();
            finish();
            return;
        }

        setContentView(R.layout.activity_dang_nhap);

        sinhVienDAO = new SinhVienDAO(this);

        anhXaGiaoDien();
        caiDatSuKien();
    }

    private void anhXaGiaoDien() {
        edtEmail = findViewById(R.id.edtEmail);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        tvChuyenDangKy = findViewById(R.id.tvChuyenDangKy);
    }

    private void caiDatSuKien() {
        btnDangNhap.setOnClickListener(v -> xuLyDangNhap());

        tvChuyenDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(intent);
        });
    }

    private void xuLyDangNhap() {
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        // Kiểm tra dữ liệu đầu vào
        if (KiemTraDuLieu.kiemTraRong(email)) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraEmail(email)) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(matKhau)) {
            edtMatKhau.setError("Vui lòng nhập mật khẩu");
            edtMatKhau.requestFocus();
            return;
        }

        // Mã hóa mật khẩu nhập vào để so sánh với CSDL
        String matKhauHash = MaHoaMatKhau.maHoaSHA256(matKhau);

        SinhVien sv = sinhVienDAO.dangNhap(email, matKhauHash);

        if (sv != null) {
            // Đăng nhập thành công -> Lưu phiên và chuyển màn hình
            quanLyPhien.taoPhienDangNhap(sv.getSinhVienId(), sv.getMaSinhVien(), sv.getHoTen(), sv.getEmail());
            Toast.makeText(this, "Đăng nhập thành công! Chào " + sv.getHoTen(), Toast.LENGTH_SHORT).show();

            chuyenTrangChu();
            finish();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_LONG).show();
        }
    }

    private void chuyenTrangChu() {
        Intent intent = new Intent(DangNhapActivity.this, TrangChuActivity.class);
        startActivity(intent);
    }
}
