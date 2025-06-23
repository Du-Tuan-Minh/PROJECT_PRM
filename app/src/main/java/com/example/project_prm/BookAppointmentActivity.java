package com.example.project_prm;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class BookAppointmentActivity extends AppCompatActivity {

    private TextInputEditText etName, etDate, etTime;
    private MaterialButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Initialize UI components
        etName = findViewById(R.id.et_name);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        btnSubmit = findViewById(R.id.btn_submit_appointment);

        // Handle submit button
        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Send data to server or save to database
                Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}