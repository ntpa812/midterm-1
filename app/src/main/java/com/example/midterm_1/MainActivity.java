package com.example.midterm_1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvInfo = new TextView(this);
        setContentView(tvInfo);

        String name = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        String email = getIntent().getStringExtra("email");
        String specialty = getIntent().getStringExtra("specialty");

        String displayText = "Chào " + name + "\nVai trò: " + role + "\nEmail: " + email;
        if (!specialty.equals("-")) {
            displayText += "\nChuyên khoa: " + specialty;
        }

        tvInfo.setText(displayText);
    }
}
