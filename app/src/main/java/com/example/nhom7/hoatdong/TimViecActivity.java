package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.adapter.ViecLamAdapter;
import com.example.nhom7.database.ViecLamDAO;
import com.example.nhom7.model.DanhMuc;
import com.example.nhom7.model.ViecLam;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Màn hình Tìm kiếm, Lọc và Sắp xếp công việc
 */
public class TimViecActivity extends AppCompatActivity {

    private EditText edtTimKiem;
    private ImageView btnTimKiem;
    private Spinner spnDanhMuc, spnDiaDiem, spnSapXep;
    private RecyclerView rvDanhSachViecLam;
    private TextView tvKhongCoDuLieu;
    private BottomNavigationView bottomNavigation;

    private ViecLamDAO viecLamDAO;
    private List<DanhMuc> danhSachDanhMuc = new ArrayList<>();
    private final List<String> danhSachDiaDiem = new ArrayList<>();
    private final List<String> danhSachSapXep = new ArrayList<>();

    private int selectedDanhMucId = 0;
    private String selectedDiaDiem = "Tất cả";
    private String selectedSapXepKey = "moi_nhat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_viec);

        viecLamDAO = new ViecLamDAO(this);

        anhXaGiaoDien();
        khoiTaoBoLoc();

        nhanDuLieuTuIntent();
        thucHienTimKiemVaLoc();

        caiDatSuKien();
        caiDatBottomNavigation();
    }

    private void anhXaGiaoDien() {
        edtTimKiem = findViewById(R.id.edtTimKiem);
        btnTimKiem = findViewById(R.id.btnTimKiem);
        spnDanhMuc = findViewById(R.id.spnDanhMuc);
        spnDiaDiem = findViewById(R.id.spnDiaDiem);
        spnSapXep = findViewById(R.id.spnSapXep);
        rvDanhSachViecLam = findViewById(R.id.rvDanhSachViecLam);
        tvKhongCoDuLieu = findViewById(R.id.tvKhongCoDuLieu);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        rvDanhSachViecLam.setLayoutManager(new LinearLayoutManager(this));
    }

    private void khoiTaoBoLoc() {
        // 1. Danh mục
        danhSachDanhMuc = viecLamDAO.layDanhSachDanhMuc();
        List<String> tenDanhMucList = new ArrayList<>();
        tenDanhMucList.add("Tất cả danh mục");
        for (DanhMuc dm : danhSachDanhMuc) {
            tenDanhMucList.add(dm.getTenDanhMuc());
        }
        ArrayAdapter<String> adapterDanhMuc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tenDanhMucList);
        adapterDanhMuc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDanhMuc.setAdapter(adapterDanhMuc);

        // 2. Địa điểm
        danhSachDiaDiem.add("Tất cả địa điểm");
        danhSachDiaDiem.add("Quận 1");
        danhSachDiaDiem.add("Quận 3");
        danhSachDiaDiem.add("Quận 10");
        danhSachDiaDiem.add("Bình Thạnh");
        danhSachDiaDiem.add("Online");
        ArrayAdapter<String> adapterDiaDiem = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachDiaDiem);
        adapterDiaDiem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDiaDiem.setAdapter(adapterDiaDiem);

        // 3. Sắp xếp
        danhSachSapXep.add("Mới nhất");
        danhSachSapXep.add("Lương tăng dần");
        danhSachSapXep.add("Lương giảm dần");
        ArrayAdapter<String> adapterSapXep = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachSapXep);
        adapterSapXep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSapXep.setAdapter(adapterSapXep);
    }

    private void nhanDuLieuTuIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("danhMucId")) {
                selectedDanhMucId = intent.getIntExtra("danhMucId", 0);
                for (DanhMuc dm : danhSachDanhMuc) {
                    if (dm.getDanhMucId() == selectedDanhMucId) {
                        spnDanhMuc.setSelection(danhSachDanhMuc.indexOf(dm) + 1);
                        break;
                    }
                }
            }
            if (intent.hasExtra("tuKhoaTimKiem")) {
                String tuKhoa = intent.getStringExtra("tuKhoaTimKiem");
                if (tuKhoa != null) {
                    edtTimKiem.setText(tuKhoa);
                }
            }
        }
    }

    private void caiDatSuKien() {
        btnTimKiem.setOnClickListener(v -> thucHienTimKiemVaLoc());

        spnDanhMuc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDanhMucId = (position == 0) ? 0 : danhSachDanhMuc.get(position - 1).getDanhMucId();
                thucHienTimKiemVaLoc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spnDiaDiem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDiaDiem = (position == 0) ? "Tất cả" : danhSachDiaDiem.get(position);
                thucHienTimKiemVaLoc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spnSapXep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        selectedSapXepKey = "luong_tang";
                        break;
                    case 2:
                        selectedSapXepKey = "luong_giam";
                        break;
                    default:
                        selectedSapXepKey = "moi_nhat";
                        break;
                }
                thucHienTimKiemVaLoc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void thucHienTimKiemVaLoc() {
        String tuKhoa = edtTimKiem.getText().toString().trim();
        List<ViecLam> list = viecLamDAO.timKiemVaLocViecLam(tuKhoa, selectedDanhMucId, selectedDiaDiem, 0.0, selectedSapXepKey);

        if (list == null || list.isEmpty()) {
            tvKhongCoDuLieu.setVisibility(View.VISIBLE);
            rvDanhSachViecLam.setVisibility(View.GONE);
        } else {
            tvKhongCoDuLieu.setVisibility(View.GONE);
            rvDanhSachViecLam.setVisibility(View.VISIBLE);

            ViecLamAdapter adapter = new ViecLamAdapter(this, list, viecLam -> {
                Intent intent = new Intent(TimViecActivity.this, ChiTietViecActivity.class);
                intent.putExtra("viecLamId", viecLam.getViecLamId());
                startActivity(intent);
            });
            rvDanhSachViecLam.setAdapter(adapter);
        }
    }

    private void caiDatBottomNavigation() {
        bottomNavigation.setSelectedItemId(R.id.menu_tim_viec);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_trang_chu) {
                startActivity(new Intent(this, TrangChuActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_tim_viec) {
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
        bottomNavigation.setSelectedItemId(R.id.menu_tim_viec);
    }
}
