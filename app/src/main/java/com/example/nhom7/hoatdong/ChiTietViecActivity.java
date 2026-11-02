package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom7.R;
import com.example.nhom7.database.UngTuyenDAO;
import com.example.nhom7.database.ViecLamDAO;
import com.example.nhom7.database.YeuThichDAO;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.tienich.QuanLyPhien;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Màn hình Chi tiết công việc
 */
public class ChiTietViecActivity extends AppCompatActivity {

    private ImageView btnQuayLai, btnYeuThich;
    private TextView tvTenViecLam, tvTenNhaTuyenDung, tvMucLuong, tvDiaDiem, tvThoiGianLamViec, tvHinhThucLamViec, tvHanUngTuyen;
    private TextView tvMoTa, tvYeuCau, tvQuyenLoi;
    private Button btnUngTuyen;

    private ViecLamDAO viecLamDAO;
    private YeuThichDAO yeuThichDAO;
    private UngTuyenDAO ungTuyenDAO;
    private QuanLyPhien quanLyPhien;

    private int viecLamId = -1;
    private ViecLam viecLamCurrent;
    private boolean isYeuThich = false;
    private final DecimalFormat dinhDangTien = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_viec);

        viecLamDAO = new ViecLamDAO(this);
        yeuThichDAO = new YeuThichDAO(this);
        ungTuyenDAO = new UngTuyenDAO(this);
        quanLyPhien = new QuanLyPhien(this);

        viecLamId = getIntent().getIntExtra("viecLamId", -1);
        if (viecLamId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin công việc!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        anhXaGiaoDien();
        taiThongTinChiTiet();
        caiDatSuKien();
    }

    private void anhXaGiaoDien() {
        btnQuayLai = findViewById(R.id.btnQuayLai);
        btnYeuThich = findViewById(R.id.btnYeuThich);
        tvTenViecLam = findViewById(R.id.tvTenViecLam);
        tvTenNhaTuyenDung = findViewById(R.id.tvTenNhaTuyenDung);
        tvMucLuong = findViewById(R.id.tvMucLuong);
        tvDiaDiem = findViewById(R.id.tvDiaDiem);
        tvThoiGianLamViec = findViewById(R.id.tvThoiGianLamViec);
        tvHinhThucLamViec = findViewById(R.id.tvHinhThucLamViec);
        tvHanUngTuyen = findViewById(R.id.tvHanUngTuyen);
        tvMoTa = findViewById(R.id.tvMoTa);
        tvYeuCau = findViewById(R.id.tvYeuCau);
        tvQuyenLoi = findViewById(R.id.tvQuyenLoi);
        btnUngTuyen = findViewById(R.id.btnUngTuyen);
    }

    private void taiThongTinChiTiet() {
        viecLamCurrent = viecLamDAO.layViecLamTheoId(viecLamId);
        if (viecLamCurrent == null) {
            Toast.makeText(this, "Lỗi tải dữ liệu công việc!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTenViecLam.setText(viecLamCurrent.getTenViecLam());
        tvTenNhaTuyenDung.setText(viecLamCurrent.getTenNhaTuyenDung() != null ? viecLamCurrent.getTenNhaTuyenDung() : "Nhà tuyển dụng");

        String luongText = "Mức lương: " + dinhDangTien.format(viecLamCurrent.getMucLuong()) + " VNĐ/giờ";
        tvMucLuong.setText(luongText);

        tvDiaDiem.setText("Địa điểm: " + (viecLamCurrent.getDiaDiem() != null ? viecLamCurrent.getDiaDiem() : "Chưa cập nhật"));
        tvThoiGianLamViec.setText("Thời gian: " + (viecLamCurrent.getThoiGianLamViec() != null ? viecLamCurrent.getThoiGianLamViec() : "Linh hoạt"));
        tvHinhThucLamViec.setText("Hình thức: " + (viecLamCurrent.getHinhThucLamViec() != null ? viecLamCurrent.getHinhThucLamViec() : "Bán thời gian"));
        tvHanUngTuyen.setText("Hạn ứng tuyển: " + (viecLamCurrent.getHanUngTuyen() != null ? viecLamCurrent.getHanUngTuyen() : "Đang tuyển"));

        tvMoTa.setText(viecLamCurrent.getMoTa() != null ? viecLamCurrent.getMoTa() : "Chưa có mô tả.");
        tvYeuCau.setText(viecLamCurrent.getYeuCau() != null ? viecLamCurrent.getYeuCau() : "Không yêu cầu đặc biệt.");
        tvQuyenLoi.setText(viecLamCurrent.getQuyenLoi() != null ? viecLamCurrent.getQuyenLoi() : "Theo chính sách công ty.");

        // Kiểm tra xem sinh viên hiện tại đã lưu công việc này chưa
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId != -1) {
            isYeuThich = yeuThichDAO.kiemTraYeuThich(sinhVienId, viecLamId);
            capNhatGiaoDienYeuThich();
        }
    }

    private void capNhatGiaoDienYeuThich() {
        if (isYeuThich) {
            btnYeuThich.setColorFilter(Color.RED);
        } else {
            btnYeuThich.setColorFilter(Color.WHITE);
        }
    }

    private void caiDatSuKien() {
        btnQuayLai.setOnClickListener(v -> finish());

        btnYeuThich.setOnClickListener(v -> xuLyYeuThich());

        btnUngTuyen.setOnClickListener(v -> xuLyUngTuyen());
    }

    private void xuLyYeuThich() {
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để lưu công việc!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isYeuThich) {
            boolean success = yeuThichDAO.xoaViecYeuThich(sinhVienId, viecLamId);
            if (success) {
                isYeuThich = false;
                capNhatGiaoDienYeuThich();
                Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }
        } else {
            String ngayThem = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            long result = yeuThichDAO.themViecYeuThich(sinhVienId, viecLamId, ngayThem);
            if (result > 0) {
                isYeuThich = true;
                capNhatGiaoDienYeuThich();
                Toast.makeText(this, "Đã lưu vào danh sách công việc yêu thích!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void xuLyUngTuyen() {
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để ứng tuyển!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra xem đã nộp đơn cho công việc này chưa
        boolean daUngTuyen = ungTuyenDAO.kiemTraDaUngTuyen(sinhVienId, viecLamId);
        if (daUngTuyen) {
            Toast.makeText(this, "Bạn đã nộp đơn ứng tuyển công việc này rồi!", Toast.LENGTH_LONG).show();
            return;
        }

        // Chuyển sang màn hình nộp đơn
        Intent intent = new Intent(ChiTietViecActivity.this, UngTuyenActivity.class);
        intent.putExtra("viecLamId", viecLamId);
        startActivity(intent);
    }
}
