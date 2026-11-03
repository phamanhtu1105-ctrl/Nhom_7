package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.adapter.DonUngTuyenAdminAdapter;
import com.example.nhom7.database.UngTuyenDAO;
import com.example.nhom7.database.ViecLamDAO;
import com.example.nhom7.model.DonUngTuyen;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.tienich.KiemTraDuLieu;
import com.example.nhom7.tienich.QuanLyPhien;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình Quản trị viên (ADMIN)
 */
public class AdminActivity extends AppCompatActivity {

    private TextView tvTongDon, tvTongViec, tvKhongCoDonAdmin;
    private Button btnDangXuatAdmin, btnThemViecLam;
    private RecyclerView rvDonUngTuyenAdmin;

    private UngTuyenDAO ungTuyenDAO;
    private ViecLamDAO viecLamDAO;
    private QuanLyPhien quanLyPhien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quanLyPhien = new QuanLyPhien(this);

        // Kiểm tra quyền Admin
        if (!quanLyPhien.daDangNhap() || !quanLyPhien.laAdmin()) {
            Toast.makeText(this, "Truy cập bị từ chối! Cần quyền Admin.", Toast.LENGTH_SHORT).show();
            chuyenSangDangNhap();
            return;
        }

        setContentView(R.layout.activity_admin);

        ungTuyenDAO = new UngTuyenDAO(this);
        viecLamDAO = new ViecLamDAO(this);

