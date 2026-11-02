package com.example.nhom7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.model.ViecLam;
import com.example.nhom7.model.ViecYeuThich;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Adapter cho danh sách Việc làm yêu thích
 */
public class ViecYeuThichAdapter extends RecyclerView.Adapter<ViecYeuThichAdapter.ViecYeuThichViewHolder> {

    public interface OnYeuThichActionListener {
        void onItemClick(ViecLam viecLam);
        void onXoaClick(ViecYeuThich viecYeuThich);
    }

    private final Context context;
    private final List<ViecYeuThich> danhSachYeuThich;
    private final OnYeuThichActionListener listener;
    private final DecimalFormat dinhDangTien = new DecimalFormat("#,###");

    public ViecYeuThichAdapter(Context context, List<ViecYeuThich> danhSachYeuThich, OnYeuThichActionListener listener) {
        this.context = context;
        this.danhSachYeuThich = danhSachYeuThich;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViecYeuThichViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viec_yeu_thich, parent, false);
        return new ViecYeuThichViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViecYeuThichViewHolder holder, int position) {
        ViecYeuThich yt = danhSachYeuThich.get(position);
        ViecLam vl = yt.getViecLam();

        if (vl != null) {
            holder.tvTenViecLam.setText(vl.getTenViecLam());
            holder.tvTenNhaTuyenDung.setText(vl.getTenNhaTuyenDung() != null ? vl.getTenNhaTuyenDung() : "Nhà tuyển dụng");
            holder.tvMucLuong.setText(dinhDangTien.format(vl.getMucLuong()) + " VNĐ/giờ");
            holder.tvDiaDiem.setText(vl.getDiaDiem());

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(vl);
                }
            });
        }

        holder.btnXoaYeuThich.setOnClickListener(v -> {
            if (listener != null) {
                listener.onXoaClick(yt);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachYeuThich != null ? danhSachYeuThich.size() : 0;
    }

    public static class ViecYeuThichViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenViecLam, tvTenNhaTuyenDung, tvMucLuong, tvDiaDiem;
        ImageView btnXoaYeuThich;

        public ViecYeuThichViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenViecLam = itemView.findViewById(R.id.tvTenViecLam);
            tvTenNhaTuyenDung = itemView.findViewById(R.id.tvTenNhaTuyenDung);
            tvMucLuong = itemView.findViewById(R.id.tvMucLuong);
            tvDiaDiem = itemView.findViewById(R.id.tvDiaDiem);
            btnXoaYeuThich = itemView.findViewById(R.id.btnXoaYeuThich);
        }
    }
}
