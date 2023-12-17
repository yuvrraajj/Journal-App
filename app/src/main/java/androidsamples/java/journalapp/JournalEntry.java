package androidsamples.java.journalapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Entity(tableName = "journal_table")
public class JournalEntry {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private UUID mUid;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "year")
    private Integer mYear;

    @ColumnInfo(name = "month")
    private Integer mMonth;

    @ColumnInfo(name = "day")
    private Integer mDay;

    @ColumnInfo(name = "startHour")
    private Integer mStartHour;

    @ColumnInfo(name = "endHour")
    private Integer mEndHour;

    @ColumnInfo(name = "startMinute")
    private Integer mStartMinute;

    @ColumnInfo(name = "endMinute")
    private Integer mEndMinute;

    public JournalEntry(String title, Integer year, Integer month, Integer day, Integer startHour, Integer endHour, Integer startMinute, Integer endMinute) {
        this.mUid = UUID.randomUUID();
        this.mTitle = title;
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mStartHour = startHour;
        this.mEndHour = endHour;
        this.mStartMinute = startMinute;
        this.mEndMinute = endMinute;
    }

    @NonNull
    public UUID getUid() {
        return mUid;
    }

    public void setUid(@NonNull UUID id) {
        this.mUid = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Integer getYear() {
        return mYear;
    }

    public void setYear(Integer year) {
        this.mYear = year;
    }

    public Integer getMonth() {
        return mMonth;
    }

    public void setMonth(Integer month) {
        this.mMonth = month;
    }

    public Integer getDay() {
        return mDay;
    }

    public void setDay(Integer day) {
        this.mDay = day;
    }

    public Integer getStartHour() {
        return mStartHour;
    }

    public void setStartHour(Integer startHour) {
        this.mStartHour = startHour;
    }

    public Integer getEndHour() {
        return mEndHour;
    }

    public void setEndHour(Integer endHour) {
        this.mEndHour = endHour;
    }

    public Integer getStartMinute() {
        return mStartMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.mStartMinute = startMinute;
    }

    public Integer getEndMinute() {
        return mEndMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.mEndMinute = endMinute;
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