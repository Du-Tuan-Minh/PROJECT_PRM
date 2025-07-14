package com.example.project_prm.MainScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_prm.R;
import java.util.ArrayList;
import java.util.List;
import com.example.project_prm.Repository.AppointmentRepository;
import com.example.project_prm.Model.AppointmentModel;
import android.app.ProgressDialog;

public class CancelledAppointmentsFragment extends Fragment {
    
    private RecyclerView rvAppointments;
    private AppointmentAdapter adapter;
    private List<AppointmentModel> appointments;
    private ProgressDialog progressDialog;
    private AppointmentRepository firestoreRepo;
    
    public static CancelledAppointmentsFragment newInstance() {
        return new CancelledAppointmentsFragment();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments_list, container, false);
        
        initViews(view);
        setupRecyclerView();
        firestoreRepo = AppointmentRepository.getInstance();
        loadCancelledAppointmentsFromFirestore();
        
        return view;
    }
    
    private void initViews(View view) {
        rvAppointments = view.findViewById(R.id.rvAppointments);
    }
    
    private void setupRecyclerView() {
        appointments = new ArrayList<>();
        adapter = new AppointmentAdapter(appointments);
        adapter.setOnAppointmentActionListener(new AppointmentAdapter.OnAppointmentActionListener() {
            @Override
            public void onAppointmentClick(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onAppointmentClick(appointment.getId(), appointment.getStatus());
                }
            }
            
            @Override
            public void onCancelAppointment(AppointmentModel appointment) {
                // Không áp dụng cho cancelled
            }
            
            @Override
            public void onRescheduleAppointment(AppointmentModel appointment) {
                // Không áp dụng cho cancelled
            }
            
            @Override
            public void onBookAgain(AppointmentModel appointment) {
                if (getActivity() instanceof AppointmentHistoryActivity) {
                    ((AppointmentHistoryActivity) getActivity()).onBookAgain(appointment.getDoctorName());
                }
            }
            
            @Override
            public void onLeaveReview(AppointmentModel appointment) {
                // Không áp dụng cho cancelled
            }
        });
        
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAppointments.setAdapter(adapter);
    }
    
    private void loadCancelledAppointmentsFromFirestore() {
        showLoading();
        String patientId = getCurrentPatientId(); // TODO: Lấy đúng patientId user hiện tại
        firestoreRepo.getAppointmentsByPatientId(patientId, new AppointmentRepository.OnAppointmentsLoadedListener() {
            @Override
            public void onAppointmentsLoaded(java.util.List<AppointmentModel> firestoreAppointments) {
                appointments.clear();
                for (AppointmentModel fs : firestoreAppointments) {
                    if ("cancelled".equalsIgnoreCase(fs.getStatus())) {
                        appointments.add(convertToMainScreenModel(fs));
                    }
                }
                adapter.notifyDataSetChanged();
                hideLoading();
            }
            @Override
            public void onError(String error) {
                hideLoading();
            }
        });
    }
    private void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang tải dữ liệu...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    private String getCurrentPatientId() {
        // TODO: Lấy patientId thực tế từ user đăng nhập
        return "test_patient_id";
    }
    private AppointmentModel convertToMainScreenModel(AppointmentModel fs) {
        AppointmentModel m = new AppointmentModel();
        m.setId(fs.getId());
        m.setDoctorName(fs.getDoctorName());
        m.setSpecialty(fs.getSpecialty());
        m.setDate(fs.getDate());
        m.setTime(fs.getTime());
        m.setPackageType(fs.getPackageType());
        m.setAmount(fs.getAmount());
        m.setDuration(fs.getDuration());
        m.setStatus(fs.getStatus());
        m.setProblemDescription(fs.getProblemDescription());
        m.setPatientName(fs.getPatientName());
        m.setPatientPhone(fs.getPatientPhone());
        m.setEmergencyContact(fs.getEmergencyContact());
        m.setEmergencyPhone(fs.getEmergencyPhone());
        m.setCreatedAt(fs.getCreatedAt());
        m.setUpdatedAt(fs.getUpdatedAt());
        return m;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadCancelledAppointmentsFromFirestore();
    }
} 