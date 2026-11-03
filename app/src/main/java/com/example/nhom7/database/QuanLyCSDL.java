package com.example.nhom7.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nhom7.tienich.MaHoaMatKhau;

/**
 * Lớp Quản lý Cơ sở dữ liệu SQLite
 */
public class QuanLyCSDL extends SQLiteOpenHelper {

    private static final String TEN_CSDL = "part_time_job.db";
    private static final int PHIEN_BAN = 1;

    // Tên các bảng
    public static final String BANG_SINH_VIEN = "sinh_vien";
    public static final String BANG_NHA_TUYEN_DUNG = "nha_tuyen_dung";
    public static final String BANG_DANH_MUC = "danh_muc";
    public static final String BANG_VIEC_LAM = "viec_lam";
    public static final String BANG_DON_UNG_TUYEN = "don_ung_tuyen";
    public static final String BANG_VIEC_YEU_THICH = "viec_yeu_thich";

    public QuanLyCSDL(Context context) {
        super(context, TEN_CSDL, null, PHIEN_BAN);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Tạo bảng sinh_vien
        String sqlSinhVien = "CREATE TABLE " + BANG_SINH_VIEN + " (" +
                "sinh_vien_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ma_sinh_vien TEXT UNIQUE NOT NULL, " +
                "ho_ten TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "so_dien_thoai TEXT, " +
                "mat_khau_hash TEXT NOT NULL, " +
                "ten_truong TEXT, " +
                "gioi_thieu TEXT" +
                ");";
        db.execSQL(sqlSinhVien);

        // 2. Tạo bảng nha_tuyen_dung
        String sqlNhaTuyenDung = "CREATE TABLE " + BANG_NHA_TUYEN_DUNG + " (" +
                "nha_tuyen_dung_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten_nha_tuyen_dung TEXT NOT NULL, " +
                "email TEXT, " +
                "so_dien_thoai TEXT, " +
                "dia_chi TEXT" +
                ");";
        db.execSQL(sqlNhaTuyenDung);

        // 3. Tạo bảng danh_muc
        String sqlDanhMuc = "CREATE TABLE " + BANG_DANH_MUC + " (" +
                "danh_muc_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten_danh_muc TEXT NOT NULL" +
                ");";
        db.execSQL(sqlDanhMuc);

        // 4. Tạo bảng viec_lam
        String sqlViecLam = "CREATE TABLE " + BANG_VIEC_LAM + " (" +
                "viec_lam_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nha_tuyen_dung_id INTEGER, " +
                "danh_muc_id INTEGER, " +
                "ten_viec_lam TEXT NOT NULL, " +
                "mo_ta TEXT, " +
                "muc_luong REAL, " +
                "dia_diem TEXT, " +
                "thoi_gian_lam_viec TEXT, " +
                "hinh_thuc_lam_viec TEXT, " +
                "yeu_cau TEXT, " +
                "quyen_loi TEXT, " +
                "han_ung_tuyen TEXT, " +
                "ngay_dang TEXT, " +
                "FOREIGN KEY (nha_tuyen_dung_id) REFERENCES " + BANG_NHA_TUYEN_DUNG + " (nha_tuyen_dung_id) ON DELETE CASCADE, " +
                "FOREIGN KEY (danh_muc_id) REFERENCES " + BANG_DANH_MUC + " (danh_muc_id) ON DELETE CASCADE" +
                ");";
        db.execSQL(sqlViecLam);

        // 5. Tạo bảng don_ung_tuyen
        String sqlDonUngTuyen = "CREATE TABLE " + BANG_DON_UNG_TUYEN + " (" +
                "don_ung_tuyen_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sinh_vien_id INTEGER NOT NULL, " +
                "viec_lam_id INTEGER NOT NULL, " +
                "loi_gioi_thieu TEXT, " +
                "ngay_ung_tuyen TEXT, " +
                "trang_thai TEXT, " +
                "UNIQUE (sinh_vien_id, viec_lam_id), " +
                "FOREIGN KEY (sinh_vien_id) REFERENCES " + BANG_SINH_VIEN + " (sinh_vien_id) ON DELETE CASCADE, " +
                "FOREIGN KEY (viec_lam_id) REFERENCES " + BANG_VIEC_LAM + " (viec_lam_id) ON DELETE CASCADE" +
                ");";
        db.execSQL(sqlDonUngTuyen);

        // 6. Tạo bảng viec_yeu_thich
        String sqlViecYeuThich = "CREATE TABLE " + BANG_VIEC_YEU_THICH + " (" +
                "viec_yeu_thich_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sinh_vien_id INTEGER NOT NULL, " +
                "viec_lam_id INTEGER NOT NULL, " +
                "ngay_them TEXT, " +
                "UNIQUE (sinh_vien_id, viec_lam_id), " +
                "FOREIGN KEY (sinh_vien_id) REFERENCES " + BANG_SINH_VIEN + " (sinh_vien_id) ON DELETE CASCADE, " +
                "FOREIGN KEY (viec_lam_id) REFERENCES " + BANG_VIEC_LAM + " (viec_lam_id) ON DELETE CASCADE" +
                ");";
        db.execSQL(sqlViecYeuThich);

        // Chèn dữ liệu mẫu thử nghiệm
        chenDuLieuMau(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BANG_VIEC_YEU_THICH);
        db.execSQL("DROP TABLE IF EXISTS " + BANG_DON_UNG_TUYEN);
        db.execSQL("DROP TABLE IF EXISTS " + BANG_VIEC_LAM);
        db.execSQL("DROP TABLE IF EXISTS " + BANG_DANH_MUC);
        db.execSQL("DROP TABLE IF EXISTS " + BANG_NHA_TUYEN_DUNG);
        db.execSQL("DROP TABLE IF EXISTS " + BANG_SINH_VIEN);
        onCreate(db);
    }

