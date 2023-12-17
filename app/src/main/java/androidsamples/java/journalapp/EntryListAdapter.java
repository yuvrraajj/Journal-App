package androidsamples.java.journalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class EntryListAdapter extends RecyclerView.Adapter<EntryViewHolder> {
    private final LayoutInflater mInflater;
    private List<JournalEntry> mEntries;

    public EntryListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fragment_entry, parent, false);
        return new EntryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        if (mEntries != null) {
            JournalEntry current = mEntries.get(position);
            holder.bind(current);
        }
    }

    @Override
    public int getItemCount() {
        return (mEntries == null) ? 0 : mEntries.size();
    }

    public void setEntries(List<JournalEntry> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }
}