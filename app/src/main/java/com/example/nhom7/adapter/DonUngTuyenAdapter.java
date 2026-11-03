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

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter cho danh sách Đơn ứng tuyển
 */
public class DonUngTuyenAdapter extends RecyclerView.Adapter<DonUngTuyenAdapter.DonUngTuyenViewHolder> {

    public interface OnHuyDonClickListener {
        void onHuyDonClick(DonUngTuyen don);
    }

    private final Context context;
    private final List<DonUngTuyen> danhSachDon;
    private final OnHuyDonClickListener listener;
    private final DecimalFormat dinhDangTien = new DecimalFormat("#,###");

    public DonUngTuyenAdapter(Context context, List<DonUngTuyen> danhSachDon, OnHuyDonClickListener listener) {
        this.context = context;
        this.danhSachDon = danhSachDon;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonUngTuyenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_don_ung_tuyen, parent, false);
        return new DonUngTuyenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonUngTuyenViewHolder holder, int position) {
        DonUngTuyen don = danhSachDon.get(position);

        holder.tvTenViecLam.setText(don.getTenViecLam() != null ? don.getTenViecLam() : "Công việc");
        holder.tvTenNhaTuyenDung.setText(don.getTenNhaTuyenDung() != null ? don.getTenNhaTuyenDung() : "Nhà tuyển dụng");
        holder.tvMucLuong.setText(String.format("Mức lương: %s VNĐ/giờ", dinhDangTien.format(don.getMucLuong())));
        holder.tvLoiGioiThieu.setText(String.format("Lời giới thiệu: %s", (don.getLoiGioiThieu() != null ? don.getLoiGioiThieu() : "")));
        holder.tvNgayUngTuyen.setText(String.format("Ngày gửi: %s", don.getNgayUngTuyen()));

        String trangThai = don.getTrangThai();
        holder.tvTrangThai.setText(trangThai);

        if ("Đã chấp nhận".equalsIgnoreCase(trangThai)) {
            holder.tvTrangThai.setTextColor(Color.parseColor("#4CAF50")); // Xanh lá
            holder.btnHuyDon.setVisibility(View.GONE);
        } else if ("Đã từ chối".equalsIgnoreCase(trangThai)) {
            holder.tvTrangThai.setTextColor(Color.parseColor("#F44336")); // Đỏ
            holder.btnHuyDon.setVisibility(View.GONE);
        } else {
            holder.tvTrangThai.setTextColor(Color.parseColor("#FF9800")); // Cam (Chờ xét duyệt)
            holder.btnHuyDon.setVisibility(View.VISIBLE);
        }

        holder.btnHuyDon.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHuyDonClick(don);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachDon != null ? danhSachDon.size() : 0;
    }

    public static class DonUngTuyenViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenViecLam, tvTenNhaTuyenDung, tvMucLuong, tvLoiGioiThieu, tvNgayUngTuyen, tvTrangThai;
        Button btnHuyDon;

        public DonUngTuyenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenViecLam = itemView.findViewById(R.id.tvTenViecLam);
            tvTenNhaTuyenDung = itemView.findViewById(R.id.tvTenNhaTuyenDung);
            tvMucLuong = itemView.findViewById(R.id.tvMucLuong);
            tvLoiGioiThieu = itemView.findViewById(R.id.tvLoiGioiThieu);
            tvNgayUngTuyen = itemView.findViewById(R.id.tvNgayUngTuyen);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnHuyDon = itemView.findViewById(R.id.btnHuyDon);
        }
    }
}
