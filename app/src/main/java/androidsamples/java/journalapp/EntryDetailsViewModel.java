package androidsamples.java.journalapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class EntryDetailsViewModel extends ViewModel {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mStartHour;
    private int mStartMinute;
    private int mEndHour;
    private int mEndMinute;

    private final JournalRepository mRepository;
    private final MutableLiveData<UUID> entryIdLiveData = new MutableLiveData<>();

    public EntryDetailsViewModel () {
        mRepository = JournalRepository.getInstance();
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getStartHour() {
        return mStartHour;
    }

    public void setStartHour(int hour) {
        this.mStartHour = hour;
    }

    public int getStartMinute() {
        return mStartMinute;
    }

    public void setStartMinute(int minute) {
        this.mStartMinute = minute;
    }

    public int getEndHour() {
        return mEndHour;
    }

    public void setEndHour(int hour) {
        this.mEndHour = hour;
    }

    public int getEndMinute() {
        return mEndMinute;
    }

    public void setEndMinute(int minute) {
        this.mEndMinute = minute;
    }

    LiveData<JournalEntry> getEntryLiveData() {
        return Transformations.switchMap(entryIdLiveData, mRepository::getEntry);
    }

    void loadEntry(UUID entryId) {
        entryIdLiveData.setValue(entryId);
    }

    void saveEntry(JournalEntry entry) {
        mRepository.update(entry);
    }

    void deleteEntry(JournalEntry entry) {
        mRepository.delete(entry);
    }

    /**
     * Generates a date string to display in entry card.
     * @return generated date string
     */
    public String getFullDateString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DATE, mDay);

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Generates a time string to display in card.
     * @param isStartTime type of time string (Start: true, End: false
     * @return generated time string
     */
    public String getFullTimeString(boolean isStartTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, isStartTime ? mStartHour : mEndHour);
        calendar.set(Calendar.MINUTE, isStartTime ? mStartMinute : mEndMinute);

        return simpleDateFormat.format(calendar.getTime());
    }
}