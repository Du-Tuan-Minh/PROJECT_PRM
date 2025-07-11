// NEW FILE: app/src/main/java/com/example/project_prm/DataManager/Entity/AppointmentStatus.java
package com.example.project_prm.DataManager.Entity;

public enum AppointmentStatus {
    PENDING("pending", "Chờ xác nhận"),
    CONFIRMED("confirmed", "Đã xác nhận"),
    UPCOMING("upcoming", "Sắp tới"),
    COMPLETED("completed", "Hoàn thành"),
    CANCELLED("cancelled", "Đã hủy"),
    RESCHEDULED("rescheduled", "Đã đổi lịch");

    private final String value;
    private final String displayName;

    AppointmentStatus(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AppointmentStatus fromValue(String value) {
        for (AppointmentStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return PENDING; // default
    }

    public static String[] getAllValues() {
        AppointmentStatus[] statuses = values();
        String[] result = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            result[i] = statuses[i].value;
        }
        return result;
    }

    public static String[] getAllDisplayNames() {
        AppointmentStatus[] statuses = values();
        String[] result = new String[statuses.length];
        for (int i = 0; i < statuses.length; i++) {
            result[i] = statuses[i].displayName;
        }
        return result;
    }
}