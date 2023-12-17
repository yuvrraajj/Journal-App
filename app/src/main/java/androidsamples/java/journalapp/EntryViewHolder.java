package androidsamples.java.journalapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

class EntryViewHolder extends RecyclerView.ViewHolder {
    private JournalEntry mEntry;

    private final TextView mTxtTitle;
    private final TextView mTxtDate;
    private final TextView mTxtStartTime;
    private final TextView mTxtEndTime;


    public EntryViewHolder(@NonNull View itemView) {
        super(itemView);

        mTxtTitle = itemView.findViewById(R.id.txt_item_title);
        mTxtDate = itemView.findViewById(R.id.txt_item_date);
        mTxtStartTime = itemView.findViewById(R.id.txt_item_start_time);
        mTxtEndTime = itemView.findViewById(R.id.txt_item_end_time);

        itemView.setOnClickListener(this::launchEntryDetails);
    }

    private void launchEntryDetails(View view) {
        EntryListFragmentDirections.AddEntryAction action = EntryListFragmentDirections.addEntryAction();
        action.setEntryId(mEntry.getUid().toString());
        action.setSelectedYear(mEntry.getYear());
        action.setSelectedMonth(mEntry.getMonth());
        action.setSelectedDate(mEntry.getDay());
        action.setSelectedStartHour(mEntry.getStartHour());
        action.setSelectedEndHour(mEntry.getEndHour());
        action.setSelectedStartMinute(mEntry.getStartMinute());
        action.setSelectedEndMinute(mEntry.getEndMinute());

        Navigation.findNavController(view).navigate(action);
    }

    void bind(JournalEntry entry) {
        mEntry = entry;
        this.mTxtTitle.setText(mEntry.getTitle());
        this.mTxtDate.setText(mEntry.getFullDateString());
        this.mTxtStartTime.setText(mEntry.getFullTimeString(true));
        this.mTxtEndTime.setText(mEntry.getFullTimeString(false));
    }
}