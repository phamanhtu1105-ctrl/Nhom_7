package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.adapter.DonUngTuyenAdapter;
import com.example.nhom7.database.UngTuyenDAO;
import com.example.nhom7.model.DonUngTuyen;
import com.example.nhom7.tienich.QuanLyPhien;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Màn hình Quản lý Đơn ứng tuyển
 */
public class DonUngTuyenActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachDon;
    private TextView tvKhongCoDuLieu;
    private BottomNavigationView bottomNavigation;

    private UngTuyenDAO ungTuyenDAO;
    private QuanLyPhien quanLyPhien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_ung_tuyen);

        ungTuyenDAO = new UngTuyenDAO(this);
        quanLyPhien = new QuanLyPhien(this);

        anhXaGiaoDien();
        taiDanhSachDon();
        caiDatBottomNavigation();
    }

    private void anhXaGiaoDien() {
        rvDanhSachDon = findViewById(R.id.rvDanhSachDon);
        tvKhongCoDuLieu = findViewById(R.id.tvKhongCoDuLieu);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        rvDanhSachDon.setLayoutManager(new LinearLayoutManager(this));
    }

    private void taiDanhSachDon() {
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId == -1) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvDanhSachDon.setVisibility(View.GONE);
            return;
        }

        List<DonUngTuyen> list = ungTuyenDAO.layDanhSachDonUngTuyenTheoSinhVien(sinhVienId);
        if (list == null || list.isEmpty()) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvDanhSachDon.setVisibility(View.GONE);
        } else {
            tvKhongCoDuLieu.setVisibility(View.GONE);
            rvDanhSachDon.setVisibility(View.VISIBLE);

            DonUngTuyenAdapter adapter = new DonUngTuyenAdapter(this, list, this::xacNhanHuyDon);
            rvDanhSachDon.setAdapter(adapter);
        }
    }

    private void xacNhanHuyDon(DonUngTuyen don) {
        new AlertDialog.Builder(this)
                .setTitle("Hủy đơn ứng tuyển")
                .setMessage("Bạn có chắc chắn muốn hủy đơn ứng tuyển công việc '" + don.getTenViecLam() + "' không?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    boolean res = ungTuyenDAO.huyDonUngTuyen(don.getDonUngTuyenId());
                    if (res) {
                        Toast.makeText(this, "Đã hủy đơn ứng tuyển!", Toast.LENGTH_SHORT).show();
                        taiDanhSachDon();
                    } else {
                        Toast.makeText(this, "Lỗi khi hủy đơn!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Quay lại", null)
                .show();
    }

    private void caiDatBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.menu_don_ung_tuyen);
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
                return true;
            } else if (itemId == R.id.menu_ho_so) {
                startActivity(new Intent(this, HoSoCaNhanActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        taiDanhSachDon();
        bottomNavigation.setSelectedItemId(R.id.menu_don_ung_tuyen);
    }
}
