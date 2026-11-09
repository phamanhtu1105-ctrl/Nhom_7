package com.example.nhom7.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom7.R;
import com.example.nhom7.model.SinhVien;

import java.util.List;

/**
 * Adapter hiển thị danh sách Sinh viên cho Admin
 */
public class SinhVienAdminAdapter extends RecyclerView.Adapter<SinhVienAdminAdapter.SinhVienAdminViewHolder> {

    public interface OnSinhVienAdminListener {
        void onXemChiTiet(SinhVien sinhVien);
    }

    private final Context context;
    private List<SinhVien> danhSachSinhVien;
    private final OnSinhVienAdminListener listener;

    public SinhVienAdminAdapter(Context context, List<SinhVien> danhSachSinhVien, OnSinhVienAdminListener listener) {
        this.context = context;
        this.danhSachSinhVien = danhSachSinhVien;
        this.listener = listener;
    }

    public void capNhatDanhSach(List<SinhVien> dsMoi) {
        this.danhSachSinhVien = dsMoi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SinhVienAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sinh_vien_admin, parent, false);
        return new SinhVienAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SinhVienAdminViewHolder holder, int position) {
        SinhVien sv = danhSachSinhVien.get(position);

        holder.tvHoTenSVAdminItem.setText(sv.getHoTen());
        holder.tvMaSVAdminItem.setText(sv.getMaSinhVien());
        holder.tvTruongSVAdminItem.setText(sv.getTenTruong() != null && !sv.getTenTruong().isEmpty() ? sv.getTenTruong() : "Chưa cập nhật trường");

        String contact = sv.getEmail() + (sv.getSoDienThoai() != null && !sv.getSoDienThoai().isEmpty() ? " | " + sv.getSoDienThoai() : "");
        holder.tvEmailSVAdminItem.setText(contact);

        holder.btnChiTietSVAdmin.setOnClickListener(v -> {
            if (listener != null) listener.onXemChiTiet(sv);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachSinhVien != null ? danhSachSinhVien.size() : 0;
    }

    public static class SinhVienAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTenSVAdminItem, tvMaSVAdminItem, tvTruongSVAdminItem, tvEmailSVAdminItem;
        Button btnChiTietSVAdmin;

        public SinhVienAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTenSVAdminItem = itemView.findViewById(R.id.tvHoTenSVAdminItem);
            tvMaSVAdminItem = itemView.findViewById(R.id.tvMaSVAdminItem);
            tvTruongSVAdminItem = itemView.findViewById(R.id.tvTruongSVAdminItem);
            tvEmailSVAdminItem = itemView.findViewById(R.id.tvEmailSVAdminItem);
            btnChiTietSVAdmin = itemView.findViewById(R.id.btnChiTietSVAdmin);
        }
    }
}
