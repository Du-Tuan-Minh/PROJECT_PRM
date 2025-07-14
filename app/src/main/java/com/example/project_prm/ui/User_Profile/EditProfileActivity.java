package com.example.project_prm.ui.User_Profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm.DataManager.DAO.UserDAO;
import com.example.project_prm.DataManager.DAO.UserProfileDAO;
import com.example.project_prm.DataManager.Entity.User;
import com.example.project_prm.DataManager.Entity.UserProfile;
import com.example.project_prm.R;
import com.example.project_prm.utils.CurrentUser;

import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    private Spinner spGender, spCountry;
    private EditText etFullName;
    private TextView tvDob, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        initViews();            // Khởi tạo các view từ layout
        setupBackButton();      // Xử lý nút quay lại
        setupSpinners();        // Gán dữ liệu cho Spinner giới tính và quốc gia
        loadUserProfile();      // Load thông tin người dùng từ Firestore
        setupUpdateButton();    // Gán sự kiện click cho nút Update

        setupDatePicker();      // Gọi để xử lý chọn ngày sinh
    }


    // Ánh xạ các view từ layout vào biến thành viên
    private void initViews() {
        etFullName = findViewById(R.id.full_name_edit);
        tvDob = findViewById(R.id.tv_dob);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        spGender = findViewById(R.id.sp_gender);
        spCountry = findViewById(R.id.sp_country);
    }


    // Gán sự kiện cho nút Back → thoát khỏi màn hình chỉnh sửa
    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }


    // Thiết lập danh sách chọn cho Spinner giới tính và quốc gia
    private void setupSpinners() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        String[] countries = {"Việt Nam", "Hoa Kỳ", "Hàn Quốc", "Nhật Bản", "Khác"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        spGender.setAdapter(genderAdapter);

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        spCountry.setAdapter(countryAdapter);
    }


    // Lấy thông tin người dùng hiện tại từ Firestore và hiển thị lên giao diện
    private void loadUserProfile() {
        String userId = CurrentUser.getUserId(this);
        if (userId == null) return;

        UserProfileDAO profileDAO = new UserProfileDAO();
        profileDAO.getByUserId(userId).addOnSuccessListener(doc -> {
            if (doc != null && doc.exists()) {
                // Lấy thông tin từ user_profiles
                String fullName = doc.getString("full_name");
                String dob = doc.getString("date_of_birth");
                String country = doc.getString("allergies");
                String gender = doc.getString("gender");

                etFullName.setText(fullName);
                tvDob.setText(dob);
                setSpinnerSelection(spGender, gender);
                setSpinnerSelection(spCountry, country);

                // 👉 Lấy thêm email và phone từ bảng users
                new UserDAO().getById(userId).addOnSuccessListener(userDoc -> {
                    if (userDoc != null && userDoc.exists()) {
                        String email = userDoc.getString("email");
                        String phone = userDoc.getString("phone");

                        tvEmail.setText(email);
                        tvPhone.setText(phone);
                    }
                });
            }
        });
    }




    // Chọn đúng giá trị trong Spinner theo chuỗi truyền vào
    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value != null) {
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            int index = adapter.getPosition(value);
            if (index >= 0) spinner.setSelection(index);
        }
    }


    // Gán sự kiện khi nhấn nút Update
    private void setupUpdateButton() {
        findViewById(R.id.update_button).setOnClickListener(v -> updateProfile());
    }


    // ✅ Hàm cập nhật hồ sơ người dùng cả bảng user_profiles và users
    private void updateProfile() {
        String userId = CurrentUser.getUserId(this);
        if (userId == null) return;

        if (!validateInput()) return;

        // Lấy dữ liệu từ UI
        String fullName = etFullName.getText().toString();
        String dob = tvDob.getText().toString();
        String email = tvEmail.getText().toString();
        String phone = tvPhone.getText().toString();
        String country = spCountry.getSelectedItem().toString();
        String gender = spGender.getSelectedItem().toString();

        // ✅ Tạo đối tượng UserProfile KHÔNG lưu phone
        UserProfile profile = new UserProfile(
                0, userId, fullName, dob, gender, "",
                0, 0, country, email, "" // emergency_contact để trống hoặc dùng cho mục khác
        );

        // ✅ Tạo đối tượng User để lưu phone và email
        User user = new User(null, email, null, phone); // role & password giữ nguyên

        UserProfileDAO profileDao = new UserProfileDAO();
        UserDAO userDao = new UserDAO();

        // ✅ Cập nhật user_profiles
        profileDao.getByUserId(userId).addOnSuccessListener(doc -> {
            if (doc != null && doc.exists()) {
                profileDao.update(doc.getId(), profile).addOnSuccessListener(unused -> {

                    // ✅ Lấy thông tin user hiện tại để giữ role và password
                    userDao.getById(userId).addOnSuccessListener(userDoc -> {
                        if (userDoc != null && userDoc.exists()) {
                            String role = userDoc.getString("role");
                            String password = userDoc.getString("password");

                            user.setRole(role);
                            user.setPassword(password);

                            // ✅ Cập nhật bảng users (gồm email + phone)
                            userDao.update(userId, user)
                                    .addOnSuccessListener(v -> {
                                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi cập nhật user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    });
                });
            }
        });
    }



    // Gọi để xử lý chọn ngày sinh

    private void setupDatePicker() {
        EditText dobEditText = findViewById(R.id.tv_dob);
        ImageView calendarIcon = findViewById(R.id.calendar_icon); // Thêm id nếu chưa có

        View.OnClickListener dateClickListener = v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String dateStr = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        dobEditText.setText(dateStr);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        };

        dobEditText.setOnClickListener(dateClickListener);
        calendarIcon.setOnClickListener(dateClickListener);
    }


    // ✅ Hàm kiểm tra dữ liệu nhập vào từ người dùng trước khi cập nhật hồ sơ
    private boolean validateInput() {
        String fullName = etFullName.getText().toString().trim();
        String dobStr = tvDob.getText().toString().trim();
        String phone = tvPhone.getText().toString().trim();
        String email = tvEmail.getText().toString().trim();
        String gender = spGender.getSelectedItem().toString();
        String country = spCountry.getSelectedItem().toString();

        // Kiểm tra họ tên
        if (fullName.isEmpty()) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return false;
        }

        // Kiểm tra ngày sinh không rỗng và không vượt quá ngày hiện tại
        if (dobStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            tvDob.requestFocus();
            return false;
        }

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            java.util.Date selectedDate = sdf.parse(dobStr);
            java.util.Date currentDate = new java.util.Date();

            if (selectedDate.after(currentDate)) {
                Toast.makeText(this, "Ngày sinh không được lớn hơn hôm nay", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Định dạng ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra số điện thoại
        if (phone.isEmpty()) {
            tvPhone.setError("Vui lòng nhập số điện thoại");
            tvPhone.requestFocus();
            return false;
        }

        if (!phone.matches("^\\+?[0-9]{9,15}$")) {
            tvPhone.setError("Số điện thoại không hợp lệ");
            tvPhone.requestFocus();
            return false;
        }

        // Kiểm tra email
        if (email.isEmpty()) {
            tvEmail.setError("Vui lòng nhập email");
            tvEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvEmail.setError("Email không hợp lệ");
            tvEmail.requestFocus();
            return false;
        }

        // Kiểm tra giới tính
        if (gender.equals("Khác") || gender.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn giới tính hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra quốc gia
        if (country.equals("Khác") || country.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn quốc gia hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



}
