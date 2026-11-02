package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhom7.R;
import com.example.nhom7.database.UngTuyenDAO;
import com.example.nhom7.database.ViecLamDAO;
import com.example.nhom7.model.DonUngTuyen;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.tienich.KiemTraDuLieu;
import com.example.nhom7.tienich.QuanLyPhien;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Màn hình Nộp đơn ứng tuyển
 */
public class UngTuyenActivity extends AppCompatActivity {

    private ImageView btnQuayLai;
    private TextView tvTenViecLam, tvTenNhaTuyenDung, tvMucLuong;
    private EditText edtLoiGioiThieu;
    private Button btnGuiDon;

    private ViecLamDAO viecLamDAO;
    private UngTuyenDAO ungTuyenDAO;
    private QuanLyPhien quanLyPhien;

    private int viecLamId = -1;
    private final DecimalFormat dinhDangTien = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ung_tuyen);

        viecLamDAO = new ViecLamDAO(this);
        ungTuyenDAO = new UngTuyenDAO(this);
        quanLyPhien = new QuanLyPhien(this);

        viecLamId = getIntent().getIntExtra("viecLamId", -1);
        if (viecLamId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin công việc!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        anhXaGiaoDien();
        hienThiThongTinViec();
        caiDatSuKien();
    }

    private void anhXaGiaoDien() {
        btnQuayLai = findViewById(R.id.btnQuayLai);
        tvTenViecLam = findViewById(R.id.tvTenViecLam);
        tvTenNhaTuyenDung = findViewById(R.id.tvTenNhaTuyenDung);
        tvMucLuong = findViewById(R.id.tvMucLuong);
        edtLoiGioiThieu = findViewById(R.id.edtLoiGioiThieu);
        btnGuiDon = findViewById(R.id.btnGuiDon);
    }

    private void hienThiThongTinViec() {
        ViecLam vl = viecLamDAO.layViecLamTheoId(viecLamId);
        if (vl != null) {
            tvTenViecLam.setText(vl.getTenViecLam());
            tvTenNhaTuyenDung.setText(vl.getTenNhaTuyenDung() != null ? vl.getTenNhaTuyenDung() : "Nhà tuyển dụng");
            tvMucLuong.setText("Mức lương: " + dinhDangTien.format(vl.getMucLuong()) + " VNĐ/giờ");
        }
    }

    private void caiDatSuKien() {
        btnQuayLai.setOnClickListener(v -> finish());

        btnGuiDon.setOnClickListener(v -> xuLyGuiDon());
    }

    private void xuLyGuiDon() {
        int sinhVienId = quanLyPhien.laySinhVienId();
        if (sinhVienId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập để gửi đơn!", Toast.LENGTH_SHORT).show();
            return;
        }

        String loiGioiThieu = edtLoiGioiThieu.getText().toString().trim();
        if (KiemTraDuLieu.kiemTraRong(loiGioiThieu)) {
            edtLoiGioiThieu.setError("Vui lòng nhập lời giới thiệu bản thân");
            edtLoiGioiThieu.requestFocus();
            return;
        }

        String ngayUngTuyen = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        DonUngTuyen don = new DonUngTuyen(sinhVienId, viecLamId, loiGioiThieu, ngayUngTuyen, "Chờ xét duyệt");

        long res = ungTuyenDAO.themDonUngTuyen(don);
        if (res > 0) {
            Toast.makeText(this, "Nộp đơn ứng tuyển thành công!", Toast.LENGTH_LONG).show();

            // Chuyển sang danh sách Đơn ứng tuyển
            Intent intent = new Intent(UngTuyenActivity.this, DonUngTuyenActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Nộp đơn thất bại hoặc bạn đã nộp đơn công việc này rồi!", Toast.LENGTH_LONG).show();
        }
    }
}
