package androidsamples.java.journalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

    private EntryDetailsViewModel mEntryDetailsViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mEntryDetailsViewModel = new ViewModelProvider(requireActivity()).get(EntryDetailsViewModel.class);

        int year = mEntryDetailsViewModel.getYear();
        int month = mEntryDetailsViewModel.getMonth();
        int day = mEntryDetailsViewModel.getDay();

        if (year == -1 && month == -1 && day == -1) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
        }

        return new DatePickerDialog(requireContext(), this::onDatePicked, year, month, day);
    }

    private void onDatePicked(DatePicker datePicker, int year, int month, int day) {
        mEntryDetailsViewModel.setYear(year);
        mEntryDetailsViewModel.setMonth(month);
        mEntryDetailsViewModel.setDay(day);

        NavController navController = NavHostFragment.findNavController(this);
        Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set("date", mEntryDetailsViewModel.getFullDateString());
    }
}