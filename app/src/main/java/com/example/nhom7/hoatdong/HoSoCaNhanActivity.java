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
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Màn hình Quản lý Hồ sơ cá nhân và Đổi mật khẩu
 */
public class HoSoCaNhanActivity extends AppCompatActivity {

    private TextView tvMaSinhVien, tvEmail;
    private EditText edtHoTen, edtSoDienThoai, edtTenTruong, edtGioiThieu;
    private EditText edtMatKhauCu, edtMatKhauMoi, edtXacNhanMatKhauMoi;
    private Button btnCapNhatHoSo, btnDoiMatKhau, btnDangXuat;
    private BottomNavigationView bottomNavigation;

    private SinhVienDAO sinhVienDAO;
    private QuanLyPhien quanLyPhien;
    private SinhVien sinhVienCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so_ca_nhan);

        sinhVienDAO = new SinhVienDAO(this);
        quanLyPhien = new QuanLyPhien(this);

        anhXaGiaoDien();
        taiThongTinHoSo();
        caiDatSuKien();
        caiDatBottomNavigation();
    }

    private void anhXaGiaoDien() {
        tvMaSinhVien = findViewById(R.id.tvMaSinhVien);
        tvEmail = findViewById(R.id.tvEmail);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtSoDienThoai = findViewById(R.id.edtSoDienThoai);
        edtTenTruong = findViewById(R.id.edtTenTruong);
        edtGioiThieu = findViewById(R.id.edtGioiThieu);

        edtMatKhauCu = findViewById(R.id.edtMatKhauCu);
        edtMatKhauMoi = findViewById(R.id.edtMatKhauMoi);
        edtXacNhanMatKhauMoi = findViewById(R.id.edtXacNhanMatKhauMoi);

        btnCapNhatHoSo = findViewById(R.id.btnCapNhatHoSo);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhau);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void taiThongTinHoSo() {
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId == -1) {
            chuyenSangDangNhap();
            return;
        }

        sinhVienCurrent = sinhVienDAO.laySinhVienTheoId(sinhVienId);
        if (sinhVienCurrent != null) {
            tvMaSinhVien.setText(sinhVienCurrent.getMaSinhVien());
            tvEmail.setText(sinhVienCurrent.getEmail());

            edtHoTen.setText(sinhVienCurrent.getHoTen());
            edtSoDienThoai.setText(sinhVienCurrent.getSoDienThoai());
            edtTenTruong.setText(sinhVienCurrent.getTenTruong());
            edtGioiThieu.setText(sinhVienCurrent.getGioiThieu());
        }
    }

    private void caiDatSuKien() {
        btnCapNhatHoSo.setOnClickListener(v -> xuLyCapNhatHoSo());

        btnDoiMatKhau.setOnClickListener(v -> xuLyDoiMatKhau());

        btnDangXuat.setOnClickListener(v -> xuLyDangXuat());
    }

    private void xuLyCapNhatHoSo() {
        if (sinhVienCurrent == null) return;

        String hoTen = edtHoTen.getText().toString().trim();
        String sdt = edtSoDienThoai.getText().toString().trim();
        String tenTruong = edtTenTruong.getText().toString().trim();
        String gioiThieu = edtGioiThieu.getText().toString().trim();

        if (KiemTraDuLieu.kiemTraRong(hoTen)) {
            edtHoTen.setError("Họ và tên không được để trống");
            edtHoTen.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(sdt) || !KiemTraDuLieu.kiemTraSoDienThoai(sdt)) {
            edtSoDienThoai.setError("Số điện thoại không hợp lệ");
            edtSoDienThoai.requestFocus();
            return;
        }

        if (KiemTraDuLieu.kiemTraRong(tenTruong)) {
            edtTenTruong.setError("Trường học không được để trống");
            edtTenTruong.requestFocus();
            return;
        }

        boolean success = sinhVienDAO.capNhatThongTin(sinhVienCurrent.getSinhVienId(), hoTen, sdt, tenTruong, gioiThieu);
        if (success) {
            quanLyPhien.capNhatHoTen(hoTen);
            Toast.makeText(this, "Cập nhật thông tin hồ sơ thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyDoiMatKhau() {
        if (sinhVienCurrent == null) return;

        String matKhauCu = edtMatKhauCu.getText().toString().trim();
        String matKhauMoi = edtMatKhauMoi.getText().toString().trim();
        String xacNhanMoi = edtXacNhanMatKhauMoi.getText().toString().trim();

        if (KiemTraDuLieu.kiemTraRong(matKhauCu)) {
            edtMatKhauCu.setError("Vui lòng nhập mật khẩu hiện tại");
            edtMatKhauCu.requestFocus();
            return;
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        String matKhauCuHash = MaHoaMatKhau.maHoaSHA256(matKhauCu);
        if (!matKhauCuHash.equals(sinhVienCurrent.getMatKhauHash())) {
            edtMatKhauCu.setError("Mật khẩu hiện tại không chính xác!");
            edtMatKhauCu.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraMatKhau(matKhauMoi)) {
            edtMatKhauMoi.setError("Mật khẩu mới phải từ 6 ký tự trở lên");
            edtMatKhauMoi.requestFocus();
            return;
        }

        if (!KiemTraDuLieu.kiemTraKhopMatKhau(matKhauMoi, xacNhanMoi)) {
            edtXacNhanMatKhauMoi.setError("Mật khẩu mới không trùng khớp");
            edtXacNhanMatKhauMoi.requestFocus();
            return;
        }

        String matKhauMoiHash = MaHoaMatKhau.maHoaSHA256(matKhauMoi);
        boolean success = sinhVienDAO.doiMatKhau(sinhVienCurrent.getSinhVienId(), matKhauMoiHash);

        if (success) {
            sinhVienCurrent.setMatKhauHash(matKhauMoiHash);
            edtMatKhauCu.setText("");
            edtMatKhauMoi.setText("");
            edtXacNhanMatKhauMoi.setText("");
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi đổi mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void xuLyDangXuat() {
        quanLyPhien.dangXuat();
        Toast.makeText(this, "Đã đăng xuất tài khoản!", Toast.LENGTH_SHORT).show();
        chuyenSangDangNhap();
    }

    private void chuyenSangDangNhap() {
        Intent intent = new Intent(HoSoCaNhanActivity.this, DangNhapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void caiDatBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.menu_ho_so);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_trang_chu) {
                startActivity(new Intent(this, TrangChuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_tim_viec) {
                startActivity(new Intent(this, TimViecActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_yeu_thich) {
                startActivity(new Intent(this, ViecYeuThichActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_don_ung_tuyen) {
                startActivity(new Intent(this, DonUngTuyenActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_ho_so) {
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiThongTinHoSo();
        bottomNavigation.setSelectedItemId(R.id.menu_ho_so);
    }
}
