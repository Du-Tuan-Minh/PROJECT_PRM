package com.example.project_prm.DataManager;

import android.content.Context;

import com.example.project_prm.DataManager.DAO.AppointmentDAO;
import com.example.project_prm.DataManager.Entity.Appointment;
import com.example.project_prm.DataManager.Entity.AppointmentStatus;
import com.example.project_prm.DataManager.Entity.Doctor;
import com.example.project_prm.DataManager.Repository.DoctorRepository;
import com.example.project_prm.DataManager.Repository.ClinicRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SampleDataCreator {

    private DatabaseHelper dbHelper;
    private AppointmentDAO appointmentDAO;
    private DoctorRepository doctorRepository;
    private ClinicRepository clinicRepository;
    private Random random;

    // Sample patient data
    private final String[] patientNames = {
            "Anderson Ashley", "John Smith", "Sarah Johnson", "Michael Brown",
            "Emily Davis", "David Wilson", "Lisa Anderson", "Robert Taylor",
            "Jessica Martinez", "Christopher Lee", "Amanda Rodriguez", "Daniel Garcia"
    };

    private final String[] patientPhones = {
            "+(406) 555-0120", "+(406) 555-0130", "+(406) 555-0140", "+(406) 555-0150",
            "+(406) 555-0160", "+(406) 555-0170", "+(406) 555-0180", "+(406) 555-0190",
            "+(406) 555-0200", "+(406) 555-0210", "+(406) 555-0220", "+(406) 555-0230"
    };

    private final String[] patientAges = {
            "22", "35", "28", "42", "31", "55", "29", "38", "26", "45", "33", "39"
    };

    private final String[] patientGenders = {
            "Male", "Male", "Female", "Male", "Female", "Male", "Female", "Male",
            "Female", "Male", "Female", "Female"
    };

    private final String[] symptoms = {
            "Lorem ipsum dolor sit amet consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "Regular checkup and consultation for general health assessment",
            "Follow-up appointment for previous treatment and medication review",
            "Headache and fever symptoms that have persisted for several days",
            "Skin condition consultation for acne and dermatological concerns",
            "Annual health checkup and preventive care screening",
            "Stomach pain consultation and digestive system evaluation",
            "Back pain consultation and musculoskeletal assessment",
            "Chest pain evaluation and cardiovascular health screening",
            "Joint pain and mobility issues requiring orthopedic consultation",
            "Neurological symptoms including dizziness and coordination problems",
            "Respiratory issues and breathing difficulties evaluation"
    };

    public SampleDataCreator(Context context) {
        dbHelper = new DatabaseHelper(context);
        appointmentDAO = new AppointmentDAO(dbHelper.getWritableDatabase());
        doctorRepository = DoctorRepository.getInstance();
        clinicRepository = new ClinicRepository();
        random = new Random();
    }

    public void createSampleAppointments(int userId) {
        // Clear existing sample data
        clearSampleData();

        // Create sample appointments using real doctor and clinic data
        createUpcomingAppointments(userId);
        createCompletedAppointments(userId);
        createCancelledAppointments(userId);
    }

    private void clearSampleData() {
        // Optional: Clear existing data for clean start
        // appointmentDAO.deleteAllAppointments();
    }

    private void createUpcomingAppointments(int userId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Doctor> doctors = doctorRepository.getAllDoctors();

        // Create 3-4 upcoming appointments using real doctor data
        for (int i = 0; i < 3; i++) {
            Doctor doctor = doctors.get(i % doctors.size());

            // Calculate appointment date (today, tomorrow, next week)
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i == 0 ? 0 : (i == 1 ? 1 : 6));

            Appointment appointment = new Appointment();
            appointment.setUserId(userId);
            appointment.setDoctor(doctor.getName());
            appointment.setClinic(doctor.getClinic());
            appointment.setDate(dateFormat.format(calendar.getTime()));

            // Set time based on appointment sequence
            String[] times = {"15:30", "10:00", "14:00"};
            appointment.setTime(times[i]);

            // Use real patient data
            int patientIndex = i % patientNames.length;
            appointment.setPatientName(patientNames[patientIndex]);
            appointment.setPatientPhone(patientPhones[patientIndex]);
            appointment.setPatientAge(patientAges[patientIndex]);
            appointment.setPatientGender(patientGenders[patientIndex]);
            appointment.setSymptoms(symptoms[patientIndex]);

            // Get appointment types for this doctor
            List<String> appointmentTypes = doctorRepository.getAppointmentTypesForDoctor(doctor.getName());
            String appointmentType = appointmentTypes.get(i % appointmentTypes.size());
            appointment.setAppointmentType(appointmentType);

            // Calculate fee using doctor repository
            double fee = doctorRepository.getAppointmentFee(doctor.getName(), appointmentType);
            appointment.setAppointmentFee(fee);

            appointment.setStatus(AppointmentStatus.UPCOMING.getValue());
            appointmentDAO.addAppointmentWithPatientInfo(appointment);
        }
    }

    private void createCompletedAppointments(int userId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Doctor> doctors = doctorRepository.getAllDoctors();

        // Create 3-4 completed appointments
        for (int i = 0; i < 3; i++) {
            Doctor doctor = doctors.get((i + 3) % doctors.size());

            // Past dates (1 week ago, 2 weeks ago, 1 month ago)
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -(7 + i * 7));

            Appointment appointment = new Appointment();
            appointment.setUserId(userId);
            appointment.setDoctor(doctor.getName());
            appointment.setClinic(doctor.getClinic());
            appointment.setDate(dateFormat.format(calendar.getTime()));

            // Set varied times
            String[] times = {"09:00", "11:00", "16:30"};
            appointment.setTime(times[i]);

            // Use real patient data
            int patientIndex = (i + 3) % patientNames.length;
            appointment.setPatientName(patientNames[patientIndex]);
            appointment.setPatientPhone(patientPhones[patientIndex]);
            appointment.setPatientAge(patientAges[patientIndex]);
            appointment.setPatientGender(patientGenders[patientIndex]);
            appointment.setSymptoms(symptoms[patientIndex + 3]);

            // Get appointment types for this doctor
            List<String> appointmentTypes = doctorRepository.getAppointmentTypesForDoctor(doctor.getName());
            String appointmentType = appointmentTypes.get(i % appointmentTypes.size());
            appointment.setAppointmentType(appointmentType);

            // Calculate fee using doctor repository
            double fee = doctorRepository.getAppointmentFee(doctor.getName(), appointmentType);
            appointment.setAppointmentFee(fee);

            appointment.setStatus(AppointmentStatus.COMPLETED.getValue());

            // Add rating and feedback for some completed appointments
            if (i == 2) { // Last completed appointment has review
                appointment.setRating(5);
                appointment.setFeedback("Excellent service and very professional doctor! Highly recommend " + doctor.getName() + " for " + doctor.getSpecialty() + " consultations.");
            }

            appointmentDAO.addAppointmentWithPatientInfo(appointment);
        }
    }

    private void createCancelledAppointments(int userId) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Doctor> doctors = doctorRepository.getAllDoctors();

        // Create 2 cancelled appointments
        for (int i = 0; i < 2; i++) {
            Doctor doctor = doctors.get((i + 6) % doctors.size());

            // Past dates (3 weeks ago, 2 weeks ago)
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -(20 - i * 10));

            Appointment appointment = new Appointment();
            appointment.setUserId(userId);
            appointment.setDoctor(doctor.getName());
            appointment.setClinic(doctor.getClinic());
            appointment.setDate(dateFormat.format(calendar.getTime()));

            // Set times
            String[] times = {"13:00", "10:30"};
            appointment.setTime(times[i]);

            // Use real patient data
            int patientIndex = (i + 6) % patientNames.length;
            appointment.setPatientName(patientNames[patientIndex]);
            appointment.setPatientPhone(patientPhones[patientIndex]);
            appointment.setPatientAge(patientAges[patientIndex]);
            appointment.setPatientGender(patientGenders[patientIndex]);
            appointment.setSymptoms(symptoms[patientIndex + 6]);

            // Get appointment types for this doctor
            List<String> appointmentTypes = doctorRepository.getAppointmentTypesForDoctor(doctor.getName());
            String appointmentType = appointmentTypes.get(i % appointmentTypes.size());
            appointment.setAppointmentType(appointmentType);

            // Calculate fee using doctor repository
            double fee = doctorRepository.getAppointmentFee(doctor.getName(), appointmentType);
            appointment.setAppointmentFee(fee);

            appointment.setStatus(AppointmentStatus.CANCELLED.getValue());

            // Add cancellation reasons
            String[] reasons = {
                    "I have recovered from the disease",
                    "I have a schedule clash"
            };
            appointment.setCancellationReason(reasons[i]);

            appointmentDAO.addAppointmentWithPatientInfo(appointment);
        }
    }

    // Create additional sample data for testing
    public void createRandomAppointments(int userId, int count) {
        List<Doctor> doctors = doctorRepository.getAllDoctors();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < count; i++) {
            Doctor doctor = doctors.get(random.nextInt(doctors.size()));

            // Random future or past date
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(60) - 30); // ±30 days

            Appointment appointment = new Appointment();
            appointment.setUserId(userId);
            appointment.setDoctor(doctor.getName());
            appointment.setClinic(doctor.getClinic());
            appointment.setDate(dateFormat.format(calendar.getTime()));
            appointment.setTime(generateRandomTime());

            // Random patient data
            int patientIndex = random.nextInt(patientNames.length);
            appointment.setPatientName(patientNames[patientIndex]);
            appointment.setPatientPhone(patientPhones[patientIndex]);
            appointment.setPatientAge(patientAges[patientIndex]);
            appointment.setPatientGender(patientGenders[patientIndex]);
            appointment.setSymptoms(symptoms[random.nextInt(symptoms.length)]);

            // Random appointment type and fee
            List<String> appointmentTypes = doctorRepository.getAppointmentTypesForDoctor(doctor.getName());
            String appointmentType = appointmentTypes.get(random.nextInt(appointmentTypes.size()));
            appointment.setAppointmentType(appointmentType);

            double fee = doctorRepository.getAppointmentFee(doctor.getName(), appointmentType);
            appointment.setAppointmentFee(fee);

            // Random status
            AppointmentStatus[] statuses = {AppointmentStatus.UPCOMING, AppointmentStatus.COMPLETED, AppointmentStatus.CANCELLED};
            appointment.setStatus(statuses[random.nextInt(statuses.length)].getValue());

            appointmentDAO.addAppointmentWithPatientInfo(appointment);
        }
    }

    private String generateRandomTime() {
        int hour = 8 + random.nextInt(10); // 8 AM to 5 PM
        int minute = random.nextBoolean() ? 0 : 30; // Either :00 or :30
        return String.format("%02d:%02d", hour, minute);
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}