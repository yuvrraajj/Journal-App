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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailsFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryDetailsFragment extends Fragment {

    private EntryDetailsViewModel mEntryDetailsViewModel;
    private JournalEntry mEntry;

    private Button mBtnEntryDate;
    private Button mBtnStartTime;
    private Button mBtnEndTime;
    private Button mBtnSave;
    private EditText mEditTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mEntryDetailsViewModel = new ViewModelProvider(requireActivity()).get(EntryDetailsViewModel.class);

        loadEntry();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(getContext(), "Save your entry first!", Toast.LENGTH_SHORT).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry_details, container, false);

        mBtnEntryDate = v.findViewById(R.id.btn_entry_date);
        mBtnStartTime = v.findViewById(R.id.btn_start_time);
        mBtnEndTime = v.findViewById(R.id.btn_end_time);
        mBtnSave = v.findViewById(R.id.btn_save);
        mEditTitle = v.findViewById(R.id.edit_title);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnEntryDate.setOnClickListener(this::pickDate);
        mBtnStartTime.setOnClickListener(v -> pickTime(v, true));
        mBtnEndTime.setOnClickListener(v -> pickTime(v, false));
        mBtnSave.setOnClickListener(this::saveDetails);

        mEntryDetailsViewModel.getEntryLiveData().observe(requireActivity(),
                entry -> {
                    this.mEntry = entry;
                    if (entry != null) updateUI();
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_entry_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_entry) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        mEntryDetailsViewModel.deleteEntry(mEntry);
                        goBack();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else if (item.getItemId() == R.id.menu_share_entry) {
            Intent intent = new Intent(ACTION_SEND);
            String shareBody = "Look what I have been up to: " + mEntry.getTitle()
                    + " on " + mEntry.getFullDateString()
                    + ", " + mEntry.getFullTimeString(true)
                    + " to " + mEntry.getFullTimeString(false) + ".";
            intent.setType("text/plain");
            intent.putExtra(EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Share using"));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Loads entry into view model
     */
    private void loadEntry() {
        UUID entryId = UUID.fromString(EntryDetailsFragmentArgs.fromBundle(getArguments()).getEntryId());
        mEntryDetailsViewModel.loadEntry(entryId);

        EntryDetailsFragmentArgs arguments = EntryDetailsFragmentArgs.fromBundle(getArguments());
        int year = arguments.getSelectedYear();
        int month = arguments.getSelectedMonth();
        int day = arguments.getSelectedDate();
        int startHour = arguments.getSelectedStartHour();
        int endHour = arguments.getSelectedEndHour();
        int startMinute = arguments.getSelectedStartMinute();
        int endMinute = arguments.getSelectedEndMinute();

        mEntryDetailsViewModel.setYear(year);
        mEntryDetailsViewModel.setMonth(month);
        mEntryDetailsViewModel.setDay(day);
        mEntryDetailsViewModel.setStartHour(startHour);
        mEntryDetailsViewModel.setEndHour(endHour);
        mEntryDetailsViewModel.setStartMinute(startMinute);
        mEntryDetailsViewModel.setEndMinute(endMinute);
    }

    /**
     * Navigate to DatePicker Fragment and change date button text.
     *
     * @param view the date picker button view
     */
    private void pickDate(View view) {
        NavController navController = Navigation.findNavController(view);

        final NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.entryDetailsFragment);

        final LifecycleEventObserver observer = (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_RESUME)
                    && navBackStackEntry.getSavedStateHandle().contains("date")) {
                String result = navBackStackEntry.getSavedStateHandle().get("date");
                mBtnEntryDate.setText(result);
            }
        };

        navBackStackEntry.getLifecycle().addObserver(observer);

        getViewLifecycleOwner().getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                navBackStackEntry.getLifecycle().removeObserver(observer);
            }
        });

        navController.navigate(R.id.datePickerAction);
    }

    /**
     * Navigate to Time Picker Fragment and change time button text.
     *
     * @param view        the time picker button view
     * @param isStartTime the type of time picker clicked (Start: true / End: false).
     */
    private void pickTime(View view, boolean isStartTime) {
        EntryDetailsFragmentDirections.TimePickerAction action = EntryDetailsFragmentDirections.timePickerAction();
        action.setIsStartTime(isStartTime);

        NavController navController = Navigation.findNavController(view);

        final NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.entryDetailsFragment);

        final LifecycleEventObserver observer = (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_RESUME)) {
                if (navBackStackEntry.getSavedStateHandle().contains("start_time")) {
                    String result = navBackStackEntry.getSavedStateHandle().get("start_time");

                    mBtnStartTime.setText(result);
                }

                if (navBackStackEntry.getSavedStateHandle().contains("end_time")) {
                    String result = navBackStackEntry.getSavedStateHandle().get("end_time");

                    mBtnEndTime.setText(result);
                }
            }
        };

        navBackStackEntry.getLifecycle().addObserver(observer);

        getViewLifecycleOwner().getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                navBackStackEntry.getLifecycle().removeObserver(observer);
            }
        });

        navController.navigate(action);
    }

    /**
     * Save entry to DB and navigate back to EntryListFragment.
     *
     * @param view the save entry button view
     */
    private void saveDetails(View view) {
        if (mEditTitle.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "Please enter the title!", Toast.LENGTH_SHORT).show();
        } else if (mEntryDetailsViewModel.getDay() == -1) {
            Toast.makeText(getContext(), "Please select the date!", Toast.LENGTH_SHORT).show();
        } else if (mEntryDetailsViewModel.getStartHour() == -1) {
            Toast.makeText(getContext(), "Please select the start time!", Toast.LENGTH_SHORT).show();
        } else if (mEntryDetailsViewModel.getEndHour() == -1) {
            Toast.makeText(getContext(), "Please select the end time!", Toast.LENGTH_SHORT).show();
        } else {
            mEntry.setTitle(mEditTitle.getText().toString().trim());
            mEntry.setYear(mEntryDetailsViewModel.getYear());
            mEntry.setMonth(mEntryDetailsViewModel.getMonth());
            mEntry.setDay(mEntryDetailsViewModel.getDay());
            mEntry.setStartHour(mEntryDetailsViewModel.getStartHour());
            mEntry.setEndHour(mEntryDetailsViewModel.getEndHour());
            mEntry.setStartMinute(mEntryDetailsViewModel.getStartMinute());
            mEntry.setEndMinute(mEntryDetailsViewModel.getEndMinute());

            mEntryDetailsViewModel.saveEntry(mEntry);

            goBack();
        }
    }

    private void updateUI() {
        mEditTitle.setText(mEntry.getTitle());
    }

    /**
     * Pop this fragment from stack
     */
    private void goBack() {
        Navigation.findNavController(requireView()).popBackStack(R.id.entryDetailsFragment, true);
    }
}