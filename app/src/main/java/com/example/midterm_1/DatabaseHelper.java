package com.example.midterm_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Clinic.db";
    private static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                "ID INTEGER PRIMARY KEY," +
                "Email TEXT," +
                "Password TEXT," +
                "Role boolen," +
                "FullName TEXT," +
                "Specialty TEXT)");

        db.execSQL("CREATE TABLE Appointments (" +
                "ID INTEGER PRIMARY KEY," +
                "Date TEXT," +
                "Time TEXT," +
                "DoctorEmail TEXT," +
                "PatientEmail TEXT," +
                "Status TEXT," +
                "Note TEXT)");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Users VALUES (1, 'doctor1@clinic.com', '123456', '1', 'Dr. Nguyễn Văn A', 'Nội tổng quát')");
        db.execSQL("INSERT INTO Users VALUES (2, 'doctor2@clinic.com', 'abc123', '1', 'Dr. Trần Thị B', 'Nhi khoa')");
        db.execSQL("INSERT INTO Users VALUES (3, 'doctor3@clinic.com', 'qwerty', '1', 'Dr. Lê Minh C', 'Tai - Mũi - Họng')");
        db.execSQL("INSERT INTO Users VALUES (4, 'patient1@email.com', '987654', '0', 'Lê Văn D', '-')");
        db.execSQL("INSERT INTO Users VALUES (5, 'patient2@email.com', '654321', '0', 'Trần Thị E', '-')");
        db.execSQL("INSERT INTO Users VALUES (6, 'patient3@email.com', '321654', '0', 'Nguyễn Văn F', '-')");

        db.execSQL("INSERT INTO Appointments VALUES (1, '2025-06-10', '08:00', 'doctor1@clinic.com', 'patient1@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (2, '2025-06-10', '09:30', 'doctor2@clinic.com', 'patient2@email.com', 'Đã xác nhận', 'Kiểm tra sốt cao')");
        db.execSQL("INSERT INTO Appointments VALUES (3, '2025-06-11', '10:00', 'doctor3@clinic.com', 'patient3@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (4, '2025-06-11', '14:00', 'doctor1@clinic.com', 'patient2@email.com', 'Đã xác nhận', 'Theo dõi huyết áp')");
        db.execSQL("INSERT INTO Appointments VALUES (5, '2025-06-12', '16:30', 'doctor2@clinic.com', 'patient1@email.com', 'Chưa xác nhận', '-')");

        db.execSQL("INSERT INTO Appointments VALUES (6, '2025-06-10', '10:00', 'doctor2@clinic.com', 'patient2@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (7, '2025-06-11', '09:00', 'doctor3@clinic.com', 'patient1@email.com', 'Đã xác nhận', 'Khám tổng quát')");
        db.execSQL("INSERT INTO Appointments VALUES (8, '2025-06-11', '15:30', 'doctor1@clinic.com', 'patient3@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (9, '2025-06-12', '11:00', 'doctor2@clinic.com', 'patient3@email.com', 'Đã xác nhận', 'Tư vấn tâm lý')");
        db.execSQL("INSERT INTO Appointments VALUES (10, '2025-06-12', '13:00', 'doctor3@clinic.com', 'patient2@email.com', 'Chưa xác nhận', '-')");

        db.execSQL("INSERT INTO Appointments VALUES (11, '2025-06-13', '08:30', 'doctor1@clinic.com', 'patient1@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (12, '2025-06-13', '10:15', 'doctor2@clinic.com', 'patient2@email.com', 'Đã xác nhận', 'Tái khám sau sốt')");
        db.execSQL("INSERT INTO Appointments VALUES (13, '2025-06-13', '14:00', 'doctor3@clinic.com', 'patient3@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (14, '2025-06-14', '09:00', 'doctor1@clinic.com', 'patient2@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (15, '2025-06-14', '15:30', 'doctor2@clinic.com', 'patient1@email.com', 'Đã xác nhận', 'Tư vấn dinh dưỡng')");
        db.execSQL("INSERT INTO Appointments VALUES (16, '2025-06-15', '08:00', 'doctor3@clinic.com', 'patient1@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (17, '2025-06-15', '11:45', 'doctor2@clinic.com', 'patient3@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (18, '2025-06-15', '13:15', 'doctor1@clinic.com', 'patient3@email.com', 'Đã xác nhận', 'Khám tim mạch')");
        db.execSQL("INSERT INTO Appointments VALUES (19, '2025-06-16', '10:00', 'doctor2@clinic.com', 'patient2@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (20, '2025-06-16', '16:00', 'doctor3@clinic.com', 'patient2@email.com', 'Chưa xác nhận', '-')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Appointments");
        onCreate(db);
    }
}

