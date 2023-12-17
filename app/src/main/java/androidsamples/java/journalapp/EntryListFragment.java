package androidsamples.java.journalapp;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_TEXT;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A fragment representing a list of Items.
 */
public class EntryListFragment extends Fragment {

    private EntryListViewModel mEntryListViewModel;

    private FloatingActionButton mBtnAddEntry;
    private RecyclerView mRecyclerView;
    private EntryListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mEntryListViewModel = new ViewModelProvider(this).get(EntryListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry_list, container, false);

        mBtnAddEntry = v.findViewById(R.id.btn_add_entry);
        mRecyclerView = v.findViewById(R.id.recyclerView);

        mAdapter = new EntryListAdapter(getActivity());

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnAddEntry.setOnClickListener(this::addNewEntry);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mEntryListViewModel.getAllEntries().observe(requireActivity(), mAdapter::setEntries);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_entry_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_info) {
            Navigation.findNavController(requireView()).navigate(R.id.showInfoAction);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Create an empty entry and navigate to EntryDetailsFragment.
     * @param view the add entry button view
     */
    private void addNewEntry(View view) {
        JournalEntry entry = new JournalEntry("", 0, 0, 0, 0, 0, 0, 0);
        mEntryListViewModel.insert(entry);

        EntryListFragmentDirections.AddEntryAction action = EntryListFragmentDirections.addEntryAction();
        action.setEntryId(entry.getUid().toString());

        Navigation.findNavController(view).navigate(action);
    }
}