package com.example.nhom7.hoatdong;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.adapter.DonUngTuyenAdminAdapter;
import com.example.nhom7.adapter.SinhVienAdminAdapter;
import com.example.nhom7.adapter.ViecLamAdminAdapter;
import com.example.nhom7.database.NhaTuyenDungDAO;
import com.example.nhom7.database.SinhVienDAO;
import com.example.nhom7.database.UngTuyenDAO;
import com.example.nhom7.database.ViecLamDAO;
import com.example.nhom7.model.DanhMuc;
import com.example.nhom7.model.DonUngTuyen;
import com.example.nhom7.model.NhaTuyenDung;
import com.example.nhom7.model.SinhVien;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.tienich.KiemTraDuLieu;
import com.example.nhom7.tienich.QuanLyPhien;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Màn hình Bảng Quản trị hệ thống (ADMIN DASHBOARD)
 */
public class AdminActivity extends AppCompatActivity {

    // Thống kê
    private TextView tvTongDon, tvDonChoDuyet, tvTongViec, tvTongSinhVien;
    private Button btnDangXuatAdmin;

    // Tabs
    private Button btnTabDuyetDon, btnTabCongViec, btnTabSinhVien;
    private LinearLayout layoutTabDuyetDon, layoutTabCongViec, layoutTabSinhVien;

    // Tab 1: Duyệt Đơn
    private Button btnFilterTatCa, btnFilterChoDuyet, btnFilterChapNhan, btnFilterTuChoi;
    private RecyclerView rvDonUngTuyenAdmin;
    private TextView tvKhongCoDonAdmin;
    private DonUngTuyenAdminAdapter donUngTuyenAdapter;
    private String filterTrangThaiHienTai = "Tất cả";

    // Tab 2: Quản lý Công việc
    private Button btnThemViecLam;
    private EditText edtTimKiemCongViecAdmin;
    private RecyclerView rvViecLamAdmin;
    private TextView tvKhongCoViecAdmin;
    private ViecLamAdminAdapter viecLamAdapter;
    private List<ViecLam> dsViecLamGoc = new ArrayList<>();

    // Tab 3: Quản lý Sinh viên
    private EditText edtTimKiemSinhVienAdmin;
    private RecyclerView rvSinhVienAdmin;
    private TextView tvKhongCoSinhVienAdmin;
    private SinhVienAdminAdapter sinhVienAdapter;
    private List<SinhVien> dsSinhVienGoc = new ArrayList<>();

    // DAO & Sessions
    private UngTuyenDAO ungTuyenDAO;
    private ViecLamDAO viecLamDAO;
    private SinhVienDAO sinhVienDAO;
    private NhaTuyenDungDAO nhaTuyenDungDAO;
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
        sinhVienDAO = new SinhVienDAO(this);
        nhaTuyenDungDAO = new NhaTuyenDungDAO(this);

