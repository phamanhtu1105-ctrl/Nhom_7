package com.example.nhom7.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.model.DonUngTuyen;

import java.util.List;

/**
 * Adapter danh sách đơn ứng tuyển dành cho Admin duyệt
 */
public class DonUngTuyenAdminAdapter extends RecyclerView.Adapter<DonUngTuyenAdminAdapter.DonAdminViewHolder> {

    public interface OnAdminDuyetListener {
        void onChapNhan(DonUngTuyen don);
        void onTuChoi(DonUngTuyen don);
    }

    private final Context context;
    private final List<DonUngTuyen> danhSachDon;
    private final OnAdminDuyetListener listener;

    public DonUngTuyenAdminAdapter(Context context, List<DonUngTuyen> danhSachDon, OnAdminDuyetListener listener) {
        this.context = context;
        this.danhSachDon = danhSachDon;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_ung_tuyen_admin, parent, false);
        return new DonAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonAdminViewHolder holder, int position) {
        DonUngTuyen don = danhSachDon.get(position);

        String hoTenSV = don.getHoTenSinhVien() != null ? don.getHoTenSinhVien() : "Sinh viên #" + don.getSinhVienId();
        holder.tvHoTenSinhVien.setText(String.format("Sinh viên: %s", hoTenSV));

        holder.tvTenViecLamAdmin.setText(String.format("Vị trí: %s", don.getTenViecLam() != null ? don.getTenViecLam() : "Công việc"));
        holder.tvNhaTuyenDungAdmin.setText(String.format("Công ty: %s", don.getTenNhaTuyenDung() != null ? don.getTenNhaTuyenDung() : "Nhà tuyển dụng"));
        holder.tvLoiGioiThieuAdmin.setText(String.format("Lời giới thiệu: %s", don.getLoiGioiThieu() != null ? don.getLoiGioiThieu() : ""));

        String trangThai = don.getTrangThai();
        holder.tvTrangThaiAdmin.setText(trangThai);

        if ("Đã chấp nhận".equalsIgnoreCase(trangThai)) {
            holder.tvTrangThaiAdmin.setTextColor(Color.parseColor("#4CAF50"));
            holder.btnChapNhan.setVisibility(View.GONE);
            holder.btnTuChoi.setVisibility(View.GONE);
        } else if ("Đã từ chối".equalsIgnoreCase(trangThai)) {
            holder.tvTrangThaiAdmin.setTextColor(Color.parseColor("#F44336"));
            holder.btnChapNhan.setVisibility(View.GONE);
            holder.btnTuChoi.setVisibility(View.GONE);
        } else {
            holder.tvTrangThaiAdmin.setTextColor(Color.parseColor("#FF9800"));
            holder.btnChapNhan.setVisibility(View.VISIBLE);
            holder.btnTuChoi.setVisibility(View.VISIBLE);
        }

        holder.btnChapNhan.setOnClickListener(v -> {
            if (listener != null) listener.onChapNhan(don);
        });

        holder.btnTuChoi.setOnClickListener(v -> {
            if (listener != null) listener.onTuChoi(don);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachDon != null ? danhSachDon.size() : 0;
    }

    public static class DonAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTenSinhVien, tvTrangThaiAdmin, tvTenViecLamAdmin, tvNhaTuyenDungAdmin, tvLoiGioiThieuAdmin;
        Button btnChapNhan, btnTuChoi;

        public DonAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTenSinhVien = itemView.findViewById(R.id.tvHoTenSinhVien);
            tvTrangThaiAdmin = itemView.findViewById(R.id.tvTrangThaiAdmin);
            tvTenViecLamAdmin = itemView.findViewById(R.id.tvTenViecLamAdmin);
            tvNhaTuyenDungAdmin = itemView.findViewById(R.id.tvNhaTuyenDungAdmin);
            tvLoiGioiThieuAdmin = itemView.findViewById(R.id.tvLoiGioiThieuAdmin);
            btnChapNhan = itemView.findViewById(R.id.btnChapNhan);
            btnTuChoi = itemView.findViewById(R.id.btnTuChoi);
        }
    }
}
