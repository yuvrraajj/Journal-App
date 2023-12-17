package androidsamples.java.journalapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TimePickerFragment extends DialogFragment {

    private EntryDetailsViewModel mEntryDetailsViewModel;
    private boolean isStartTime;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mEntryDetailsViewModel = new ViewModelProvider(requireActivity()).get(EntryDetailsViewModel.class);

        isStartTime = TimePickerFragmentArgs.fromBundle(getArguments()).getIsStartTime();

        int hour = isStartTime ? mEntryDetailsViewModel.getStartHour() : mEntryDetailsViewModel.getEndHour();
        int minute = isStartTime ? mEntryDetailsViewModel.getStartMinute() : mEntryDetailsViewModel.getEndMinute();

        if (hour == -1 && minute == -1) {
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(requireContext(), this::onTimePicked, hour, minute, true);
    }

    private void onTimePicked(TimePicker timePicker, int hour, int minute) {
        if (isStartTime) {
            mEntryDetailsViewModel.setStartHour(hour);
            mEntryDetailsViewModel.setStartMinute(minute);
        } else {
            mEntryDetailsViewModel.setEndHour(hour);
            mEntryDetailsViewModel.setEndMinute(minute);
        }

        NavController navController = NavHostFragment.findNavController(this);
        SavedStateHandle savedStateHandle = Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle();

        savedStateHandle.set(isStartTime  ? "start_time" : "end_time",
                mEntryDetailsViewModel.getFullTimeString(isStartTime));
    }
}