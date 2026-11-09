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
import com.example.nhom7.model.ViecLam;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter danh sách công việc dành cho Admin quản lý (Sửa/Xóa)
 */
public class ViecLamAdminAdapter extends RecyclerView.Adapter<ViecLamAdminAdapter.ViecLamAdminViewHolder> {

    public interface OnViecLamAdminListener {
        void onSuaViecLam(ViecLam viecLam);
        void onXoaViecLam(ViecLam viecLam);
    }

    private final Context context;
    private List<ViecLam> danhSachViecLam;
    private final OnViecLamAdminListener listener;
    private final NumberFormat currencyFormatter;

    public ViecLamAdminAdapter(Context context, List<ViecLam> danhSachViecLam, OnViecLamAdminListener listener) {
        this.context = context;
        this.danhSachViecLam = danhSachViecLam;
        this.listener = listener;
        this.currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));
    }

    public void capNhatDanhSach(List<ViecLam> dsMoi) {
        this.danhSachViecLam = dsMoi;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViecLamAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viec_lam_admin, parent, false);
        return new ViecLamAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViecLamAdminViewHolder holder, int position) {
        ViecLam vl = danhSachViecLam.get(position);

        holder.tvTenViecLamAdminItem.setText(vl.getTenViecLam());
        holder.tvDanhMucAdminItem.setText(vl.getTenDanhMuc() != null ? vl.getTenDanhMuc() : "Chung");
        holder.tvTenNhaTuyenDungAdminItem.setText(vl.getTenNhaTuyenDung() != null ? vl.getTenNhaTuyenDung() : "Doanh nghiệp");

        String luongFormatted = currencyFormatter.format(vl.getMucLuong()) + " VNĐ/giờ";
        holder.tvMucLuongAdminItem.setText(luongFormatted);
        holder.tvDiaDiemAdminItem.setText(vl.getDiaDiem() != null ? vl.getDiaDiem() : "Toàn quốc");

        holder.btnSuaViecAdmin.setOnClickListener(v -> {
            if (listener != null) listener.onSuaViecLam(vl);
        });

        holder.btnXoaViecAdmin.setOnClickListener(v -> {
            if (listener != null) listener.onXoaViecLam(vl);
        });
    }

    @Override
    public int getItemCount() {
        return danhSachViecLam != null ? danhSachViecLam.size() : 0;
    }

    public static class ViecLamAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenViecLamAdminItem, tvDanhMucAdminItem, tvTenNhaTuyenDungAdminItem, tvMucLuongAdminItem, tvDiaDiemAdminItem;
        Button btnSuaViecAdmin, btnXoaViecAdmin;

        public ViecLamAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenViecLamAdminItem = itemView.findViewById(R.id.tvTenViecLamAdminItem);
            tvDanhMucAdminItem = itemView.findViewById(R.id.tvDanhMucAdminItem);
            tvTenNhaTuyenDungAdminItem = itemView.findViewById(R.id.tvTenNhaTuyenDungAdminItem);
            tvMucLuongAdminItem = itemView.findViewById(R.id.tvMucLuongAdminItem);
            tvDiaDiemAdminItem = itemView.findViewById(R.id.tvDiaDiemAdminItem);
            btnSuaViecAdmin = itemView.findViewById(R.id.btnSuaViecAdmin);
            btnXoaViecAdmin = itemView.findViewById(R.id.btnXoaViecAdmin);
        }
    }
}
