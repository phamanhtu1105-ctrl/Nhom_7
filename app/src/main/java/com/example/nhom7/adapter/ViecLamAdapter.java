package com.example.nhom7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.model.ViecLam;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter cho danh sách Việc làm
 */
public class ViecLamAdapter extends RecyclerView.Adapter<ViecLamAdapter.ViecLamViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ViecLam viecLam);
    }

    private final Context context;
    private final List<ViecLam> danhSachViecLam;
    private final OnItemClickListener listener;
    private final DecimalFormat dinhDangTien = new DecimalFormat("#,###");

    public ViecLamAdapter(Context context, List<ViecLam> danhSachViecLam, OnItemClickListener listener) {
        this.context = context;
        this.danhSachViecLam = danhSachViecLam;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViecLamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viec_lam, parent, false);
        return new ViecLamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViecLamViewHolder holder, int position) {
        ViecLam vl = danhSachViecLam.get(position);

        holder.tvTenViecLam.setText(vl.getTenViecLam());
        holder.tvTenNhaTuyenDung.setText(vl.getTenNhaTuyenDung() != null ? vl.getTenNhaTuyenDung() : "Nhà tuyển dụng");

        String luongFormatted = dinhDangTien.format(vl.getMucLuong()) + " VNĐ/giờ";
        holder.tvMucLuong.setText(luongFormatted);

        holder.tvDiaDiem.setText(vl.getDiaDiem());
        holder.tvThoiGianLamViec.setText(String.format("⏱ %s", (vl.getThoiGianLamViec() != null ? vl.getThoiGianLamViec() : "Linh hoạt")));
        holder.tvHinhThucLamViec.setText(vl.getHinhThucLamViec() != null ? vl.getHinhThucLamViec() : "Bán thời gian");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(vl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachViecLam != null ? danhSachViecLam.size() : 0;
    }

    public static class ViecLamViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenViecLam, tvTenNhaTuyenDung, tvMucLuong, tvDiaDiem, tvThoiGianLamViec, tvHinhThucLamViec;

        public ViecLamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenViecLam = itemView.findViewById(R.id.tvTenViecLam);
            tvTenNhaTuyenDung = itemView.findViewById(R.id.tvTenNhaTuyenDung);
            tvMucLuong = itemView.findViewById(R.id.tvMucLuong);
            tvDiaDiem = itemView.findViewById(R.id.tvDiaDiem);
            tvThoiGianLamViec = itemView.findViewById(R.id.tvThoiGianLamViec);
            tvHinhThucLamViec = itemView.findViewById(R.id.tvHinhThucLamViec);
        }
    }
}
