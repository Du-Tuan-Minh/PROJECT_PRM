// File: app/src/main/java/com/example/project_prm/ui/BookingScreen/TimeSlotAdapter.java
// Add this interface to TimeSlotAdapter class

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    // Add this interface
    public interface OnTimeSlotClickListener {
        void onTimeSlotSelected(String timeSlot, int position);
    }

    private List<String> timeSlots;
    private OnTimeSlotClickListener listener;
    private int selectedPosition = -1;

    public TimeSlotAdapter(List<String> timeSlots, OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    // Rest of adapter implementation...

    @Override
    public TimeSlotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Implementation
        return null;
    }

    @Override
    public void onBindViewHolder(TimeSlotViewHolder holder, int position) {
        // Implementation
    }

    @Override
    public int getItemCount() {
        return timeSlots != null ? timeSlots.size() : 0;
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        public TimeSlotViewHolder(View itemView) {
            super(itemView);
        }
    }
}