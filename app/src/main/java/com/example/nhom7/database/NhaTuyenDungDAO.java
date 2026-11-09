package com.example.nhom7.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nhom7.model.NhaTuyenDung;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO xử lý dữ liệu liên quan đến Nhà tuyển dụng
 */
public class NhaTuyenDungDAO {
    private final QuanLyCSDL dbHelper;

    public NhaTuyenDungDAO(Context context) {
        dbHelper = new QuanLyCSDL(context);
    }

    // Lấy danh sách tất cả Nhà tuyển dụng
    public List<NhaTuyenDung> layDanhSachNhaTuyenDung() {
        List<NhaTuyenDung> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuanLyCSDL.BANG_NHA_TUYEN_DUNG + " ORDER BY nha_tuyen_dung_id ASC", null);

        if (cursor.moveToFirst()) {
            do {
                NhaTuyenDung ntd = new NhaTuyenDung();
                ntd.setNhaTuyenDungId(cursor.getInt(cursor.getColumnIndexOrThrow("nha_tuyen_dung_id")));
                ntd.setTenNhaTuyenDung(cursor.getString(cursor.getColumnIndexOrThrow("ten_nha_tuyen_dung")));
                ntd.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                ntd.setSoDienThoai(cursor.getString(cursor.getColumnIndexOrThrow("so_dien_thoai")));
                ntd.setDiaChi(cursor.getString(cursor.getColumnIndexOrThrow("dia_chi")));
                list.add(ntd);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}