        anhXaGiaoDien();
        caiDatSuKien();
        taiTatCaDuLieu();
        chuyenTab("DUYET_DON");
    }

    private void anhXaGiaoDien() {
        // Stats
        tvTongDon = findViewById(R.id.tvTongDon);
        tvDonChoDuyet = findViewById(R.id.tvDonChoDuyet);
        tvTongViec = findViewById(R.id.tvTongViec);
        tvTongSinhVien = findViewById(R.id.tvTongSinhVien);
        btnDangXuatAdmin = findViewById(R.id.btnDangXuatAdmin);

        // Tabs
        btnTabDuyetDon = findViewById(R.id.btnTabDuyetDon);
        btnTabCongViec = findViewById(R.id.btnTabCongViec);
        btnTabSinhVien = findViewById(R.id.btnTabSinhVien);

        layoutTabDuyetDon = findViewById(R.id.layoutTabDuyetDon);
        layoutTabCongViec = findViewById(R.id.layoutTabCongViec);
        layoutTabSinhVien = findViewById(R.id.layoutTabSinhVien);

        // Tab 1 Views
        btnFilterTatCa = findViewById(R.id.btnFilterTatCa);
        btnFilterChoDuyet = findViewById(R.id.btnFilterChoDuyet);
        btnFilterChapNhan = findViewById(R.id.btnFilterChapNhan);
        btnFilterTuChoi = findViewById(R.id.btnFilterTuChoi);
        rvDonUngTuyenAdmin = findViewById(R.id.rvDonUngTuyenAdmin);
        tvKhongCoDonAdmin = findViewById(R.id.tvKhongCoDonAdmin);
        rvDonUngTuyenAdmin.setLayoutManager(new LinearLayoutManager(this));

        // Tab 2 Views
        btnThemViecLam = findViewById(R.id.btnThemViecLam);
        edtTimKiemCongViecAdmin = findViewById(R.id.edtTimKiemCongViecAdmin);
        rvViecLamAdmin = findViewById(R.id.rvViecLamAdmin);
        tvKhongCoViecAdmin = findViewById(R.id.tvKhongCoViecAdmin);
        rvViecLamAdmin.setLayoutManager(new LinearLayoutManager(this));

        // Tab 3 Views
        edtTimKiemSinhVienAdmin = findViewById(R.id.edtTimKiemSinhVienAdmin);
        rvSinhVienAdmin = findViewById(R.id.rvSinhVienAdmin);
        tvKhongCoSinhVienAdmin = findViewById(R.id.tvKhongCoSinhVienAdmin);
        rvSinhVienAdmin.setLayoutManager(new LinearLayoutManager(this));
    }

    private void caiDatSuKien() {
        // Đăng xuất
        btnDangXuatAdmin.setOnClickListener(v -> {
            quanLyPhien.dangXuat();
            Toast.makeText(this, "Đã đăng xuất tài khoản Admin!", Toast.LENGTH_SHORT).show();
            chuyenSangDangNhap();
        });

        // Tab navigation
        btnTabDuyetDon.setOnClickListener(v -> chuyenTab("DUYET_DON"));
        btnTabCongViec.setOnClickListener(v -> chuyenTab("CONG_VIEC"));
        btnTabSinhVien.setOnClickListener(v -> chuyenTab("SINH_VIEN"));

        // Filter chips Tab 1
        btnFilterTatCa.setOnClickListener(v -> locDonTheoTrangThai("Tất cả"));
        btnFilterChoDuyet.setOnClickListener(v -> locDonTheoTrangThai("Chờ xét duyệt"));
        btnFilterChapNhan.setOnClickListener(v -> locDonTheoTrangThai("Đã chấp nhận"));
        btnFilterTuChoi.setOnClickListener(v -> locDonTheoTrangThai("Đã từ chối"));

        // Thêm công việc
        btnThemViecLam.setOnClickListener(v -> hienThiDialogThemHoacSuaViecLam(null));

        // Search công việc
        edtTimKiemCongViecAdmin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locDanhSachViecLam(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Search sinh viên
        edtTimKiemSinhVienAdmin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locDanhSachSinhVien(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void chuyenTab(String tenTab) {
        int colorActive = getResources().getColor(R.color.mau_chu_dao, getTheme());
        int colorInactive = Color.TRANSPARENT;
        int textActive = Color.WHITE;
        int textInactive = getResources().getColor(R.color.mau_chu_chinh, getTheme());

        if ("DUYET_DON".equals(tenTab)) {
            layoutTabDuyetDon.setVisibility(View.VISIBLE);
            layoutTabCongViec.setVisibility(View.GONE);
            layoutTabSinhVien.setVisibility(View.GONE);

            btnTabDuyetDon.setBackgroundColor(colorActive);
            btnTabDuyetDon.setTextColor(textActive);
            btnTabCongViec.setBackgroundColor(colorInactive);
            btnTabCongViec.setTextColor(textInactive);
            btnTabSinhVien.setBackgroundColor(colorInactive);
            btnTabSinhVien.setTextColor(textInactive);

        } else if ("CONG_VIEC".equals(tenTab)) {
            layoutTabDuyetDon.setVisibility(View.GONE);
            layoutTabCongViec.setVisibility(View.VISIBLE);
            layoutTabSinhVien.setVisibility(View.GONE);

            btnTabDuyetDon.setBackgroundColor(colorInactive);
            btnTabDuyetDon.setTextColor(textInactive);
            btnTabCongViec.setBackgroundColor(colorActive);
            btnTabCongViec.setTextColor(textActive);
            btnTabSinhVien.setBackgroundColor(colorInactive);
            btnTabSinhVien.setTextColor(textInactive);

        } else if ("SINH_VIEN".equals(tenTab)) {
            layoutTabDuyetDon.setVisibility(View.GONE);
            layoutTabCongViec.setVisibility(View.GONE);
            layoutTabSinhVien.setVisibility(View.VISIBLE);

            btnTabDuyetDon.setBackgroundColor(colorInactive);
            btnTabDuyetDon.setTextColor(textInactive);
            btnTabCongViec.setBackgroundColor(colorInactive);
            btnTabCongViec.setTextColor(textInactive);
            btnTabSinhVien.setBackgroundColor(colorActive);
            btnTabSinhVien.setTextColor(textActive);
        }
    }

    private void taiTatCaDuLieu() {
        taiDuLieuThongKe();
        taiDuLieuDonUngTuyen();
        taiDuLieuViecLam();
        taiDuLieuSinhVien();
    }

    private void taiDuLieuThongKe() {
        int tongDon = ungTuyenDAO.layTatCaDonUngTuyen().size();
        int donChoDuyet = ungTuyenDAO.demDonChoDuyet();
        int tongViec = viecLamDAO.layTatCaViecLam().size();
        int tongSV = sinhVienDAO.demTongSinhVien();

        tvTongDon.setText(String.valueOf(tongDon));
        tvDonChoDuyet.setText(String.valueOf(donChoDuyet));
        tvTongViec.setText(String.valueOf(tongViec));
        tvTongSinhVien.setText(String.valueOf(tongSV));
    }

    // TAB 1: DUYỆT ĐƠN ỨNG TUYỂN
    private void taiDuLieuDonUngTuyen() {
        locDonTheoTrangThai(filterTrangThaiHienTai);
    }

    private void locDonTheoTrangThai(String trangThai) {
        filterTrangThaiHienTai = trangThai;

        // Cập nhật giao diện Chip Filter
        btnFilterTatCa.setAlpha("Tất cả".equals(trangThai) ? 1.0f : 0.6f);
        btnFilterChoDuyet.setAlpha("Chờ xét duyệt".equals(trangThai) ? 1.0f : 0.6f);
        btnFilterChapNhan.setAlpha("Đã chấp nhận".equals(trangThai) ? 1.0f : 0.6f);
        btnFilterTuChoi.setAlpha("Đã từ chối".equals(trangThai) ? 1.0f : 0.6f);

        List<DonUngTuyen> dsDon = ungTuyenDAO.layDonUngTuyenTheoTrangThai(trangThai);
        if (dsDon == null || dsDon.isEmpty()) {
            tvKhongCoDonAdmin.setVisibility(View.VISIBLE);
            rvDonUngTuyenAdmin.setVisibility(View.GONE);
        } else {
            tvKhongCoDonAdmin.setVisibility(View.GONE);
            rvDonUngTuyenAdmin.setVisibility(View.VISIBLE);

            if (donUngTuyenAdapter == null) {
                donUngTuyenAdapter = new DonUngTuyenAdminAdapter(this, dsDon, new DonUngTuyenAdminAdapter.OnAdminDuyetListener() {
                    @Override
                    public void onChapNhan(DonUngTuyen don) {
                        duyetDon(don.getDonUngTuyenId(), "Đã chấp nhận");
                    }

                    @Override
                    public void onTuChoi(DonUngTuyen don) {
                        duyetDon(don.getDonUngTuyenId(), "Đã từ chối");
                    }
                });
                rvDonUngTuyenAdmin.setAdapter(donUngTuyenAdapter);
            } else {
                donUngTuyenAdapter.capNhatDanhSach(dsDon);
            }
        }
    }

    private void duyetDon(int donUngTuyenId, String trangThaiMoi) {
        boolean success = ungTuyenDAO.capNhatTrangThaiDon(donUngTuyenId, trangThaiMoi);
        if (success) {
            Toast.makeText(this, "Đã cập nhật đơn: " + trangThaiMoi, Toast.LENGTH_SHORT).show();
            taiDuLieuThongKe();
            taiDuLieuDonUngTuyen();
        } else {
            Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    // TAB 2: QUẢN LÝ CÔNG VIỆC
    private void taiDuLieuViecLam() {
        dsViecLamGoc = viecLamDAO.layTatCaViecLam();
        hienThiDanhSachViecLam(dsViecLamGoc);
    }

    private void hienThiDanhSachViecLam(List<ViecLam> ds) {
        if (ds == null || ds.isEmpty()) {
            tvKhongCoViecAdmin.setVisibility(View.VISIBLE);
            rvViecLamAdmin.setVisibility(View.GONE);
        } else {
            tvKhongCoViecAdmin.setVisibility(View.GONE);
            rvViecLamAdmin.setVisibility(View.VISIBLE);

            if (viecLamAdapter == null) {
                viecLamAdapter = new ViecLamAdminAdapter(this, ds, new ViecLamAdminAdapter.OnViecLamAdminListener() {
                    @Override
                    public void onSuaViecLam(ViecLam viecLam) {
                        hienThiDialogThemHoacSuaViecLam(viecLam);
                    }

                    @Override
                    public void onXoaViecLam(ViecLam viecLam) {
                        xacNhanXoaViecLam(viecLam);
                    }
                });
                rvViecLamAdmin.setAdapter(viecLamAdapter);
            } else {
                viecLamAdapter.capNhatDanhSach(ds);
            }
        }
    }

    private void locDanhSachViecLam(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            hienThiDanhSachViecLam(dsViecLamGoc);
            return;
        }

        String key = tuKhoa.toLowerCase().trim();
        List<ViecLam> dsLoc = new ArrayList<>();
        for (ViecLam vl : dsViecLamGoc) {
            if ((vl.getTenViecLam() != null && vl.getTenViecLam().toLowerCase().contains(key)) ||
                    (vl.getTenNhaTuyenDung() != null && vl.getTenNhaTuyenDung().toLowerCase().contains(key)) ||
                    (vl.getDiaDiem() != null && vl.getDiaDiem().toLowerCase().contains(key))) {
                dsLoc.add(vl);
            }
        }
        hienThiDanhSachViecLam(dsLoc);
    }

    private void hienThiDialogThemHoacSuaViecLam(ViecLam viecLamCanSua) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_them_sua_viec_lam, null);
        builder.setView(dialogView);

        TextView tvTieuDe = dialogView.findViewById(R.id.tvTieuDeDialogViecLam);
        Spinner spnDanhMuc = dialogView.findViewById(R.id.spnDanhMucDialog);
        Spinner spnNhaTuyenDung = dialogView.findViewById(R.id.spnNhaTuyenDungDialog);
        EditText edtTenViec = dialogView.findViewById(R.id.edtTenViec);
        EditText edtLuong = dialogView.findViewById(R.id.edtLuong);
        EditText edtDiaDiemViec = dialogView.findViewById(R.id.edtDiaDiemViec);
        EditText edtThoiGianViec = dialogView.findViewById(R.id.edtThoiGianViec);
        EditText edtMoTaViec = dialogView.findViewById(R.id.edtMoTaViec);
        EditText edtYeuCauViec = dialogView.findViewById(R.id.edtYeuCauViec);
        EditText edtQuyenLoiViec = dialogView.findViewById(R.id.edtQuyenLoiViec);

        // Nạp Spinner Danh mục
        List<DanhMuc> dsDanhMuc = viecLamDAO.layDanhSachDanhMuc();
        List<String> tenDanhMucList = new ArrayList<>();
        for (DanhMuc dm : dsDanhMuc) tenDanhMucList.add(dm.getTenDanhMuc());
        ArrayAdapter<String> adapterDM = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tenDanhMucList);
        spnDanhMuc.setAdapter(adapterDM);

        // Nạp Spinner Nhà tuyển dụng
        List<NhaTuyenDung> dsNTD = nhaTuyenDungDAO.layDanhSachNhaTuyenDung();
        List<String> tenNTDList = new ArrayList<>();
        for (NhaTuyenDung ntd : dsNTD) tenNTDList.add(ntd.getTenNhaTuyenDung());
        ArrayAdapter<String> adapterNTD = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tenNTDList);
        spnNhaTuyenDung.setAdapter(adapterNTD);

        boolean laChinhSua = (viecLamCanSua != null);
        if (laChinhSua) {
            tvTieuDe.setText("CHỈNH SỬA CÔNG VIỆC");
            edtTenViec.setText(viecLamCanSua.getTenViecLam());
            edtLuong.setText(String.format(Locale.US, "%.0f", viecLamCanSua.getMucLuong()));
            edtDiaDiemViec.setText(viecLamCanSua.getDiaDiem());
            edtThoiGianViec.setText(viecLamCanSua.getThoiGianLamViec());
            edtMoTaViec.setText(viecLamCanSua.getMoTa());
            edtYeuCauViec.setText(viecLamCanSua.getYeuCau());
            edtQuyenLoiViec.setText(viecLamCanSua.getQuyenLoi());

            // Set chọn danh mục & nhà tuyển dụng hiện tại
            for (int i = 0; i < dsDanhMuc.size(); i++) {
                if (dsDanhMuc.get(i).getDanhMucId() == viecLamCanSua.getDanhMucId()) {
                    spnDanhMuc.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < dsNTD.size(); i++) {
                if (dsNTD.get(i).getNhaTuyenDungId() == viecLamCanSua.getNhaTuyenDungId()) {
                    spnNhaTuyenDung.setSelection(i);
                    break;
                }
            }
        } else {
            tvTieuDe.setText("THÊM CÔNG VIỆC MỚI");
        }

        builder.setPositiveButton(laChinhSua ? "Lưu thay đổi" : "Thêm công việc", (dialog, which) -> {
            String tenViec = edtTenViec.getText().toString().trim();
            String luongStr = edtLuong.getText().toString().trim();
            String diaDiem = edtDiaDiemViec.getText().toString().trim();
            String thoiGian = edtThoiGianViec.getText().toString().trim();
            String moTa = edtMoTaViec.getText().toString().trim();
            String yeuCau = edtYeuCauViec.getText().toString().trim();
            String quyenLoi = edtQuyenLoiViec.getText().toString().trim();

            if (KiemTraDuLieu.kiemTraRong(tenViec) || KiemTraDuLieu.kiemTraRong(luongStr) || KiemTraDuLieu.kiemTraRong(diaDiem)) {
                Toast.makeText(AdminActivity.this, "Vui lòng điền Tên việc, Mức lương và Địa điểm!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPosDM = spnDanhMuc.getSelectedItemPosition();
            int selectedPosNTD = spnNhaTuyenDung.getSelectedItemPosition();

            int danhMucId = (selectedPosDM >= 0 && selectedPosDM < dsDanhMuc.size()) ? dsDanhMuc.get(selectedPosDM).getDanhMucId() : 1;
            int ntdId = (selectedPosNTD >= 0 && selectedPosNTD < dsNTD.size()) ? dsNTD.get(selectedPosNTD).getNhaTuyenDungId() : 1;

            double luong = parseDoubleSafe(luongStr);

            if (laChinhSua) {
                viecLamCanSua.setDanhMucId(danhMucId);
                viecLamCanSua.setNhaTuyenDungId(ntdId);
                viecLamCanSua.setTenViecLam(tenViec);
                viecLamCanSua.setMucLuong(luong);
                viecLamCanSua.setDiaDiem(diaDiem);
                viecLamCanSua.setThoiGianLamViec(!thoiGian.isEmpty() ? thoiGian : "Ca linh hoạt");
                viecLamCanSua.setMoTa(!moTa.isEmpty() ? moTa : "Chưa có mô tả.");
                viecLamCanSua.setYeuCau(!yeuCau.isEmpty() ? yeuCau : "Không yêu cầu.");
                viecLamCanSua.setQuyenLoi(!quyenLoi.isEmpty() ? quyenLoi : "Chế độ đầy đủ.");

                boolean success = viecLamDAO.capNhatViecLam(viecLamCanSua);
                if (success) {
                    Toast.makeText(AdminActivity.this, "Cập nhật công việc thành công!", Toast.LENGTH_SHORT).show();
                    taiTatCaDuLieu();
                } else {
                    Toast.makeText(AdminActivity.this, "Cập nhật công việc thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                String ngayDang = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                ViecLam vl = new ViecLam();
                vl.setNhaTuyenDungId(ntdId);
                vl.setDanhMucId(danhMucId);
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
                    taiTatCaDuLieu();
                } else {
                    Toast.makeText(AdminActivity.this, "Thêm công việc thất bại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void xacNhanXoaViecLam(ViecLam vl) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa công việc '" + vl.getTenViecLam() + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean res = viecLamDAO.xoaViecLam(vl.getViecLamId());
                    if (res) {
                        Toast.makeText(AdminActivity.this, "Đã xóa công việc!", Toast.LENGTH_SHORT).show();
                        taiTatCaDuLieu();
                    } else {
                        Toast.makeText(AdminActivity.this, "Xóa công việc thất bại!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // TAB 3: QUẢN LÝ SINH VIÊN
    private void taiDuLieuSinhVien() {
        dsSinhVienGoc = sinhVienDAO.layTatCaSinhVien();
        hienThiDanhSachSinhVien(dsSinhVienGoc);
    }

    private void hienThiDanhSachSinhVien(List<SinhVien> ds) {
        if (ds == null || ds.isEmpty()) {
            tvKhongCoSinhVienAdmin.setVisibility(View.VISIBLE);
            rvSinhVienAdmin.setVisibility(View.GONE);
        } else {
            tvKhongCoSinhVienAdmin.setVisibility(View.GONE);
            rvSinhVienAdmin.setVisibility(View.VISIBLE);

            if (sinhVienAdapter == null) {
                sinhVienAdapter = new SinhVienAdminAdapter(this, ds, this::hienThiDialogChiTietSinhVien);
                rvSinhVienAdmin.setAdapter(sinhVienAdapter);
            } else {
                sinhVienAdapter.capNhatDanhSach(ds);
            }
        }
    }

    private void locDanhSachSinhVien(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            hienThiDanhSachSinhVien(dsSinhVienGoc);
            return;
        }

        String key = tuKhoa.toLowerCase().trim();
        List<SinhVien> dsLoc = new ArrayList<>();
        for (SinhVien sv : dsSinhVienGoc) {
            if ((sv.getHoTen() != null && sv.getHoTen().toLowerCase().contains(key)) ||
                    (sv.getMaSinhVien() != null && sv.getMaSinhVien().toLowerCase().contains(key)) ||
                    (sv.getTenTruong() != null && sv.getTenTruong().toLowerCase().contains(key)) ||
                    (sv.getEmail() != null && sv.getEmail().toLowerCase().contains(key))) {
                dsLoc.add(sv);
            }
        }
        hienThiDanhSachSinhVien(dsLoc);
    }

    private void hienThiDialogChiTietSinhVien(SinhVien sv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_chi_tiet_sinh_vien, null);
        builder.setView(dialogView);

        TextView tvHoTen = dialogView.findViewById(R.id.tvDialogHoTenSV);
        TextView tvMaSV = dialogView.findViewById(R.id.tvDialogMaSV);
        TextView tvTruong = dialogView.findViewById(R.id.tvDialogTruongSV);
        TextView tvEmail = dialogView.findViewById(R.id.tvDialogEmailSV);
        TextView tvSdt = dialogView.findViewById(R.id.tvDialogSdtSV);
        TextView tvGioiThieu = dialogView.findViewById(R.id.tvDialogGioiThieuSV);

        tvHoTen.setText(sv.getHoTen());
        tvMaSV.setText(String.format("Mã SV: %s", sv.getMaSinhVien()));
        tvTruong.setText(sv.getTenTruong() != null && !sv.getTenTruong().isEmpty() ? sv.getTenTruong() : "Chưa cập nhật");
        tvEmail.setText(sv.getEmail());
        tvSdt.setText(sv.getSoDienThoai() != null && !sv.getSoDienThoai().isEmpty() ? sv.getSoDienThoai() : "Chưa cập nhật");
        tvGioiThieu.setText(sv.getGioiThieu() != null && !sv.getGioiThieu().isEmpty() ? sv.getGioiThieu() : "Chưa có giới thiệu.");

        builder.setPositiveButton("Đóng", null);
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
