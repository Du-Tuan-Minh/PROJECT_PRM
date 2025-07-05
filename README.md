app/
├── java/
│   └── com.example.healthbooking/
│       ├── data/               → Quản lý dữ liệu: API, DB, Model, Repository
│       │   ├── api/            → Retrofit API interface
│       │   ├── db/             → SQLite hoặc Room (DAO, Database class)
│       │   ├── model/          → Các model (POJO) như User.java, Doctor.java
│       │   └── repository/     → Repository pattern để tách biệt data logic
│
│       ├── ui/                 → UI cho từng màn hình (Activity/Fragment)
│       │   ├── login/          → LoginActivity.java, LoginViewModel.java
│       │   ├── main/           → MainActivity.java, MainFragment.java
│       │   ├── booking/        → BookingActivity.java, BookingAdapter.java
│       │   └── adapter/        → RecyclerView adapters
│
│       ├── widgets/            → Custom Button, EditText, Dialog,...
│
│       ├── utils/              → Tiện ích dùng chung: DateUtils, SharedPrefs,...
│
│       └── App.java            → Application class (khởi tạo global)
│
res/
└── layout/
│   ├── activity/
│   │   └── activity_main.xml
│   ├── fragment/
│   │   └── fragment_profile.xml
│   ├── item/
│   │   └── item_doctor.xml
│   ├── dialog/
│   │   └── dialog_confirm.xml
│
├── drawable/           → Backgrounds, shapes, vector, ảnh png/jpg
│   │   ├── icon/
│   │   ├── ic_user.xml
│   │   ├── ic_back.xml
│   │
│   ├── shape/
│   │   ├── bg_button_primary.xml
│   │   ├── bg_card.xml
│   │
│   ├── selector/
│   │   ├── btn_selector.xml
│   │   └── tab_selector.xml
│   │
│   ├── image/
│   │   └── placeholder.png
│   │
│   ├── mipmap/             → Icon launcher app (tự động tạo)
│   │   └── ic_launcher.png
│
├── values/             → Các file định nghĩa chung (theme, color, string)
│   ├── colors.xml              # Màu sắc dùng chung
│   ├── strings.xml             # Text đa ngôn ngữ
│   ├── styles.xml              # Style theme cho App
│   └── dimens.xml              # Kích thước padding/margin/font
│
├── font/               → Custom fonts (.ttf, .otf)
│   ├── roboto_regular.ttf
│   └── roboto_bold.ttf
│
├── anim/               → Animation XML (fade_in, slide, etc.)
│   └── fade_in.xml
│
├── menu/               → Menu XML cho Toolbar hoặc BottomNav
│   └── main_menu.xml
│
├── xml/                → File cấu hình (preferences, file_paths,...)
│   └── file_paths.xml



Naming Convention (Quy tắc đặt tên)
Tên lớp	        Kiểu	        Ví dụ
Activity	Tên + Activity	    LoginActivity, MainActivity
Fragment	Tên + Fragment	    ProfileFragment
Adapter	    Tên + Adapter	    DoctorAdapter, MessageAdapter
Model	    Danh từ	            User, Doctor, Appointment
XML Layout	snake_case	        activity_login.xml, fragment_home.xml