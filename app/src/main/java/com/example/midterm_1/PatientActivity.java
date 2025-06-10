package com.example.midterm_1;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PatientActivity extends AppCompatActivity {
    TextView tvName, tvEmail, tvSpecialty;
    DatePicker datePicker;
    TimePicker timePicker;
    Spinner spinnerDoctors;
    Button btnBook;
    DatabaseHelper dbHelper;
    String patientEmail;

    ArrayList<String> doctorEmails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        ListView listAppointments = findViewById(R.id.list_appointments);
        dbHelper = new DatabaseHelper(this);
        ArrayList<String> appointments = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointments);
        listAppointments.setAdapter(adapter);
        tvName = findViewById(R.id.tv_patient_name);
        tvEmail = findViewById(R.id.tv_patient_email);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        tvName.setText("Họ tên: " + name);
        tvEmail.setText("Email: " + email);

        patientEmail = getIntent().getStringExtra("email");

        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        spinnerDoctors = findViewById(R.id.spinner_doctors);
        btnBook = findViewById(R.id.btn_book_appointment);

        Button btnLogout = findViewById(R.id.btn_log_out);

        loadMyAppointments(appointments, adapter);
        loadDoctors();

        btnBook.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            int day = datePicker.getDayOfMonth();
            String date = String.format("%04d-%02d-%02d", year, month, day);

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String time = String.format("%02d:%02d", hour, minute);

            String doctorEmail = doctorEmails.get(spinnerDoctors.getSelectedItemPosition());

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO Appointments (Date, Time, DoctorEmail, PatientEmail, Status, Note) VALUES (?, ?, ?, ?, ?, ?)",
                    new Object[]{date, time, doctorEmail, patientEmail, "Chưa xác nhận", ""});
            db.close();

            Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
            loadMyAppointments(appointments, adapter);
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PatientActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    void loadDoctors() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FullName, Email FROM Users WHERE Role = 1", null);
        ArrayList<String> names = new ArrayList<>();

        while (cursor.moveToNext()) {
            names.add(cursor.getString(0));
            doctorEmails.add(cursor.getString(1));
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctors.setAdapter(adapter);
    }

    void loadMyAppointments(ArrayList<String> appointments, ArrayAdapter<String> adapter) {
        appointments.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT Date, Time, Status, Note, DoctorEmail FROM Appointments WHERE PatientEmail = ?", new String[]{patientEmail});
        while (cursor.moveToNext()) {
            String entry = cursor.getString(0) + " " + cursor.getString(1) + "\n" +
                    "Bác sĩ: " + cursor.getString(4) + "\n" +
                    cursor.getString(2) + "\n" + cursor.getString(3);
            appointments.add(entry);
        }
        cursor.close();
        db.close();
        adapter.notifyDataSetChanged();
    }


}
