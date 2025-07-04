package com.example.project_prm.MainScreen;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.DatabaseHelper;
import com.example.project_prm.R;

public class BookAppointmentActivity extends AppCompatActivity {
    EditText etClinic, etDoctor, etDate, etTime;
    Button btnBook;
    AppointmentDAO appointmentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        etClinic = findViewById(R.id.etClinic);
        etDoctor = findViewById(R.id.etDoctor);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnBook = findViewById(R.id.btnBook);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        appointmentDAO = new AppointmentDAO(db);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        btnBook.setOnClickListener(v -> {
            String clinic = etClinic.getText().toString().trim();
            String doctor = etDoctor.getText().toString().trim();
            String date = etDate.getText().toString().trim();
            String time = etTime.getText().toString().trim();

            if (clinic.isEmpty() || doctor.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = appointmentDAO.addAppointment(userId, clinic, doctor, date, time);

            if (result != -1) {
                Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi đặt lịch!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
