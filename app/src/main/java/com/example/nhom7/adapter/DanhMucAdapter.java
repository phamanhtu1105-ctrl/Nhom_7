package com.example.nhom7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.model.DanhMuc;

import java.util.List;

/**
 * Adapter cho Danh mục công việc trên Trang chủ
 */
public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.DanhMucViewHolder> {

    public interface OnDanhMucClickListener {
        void onDanhMucClick(DanhMuc danhMuc);
    }

    private final Context context;
    private final List<DanhMuc> danhSachDanhMuc;
    private final OnDanhMucClickListener listener;

    public DanhMucAdapter(Context context, List<DanhMuc> danhSachDanhMuc, OnDanhMucClickListener listener) {
        this.context = context;
        this.danhSachDanhMuc = danhSachDanhMuc;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DanhMucViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_danh_muc, parent, false);
        return new DanhMucViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhMucViewHolder holder, int position) {
        DanhMuc dm = danhSachDanhMuc.get(position);
        holder.tvTenDanhMuc.setText(dm.getTenDanhMuc());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDanhMucClick(dm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachDanhMuc != null ? danhSachDanhMuc.size() : 0;
    }

    public static class DanhMucViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenDanhMuc;

        public DanhMucViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenDanhMuc = itemView.findViewById(R.id.tvTenDanhMuc);
        }
    }
}
