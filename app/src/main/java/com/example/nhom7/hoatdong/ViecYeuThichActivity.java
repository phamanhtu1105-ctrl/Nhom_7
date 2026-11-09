package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.adapter.ViecYeuThichAdapter;
import com.example.nhom7.database.YeuThichDAO;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.model.ViecYeuThich;
import com.example.nhom7.tienich.QuanLyPhien;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * Màn hình Quản lý Công việc yêu thích
 */
public class ViecYeuThichActivity extends AppCompatActivity {

    private RecyclerView rvDanhSachYeuThich;
    private TextView tvKhongCoDuLieu;
    private BottomNavigationView bottomNavigation;

    private YeuThichDAO yeuThichDAO;
    private QuanLyPhien quanLyPhien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viec_yeu_thich);

        yeuThichDAO = new YeuThichDAO(this);
        quanLyPhien = new QuanLyPhien(this);

        anhXaGiaoDien();
        taiDanhSachYeuThich();
        caiDatBottomNavigation();
    }

    private void anhXaGiaoDien() {
        rvDanhSachYeuThich = findViewById(R.id.rvDanhSachYeuThich);
        tvKhongCoDuLieu = findViewById(R.id.tvKhongCoDuLieu);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        rvDanhSachYeuThich.setLayoutManager(new LinearLayoutManager(this));
    }

    private void taiDanhSachYeuThich() {
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId < 0) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvDanhSachYeuThich.setVisibility(View.GONE);
            return;
        }

        List<ViecYeuThich> list = yeuThichDAO.layDanhSachYeuThichTheoSinhVien(sinhVienId);

        if (list == null || list.isEmpty()) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvDanhSachYeuThich.setVisibility(View.GONE);
        } else {
            tvKhongCoDuLieu.setVisibility(View.GONE);
            rvDanhSachYeuThich.setVisibility(View.VISIBLE);

            ViecYeuThichAdapter adapter = new ViecYeuThichAdapter(this, list, new ViecYeuThichAdapter.OnYeuThichActionListener() {
                @Override
                public void onItemClick(ViecLam viecLam) {
                    Intent intent = new Intent(ViecYeuThichActivity.this, ChiTietViecActivity.class);
                    intent.putExtra("viecLamId", viecLam.getViecLamId());
                    startActivity(intent);
                }

                @Override
                public void onXoaClick(ViecYeuThich viecYeuThich) {
                    boolean success = yeuThichDAO.xoaViecYeuThich(sinhVienId, viecYeuThich.getViecLamId());
                    if (success) {
                        Toast.makeText(ViecYeuThichActivity.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        taiDanhSachYeuThich();
                    } else {
                        Toast.makeText(ViecYeuThichActivity.this, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            rvDanhSachYeuThich.setAdapter(adapter);
        }
    }

    private void caiDatBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.menu_yeu_thich);
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
                return true;
            } else if (itemId == R.id.menu_don_ung_tuyen) {
                startActivity(new Intent(this, DonUngTuyenActivity.class));
                finish();
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
        taiDanhSachYeuThich();
        bottomNavigation.setSelectedItemId(R.id.menu_yeu_thich);
    }
}
