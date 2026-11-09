package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.adapter.DanhMucAdapter;
import com.example.nhom7.adapter.ViecLamAdapter;
import com.example.nhom7.database.ViecLamDAO;
import com.example.nhom7.model.DanhMuc;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.tienich.QuanLyPhien;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Màn hình Trang chủ hệ thống
 */
public class TrangChuActivity extends AppCompatActivity {

    private TextView tvChaoSinhVien, tvXemTatCa, tvKhongCoDuLieu;
    private EditText edtTimKiem;
    private ImageView btnTimKiem;
    private RecyclerView rvDanhMuc, rvDanhSachViecLam;
    private BottomNavigationView bottomNavigation;

    private QuanLyPhien quanLyPhien;
    private ViecLamDAO viecLamDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quanLyPhien = new QuanLyPhien(this);

        // Kiểm tra phiên đăng nhập
        if (!quanLyPhien.daDangNhap()) {
            chuyenSangDangNhap();
            return;
        }

        setContentView(R.layout.activity_trang_chu);

        viecLamDAO = new ViecLamDAO(this);

        anhXaGiaoDien();
        hienThiThongTinNguoiDung();
        hienThiDanhSachDanhMuc();
        hienThiDanhSachViecLamMoi();
        caiDatSuKien();
        caiDatBottomNavigation();
    }

    private void anhXaGiaoDien() {
        tvChaoSinhVien = findViewById(R.id.tvChaoSinhVien);
        tvXemTatCa = findViewById(R.id.tvXemTatCa);
        tvKhongCoDuLieu = findViewById(R.id.tvKhongCoDuLieu);
        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        rvDanhMuc = findViewById(R.id.rvDanhMuc);
        rvDanhSachViecLam = findViewById(R.id.rvDanhSachViecLam);
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }

    private void hienThiThongTinNguoiDung() {
        String hoTen = quanLyPhien.layHoTen();
        if (hoTen != null && !hoTen.isEmpty()) {
            tvChaoSinhVien.setText(hoTen);
        } else {
            tvChaoSinhVien.setText(R.string.sub_title_app);
        }
    }

    private void hienThiDanhSachDanhMuc() {
        List<DanhMuc> danhSach = viecLamDAO.layDanhSachDanhMuc();
        rvDanhMuc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DanhMucAdapter adapter = new DanhMucAdapter(this, danhSach, danhMuc -> {
            // Chuyển sang màn hình Tìm việc lọc theo Danh mục chọn
            Intent intent = new Intent(TrangChuActivity.this, TimViecActivity.class);
            intent.putExtra("danhMucId", danhMuc.getDanhMucId());
            intent.putExtra("tenDanhMuc", danhMuc.getTenDanhMuc());
            startActivity(intent);
        });
        rvDanhMuc.setAdapter(adapter);
    }

    private void hienThiDanhSachViecLamMoi() {
        List<ViecLam> danhSachViec = viecLamDAO.layViecLamMoiNhat(10);

        if (danhSachViec == null || danhSachViec.isEmpty()) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvDanhSachViecLam.setVisibility(View.GONE);
        } else {
            tvKhongCoDuLieu.setVisibility(View.GONE);
            rvDanhSachViecLam.setVisibility(View.VISIBLE);

            rvDanhSachViecLam.setLayoutManager(new LinearLayoutManager(this));
            ViecLamAdapter adapter = new ViecLamAdapter(this, danhSachViec, viecLam -> {
                // Chuyển sang màn hình Chi tiết công việc
                Intent intent = new Intent(TrangChuActivity.this, ChiTietViecActivity.class);
                intent.putExtra("viecLamId", viecLam.getViecLamId());
                startActivity(intent);
            });
            rvDanhSachViecLam.setAdapter(adapter);
        }
    }

    private void caiDatSuKien() {
        // Tìm kiếm nhanh khi bấm biểu tượng kính lúp
        btnTimKiem.setOnClickListener(v -> thucHienTimKiem());

        // Xem tất cả việc làm
        tvXemTatCa.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, TimViecActivity.class);
            startActivity(intent);
        });
    }

    private void thucHienTimKiem() {
        String tuKhoa = edtTimKiem.getText().toString().trim();
        Intent intent = new Intent(TrangChuActivity.this, TimViecActivity.class);
        intent.putExtra("tuKhoaTimKiem", tuKhoa);
        startActivity(intent);
    }

    private void caiDatBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.menu_trang_chu);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_trang_chu) {
                return true;
            } else if (itemId == R.id.menu_tim_viec) {
                startActivity(new Intent(this, TimViecActivity.class));
                return true;
            } else if (itemId == R.id.menu_yeu_thich) {
                startActivity(new Intent(this, ViecYeuThichActivity.class));
                return true;
            } else if (itemId == R.id.menu_don_ung_tuyen) {
                startActivity(new Intent(this, DonUngTuyenActivity.class));
                return true;
            } else if (itemId == R.id.menu_ho_so) {
                startActivity(new Intent(this, HoSoCaNhanActivity.class));
                return true;
            }
            return false;
        });
    }

    private void chuyenSangDangNhap() {
        Intent intent = new Intent(TrangChuActivity.this, DangNhapActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại danh sách và thông tin khi quay trở về Trang Chủ
        if (quanLyPhien.daDangNhap()) {
            hienThiThongTinNguoiDung();
            hienThiDanhSachViecLamMoi();
            bottomNavigation.setSelectedItemId(R.id.menu_trang_chu);
        }
    }
}