    // Phương thức chèn dữ liệu mẫu ban đầu
    private void chenDuLieuMau(SQLiteDatabase db) {
        // 0. Chèn tài khoản Admin hệ thống (admin@gmail.com / admin123)
        ContentValues cvAdmin = new ContentValues();
        cvAdmin.put("ma_sinh_vien", "ADMIN001");
        cvAdmin.put("ho_ten", "Quản Trị Viên (ADMIN)");
        cvAdmin.put("email", "admin@gmail.com");
        cvAdmin.put("so_dien_thoai", "0900000000");
        cvAdmin.put("mat_khau_hash", MaHoaMatKhau.maHoaSHA256("admin123"));
        cvAdmin.put("ten_truong", "Hệ thống Quản trị Nhom_7");
        cvAdmin.put("gioi_thieu", "Tài khoản Quản trị viên hệ thống.");
        db.insert(BANG_SINH_VIEN, null, cvAdmin);

        // 1. Chèn tài khoản sinh viên mẫu (Tài khoản thử nghiệm: sv001@gmail.com / 123456)
        ContentValues cvSV = new ContentValues();
        cvSV.put("ma_sinh_vien", "SV001");
        cvSV.put("ho_ten", "Nguyễn Văn An");
        cvSV.put("email", "sv001@gmail.com");
        cvSV.put("so_dien_thoai", "0912345678");
        cvSV.put("mat_khau_hash", MaHoaMatKhau.maHoaSHA256("123456"));
        cvSV.put("ten_truong", "Đại học Công nghệ");
        cvSV.put("gioi_thieu", "Sinh viên năm 4 ngành CNTT, giao tiếp tốt, chăm chỉ.");
        long idSV1 = db.insert(BANG_SINH_VIEN, null, cvSV);

        // 2. Chèn 8 Danh mục công việc theo yêu cầu
        String[] danhSachDanhMuc = {
                "Nhân viên bán hàng",
                "Nhân viên phục vụ",
                "Gia sư",
                "Cộng tác viên",
                "Nhân viên giao hàng",
                "Nhân viên văn phòng",
                "Marketing",
                "Công việc online"
        };
        for (String tenDM : danhSachDanhMuc) {
            ContentValues cvDM = new ContentValues();
            cvDM.put("ten_danh_muc", tenDM);
            db.insert(BANG_DANH_MUC, null, cvDM);
        }

        // 3. Chèn Nhà tuyển dụng mẫu
        String[][] nhaTuyenDungMau = {
                {"Chuỗi Cà Phê Highlands", "tuyendung@highlands.vn", "02871001234", "Quận 1, TP. Hồ Chí Minh"},
                {"Nhà Sách Phương Nam", "hr@nhasachphuongnam.vn", "02838225566", "Quận 3, TP. Hồ Chí Minh"},
                {"Trung Tâm Gia Sư Tri Thức", "giasutrithuc@gmail.com", "0909123456", "Quận Bình Thạnh, TP. Hồ Chí Minh"},
                {"Công Ty Truyền Thông ABC", "contact@abcexpress.com", "0988776655", "Quận Tân Bình, TP. Hồ Chí Minh"},
                {"Cửa Hàng Tiện Lợi Circle K", "recruitment@circlek.com.vn", "02836208888", "Quận 10, TP. Hồ Chí Minh"}
        };
        for (String[] ntd : nhaTuyenDungMau) {
            ContentValues cvNTD = new ContentValues();
            cvNTD.put("ten_nha_tuyen_dung", ntd[0]);
            cvNTD.put("email", ntd[1]);
            cvNTD.put("so_dien_thoai", ntd[2]);
            cvNTD.put("dia_chi", ntd[3]);
            db.insert(BANG_NHA_TUYEN_DUNG, null, cvNTD);
        }

        // 4. Chèn Việc làm mẫu
        Object[][] viecLamMau = {
                {1, 2, "Nhân viên Phục vụ Cà phê Ca Sáng/Tối", "Pha chế và phục vụ khách hàng tại cửa hàng.", 25000.0, "Quận 1, TP. Hồ Chí Minh", "Ca 4 tiếng (7h-11h hoặc 18h-22h)", "Bán thời gian", "Nhanh nhẹn, trung thực, ưu tiên sinh viên", "Bao cơm, hỗ trợ xoay ca học", "30/12/2026", "01/11/2026"},
                {2, 1, "Thu ngân & Bán hàng Nhà sách", "Thực hiện tính tiền và tư vấn sách cho khách hàng.", 28000.0, "Quận 3, TP. Hồ Chí Minh", "Ca 5 tiếng (13h-18h)", "Bán thời gian", "Cẩn thận, giao tiếp tốt", "Giảm giá mua sách, thưởng doanh số", "25/12/2026", "02/11/2026"},
                {3, 3, "Gia sư Toán & Lý lớp 10", "Dạy kèm học sinh cấp 3 chuẩn bị thi học kỳ.", 120000.0, "Quận Bình Thạnh, TP. Hồ Chí Minh", "2 buổi/tuần (19h-21h)", "Bán thời gian", "Sinh viên ngành Sư phạm/Kỹ thuật điểm cao", "Lương theo buổi, thưởng nếu học sinh tiến bộ", "20/12/2026", "01/11/2026"},
                {4, 7, "Cộng tác viên Viết nội dung Marketing", "Sáng tạo nội dung bài viết Fanpage và Website.", 35000.0, "Công việc online", "Linh hoạt 15-20h/tuần", "Online / Bán thời gian", "Biết viết lách, ưu tiên sinh viên Báo chí/Marketing", "Thưởng theo chất lượng bài viết", "31/12/2026", "02/11/2026"},
                {5, 5, "Nhân viên Giao hàng Bán thời gian", "Giao nhận đơn hàng ngắn hạn khu vực nội thành.", 30000.0, "Quận 10, TP. Hồ Chí Minh", "Ca linh hoạt 3-4 tiếng", "Bán thời gian", "Có xe máy riêng và bằng lái A1", "Hỗ trợ xăng xe, tiền điện thoại", "15/12/2026", "01/11/2026"},
                {1, 2, "Nhân viên Pha chế Barista", "Thực hiện pha chế các dòng đồ uống theo công thức.", 27000.0, "Quận 1, TP. Hồ Chí Minh", "Ca tối (17h-22h)", "Bán thời gian", "Được đào tạo pha chế, trung thực", "Chế độ xoay ca linh hoạt", "28/12/2026", "02/11/2026"}
        };

        for (Object[] vl : viecLamMau) {
            ContentValues cvVL = new ContentValues();
            cvVL.put("nha_tuyen_dung_id", (Integer) vl[0]);
            cvVL.put("danh_muc_id", (Integer) vl[1]);
            cvVL.put("ten_viec_lam", (String) vl[2]);
            cvVL.put("mo_ta", (String) vl[3]);
            cvVL.put("muc_luong", (Double) vl[4]);
            cvVL.put("dia_diem", (String) vl[5]);
            cvVL.put("thoi_gian_lam_viec", (String) vl[6]);
            cvVL.put("hinh_thuc_lam_viec", (String) vl[7]);
            cvVL.put("yeu_cau", (String) vl[8]);
            cvVL.put("quyen_loi", (String) vl[9]);
            cvVL.put("han_ung_tuyen", (String) vl[10]);
            cvVL.put("ngay_dang", (String) vl[11]);
            db.insert(BANG_VIEC_LAM, null, cvVL);
        }

        // 5. Chèn 1 đơn ứng tuyển mẫu cho Sinh viên SV001
        if (idSV1 != -1) {
            ContentValues cvUT = new ContentValues();
            cvUT.put("sinh_vien_id", idSV1);
            cvUT.put("viec_lam_id", 1);
            cvUT.put("loi_gioi_thieu", "Em muốn ứng tuyển ca sáng vì thời gian học ở trường rơi vào ca chiều.");
            cvUT.put("ngay_ung_tuyen", "02/11/2026");
            cvUT.put("trang_thai", "Chờ xét duyệt");
            db.insert(BANG_DON_UNG_TUYEN, null, cvUT);

            // Chèn 1 việc yêu thích mẫu
            ContentValues cvYT = new ContentValues();
            cvYT.put("sinh_vien_id", idSV1);
            cvYT.put("viec_lam_id", 3);
            cvYT.put("ngay_them", "02/11/2026");
            db.insert(BANG_VIEC_YEU_THICH, null, cvYT);
        }
    }
}
