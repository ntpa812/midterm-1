package com.example.midterm_1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email=? AND Password=?", new String[]{email, password});

            if (cursor.moveToFirst()) {
                int role = cursor.getInt(cursor.getColumnIndexOrThrow("Role"));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("FullName"));
                String specialty = cursor.getString(cursor.getColumnIndexOrThrow("Specialty"));

                Intent intent;

                if (role==1) {
                    intent = new Intent(LoginActivity.this, DoctorActivity.class);
                    intent.putExtra("name", fullName);
                    intent.putExtra("specialty", specialty);
                } else if (role==0) {
                    intent = new Intent(LoginActivity.this, PatientActivity.class);
                    intent.putExtra("name", fullName);
                } else {
                    Toast.makeText(this, "Role không hợp lệ", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    db.close();
                    return;
                }
                intent.putExtra("email", email);
                intent.putExtra("role", role);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        });

    }



}