        anhXaGiaoDien();
        taiDuLieuAdmin();
        caiDatSuKien();
    }

    private void anhXaGiaoDien() {
        tvTongDon = findViewById(R.id.tvTongDon);
        tvTongViec = findViewById(R.id.tvTongViec);
        tvKhongCoDonAdmin = findViewById(R.id.tvKhongCoDonAdmin);
        btnDangXuatAdmin = findViewById(R.id.btnDangXuatAdmin);
        btnThemViecLam = findViewById(R.id.btnThemViecLam);
        rvDonUngTuyenAdmin = findViewById(R.id.rvDonUngTuyenAdmin);

        rvDonUngTuyenAdmin.setLayoutManager(new LinearLayoutManager(this));
    }

    private void taiDuLieuAdmin() {
        // 1. Thống kê tổng số việc
        List<ViecLam> dsViec = viecLamDAO.layTatCaViecLam();
        int tongViec = dsViec != null ? dsViec.size() : 0;
        tvTongViec.setText(String.format(Locale.getDefault(), "%d", tongViec));

        // 2. Thống kê & Danh sách tất cả đơn ứng tuyển
        List<DonUngTuyen> dsDon = ungTuyenDAO.layTatCaDonUngTuyen();
        int tongDon = dsDon != null ? dsDon.size() : 0;
        tvTongDon.setText(String.format(Locale.getDefault(), "%d", tongDon));

        if (dsDon == null || dsDon.isEmpty()) {
            tvKhongCoDonAdmin.setVisibility(View.VISIBLE);
            rvDonUngTuyenAdmin.setVisibility(View.GONE);
        } else {
            tvKhongCoDonAdmin.setVisibility(View.GONE);
            rvDonUngTuyenAdmin.setVisibility(View.VISIBLE);

            DonUngTuyenAdminAdapter adapter = new DonUngTuyenAdminAdapter(this, dsDon, new DonUngTuyenAdminAdapter.OnAdminDuyetListener() {
                @Override
                public void onChapNhan(DonUngTuyen don) {
                    duyetDon(don.getDonUngTuyenId(), "Đã chấp nhận");
                }

                @Override
                public void onTuChoi(DonUngTuyen don) {
                    duyetDon(don.getDonUngTuyenId(), "Đã từ chối");
                }
            });
            rvDonUngTuyenAdmin.setAdapter(adapter);
        }
    }

    private void duyetDon(int donUngTuyenId, String trangThaiMoi) {
        boolean success = ungTuyenDAO.capNhatTrangThaiDon(donUngTuyenId, trangThaiMoi);
        if (success) {
            Toast.makeText(this, "Đã duyệt đơn: " + trangThaiMoi, Toast.LENGTH_SHORT).show();
            taiDuLieuAdmin();
        } else {
            Toast.makeText(this, "Cập nhật đơn thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void caiDatSuKien() {
        btnDangXuatAdmin.setOnClickListener(v -> {
            quanLyPhien.dangXuat();
            Toast.makeText(this, "Đã đăng xuất tài khoản Admin!", Toast.LENGTH_SHORT).show();
            chuyenSangDangNhap();
        });

        btnThemViecLam.setOnClickListener(v -> hienThiDialogThemViecLam());
    }

    private void hienThiDialogThemViecLam() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_them_viec_lam, null);
        builder.setView(dialogView);

        EditText edtTenViec = dialogView.findViewById(R.id.edtTenViec);
        EditText edtLuong = dialogView.findViewById(R.id.edtLuong);
        EditText edtDiaDiemViec = dialogView.findViewById(R.id.edtDiaDiemViec);
        EditText edtThoiGianViec = dialogView.findViewById(R.id.edtThoiGianViec);
        EditText edtMoTaViec = dialogView.findViewById(R.id.edtMoTaViec);
        EditText edtYeuCauViec = dialogView.findViewById(R.id.edtYeuCauViec);
        EditText edtQuyenLoiViec = dialogView.findViewById(R.id.edtQuyenLoiViec);

        builder.setPositiveButton("Thêm công việc", (dialog, which) -> {
            String tenViec = edtTenViec.getText().toString().trim();
            String luongStr = edtLuong.getText().toString().trim();
            String diaDiem = edtDiaDiemViec.getText().toString().trim();
            String thoiGian = edtThoiGianViec.getText().toString().trim();
            String moTa = edtMoTaViec.getText().toString().trim();
            String yeuCau = edtYeuCauViec.getText().toString().trim();
            String quyenLoi = edtQuyenLoiViec.getText().toString().trim();

            if (KiemTraDuLieu.kiemTraRong(tenViec) || KiemTraDuLieu.kiemTraRong(luongStr) || KiemTraDuLieu.kiemTraRong(diaDiem)) {
                Toast.makeText(AdminActivity.this, "Vui lòng điền đủ Tên việc, Mức lương và Địa điểm!", Toast.LENGTH_SHORT).show();
                return;
            }

            double luong = parseDoubleSafe(luongStr);

            String ngayDang = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            ViecLam vl = new ViecLam();
            vl.setNhaTuyenDungId(1);
            vl.setDanhMucId(1);
            vl.setTenViecLam(tenViec);
            vl.setMucLuong(luong);
            vl.setDiaDiem(diaDiem);
            vl.setThoiGianLamViec(!thoiGian.isEmpty() ? thoiGian : "Ca linh hoạt");
            vl.setHinhThucLamViec("Bán thời gian");
            vl.setMoTa(!moTa.isEmpty() ? moTa : "Chưa có mô tả.");
            vl.setYeuCau(!yeuCau.isEmpty() ? yeuCau : "Không yêu cầu.");
            vl.setQuyenLoi(!quyenLoi.isEmpty() ? quyenLoi : "Chế độ đầy đủ.");
            vl.setHanUngTuyen("31/12/2026");
            vl.setNgayDang(ngayDang);

            long res = viecLamDAO.themViecLam(vl);
            if (res > 0) {
                Toast.makeText(AdminActivity.this, "Đã thêm công việc thành công!", Toast.LENGTH_SHORT).show();
                taiDuLieuAdmin();
            } else {
                Toast.makeText(AdminActivity.this, "Lỗi khi thêm công việc!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void chuyenSangDangNhap() {
        Intent intent = new Intent(AdminActivity.this, DangNhapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private double parseDoubleSafe(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
