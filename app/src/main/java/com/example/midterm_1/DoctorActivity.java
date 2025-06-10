package com.example.midterm_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class DoctorActivity extends AppCompatActivity {
    TextView tvName, tvEmail, tvSpecialty;
    DatePicker datePicker;
    Button btnFilter;
    ListView listAppointments;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> appointmentList;
    ArrayList<Integer> appointmentIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        tvName = findViewById(R.id.tv_doctor_name);
        tvEmail = findViewById(R.id.tv_doctor_email);
        tvSpecialty = findViewById(R.id.tv_doctor_specialty);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String specialty = getIntent().getStringExtra("specialty");

        tvName.setText("Họ tên: " + name);
        tvEmail.setText("Email: " + email);
        tvSpecialty.setText("Chuyên khoa: " + specialty);

        datePicker = findViewById(R.id.datePicker);
        btnFilter = findViewById(R.id.btn_filter);
        listAppointments = findViewById(R.id.listAppointments);
        dbHelper = new DatabaseHelper(this);

        appointmentList = new ArrayList<>();
        appointmentIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        listAppointments.setAdapter(adapter);

        btnFilter.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();

            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
            loadAppointments(selectedDate);
        });

        Button btnLogout = findViewById(R.id.btn_log_out);

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DoctorActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        listAppointments.setOnItemClickListener((parent, view, position, id) -> {
            if (appointmentIds.size() <= position) return;
            int appointmentId = appointmentIds.get(position);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT Note, Status FROM Appointments WHERE ID = ?", new String[]{String.valueOf(appointmentId)});
            String currentNote = "";
            String currentStatus = "";
            if (cursor.moveToFirst()) {
                currentNote = cursor.getString(cursor.getColumnIndexOrThrow("Note"));
                currentStatus = cursor.getString(cursor.getColumnIndexOrThrow("Status"));
            }
            cursor.close();
            db.close();

            final String finalCurrentStatus = currentStatus;

            final android.widget.EditText input = new android.widget.EditText(this);
            input.setText(currentNote);

            new AlertDialog.Builder(this)
                    .setTitle("Ghi chú và xác nhận lịch khám")
                    .setMessage("Trạng thái hiện tại: " + finalCurrentStatus)
                    .setView(input)
                    .setPositiveButton("Lưu ghi chú", (dialog, which) -> {
                        String newNote = input.getText().toString().trim();

                        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                        dbWrite.execSQL("UPDATE Appointments SET Note = ? WHERE ID = ?", new Object[]{newNote, appointmentId});
                        dbWrite.close();

                        Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_SHORT).show();

                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        loadAppointments(selectedDate);
                    })
                    .setNeutralButton(finalCurrentStatus.equals("Đã xác nhận") ? "Bỏ xác nhận" : "Xác nhận", (dialog, which) -> {
                        String newStatus = finalCurrentStatus.equals("Đã xác nhận") ? "Chưa xác nhận" : "Đã xác nhận";

                        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                        dbWrite.execSQL("UPDATE Appointments SET Status = ? WHERE ID = ?", new Object[]{newStatus, appointmentId});
                        dbWrite.close();

                        Toast.makeText(this, "Trạng thái đã cập nhật: " + newStatus, Toast.LENGTH_SHORT).show();

                        int year = datePicker.getYear();
                        int month = datePicker.getMonth() + 1;
                        int day = datePicker.getDayOfMonth();
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        loadAppointments(selectedDate);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }
    private void loadAppointments(String selectedDate) {
        appointmentList.clear();
        appointmentIds.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT a.ID AS AppID, a.Time, a.Note, a.Status, u.FullName FROM Appointments a JOIN Users u ON a.PatientEmail = u.Email WHERE Date = ?", new String[]{selectedDate});

                if (cursor.moveToFirst()) {
                do {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("AppID"));
        String time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
        String patient = cursor.getString(cursor.getColumnIndexOrThrow("FullName"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("Status"));
        String note = cursor.getString(cursor.getColumnIndexOrThrow("Note"));
        String entry = time + " - " + patient + "\n" + status + (note != null && !note.isEmpty() ? "\n" + note : "");
                        appointmentList.add(entry);
                        appointmentIds.add(id);
                    } while (cursor.moveToNext());
                } else {
                    appointmentList.add("Không có lịch khám cho ngày này.");
                    appointmentIds.clear();
                }
                cursor.close();
                db.close();
                adapter.notifyDataSetChanged();
    }
}


