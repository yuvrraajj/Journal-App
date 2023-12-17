package androidsamples.java.journalapp;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

import static androidsamples.java.journalapp.CustomAssertions.doesNotExist;

import android.content.pm.ActivityInfo;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class InstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void enableAccessibilityChecks() {
        AccessibilityChecks.enable();
    }

    @Test
    public void Test1() {
        onView(withId(R.id.menu_info)).perform(click());
        onView(withId(R.id.btn_go_back)).check(matches(withText(R.string.go_back)));
    }

    @Test
    public void Test2() {
        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.record_entry)).check(matches(withText(R.string.record_entry)));
    }

    @Test
    public void TestEntry() {
        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.edit_title)).perform(typeText("Test Entry"), closeSoftKeyboard());
        onView(withId(R.id.btn_entry_date)).perform(click());

        // Set the desired date in the date picker dialog
        int yearToSet = 2023; // Replace with the desired year
        int monthToSet = Calendar.MARCH; // Replace with the desired month (e.g., Calendar.MARCH for March)
        int dayToSet = 15;
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(yearToSet, monthToSet + 1, dayToSet));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());

        // Set the desired start time
        int hourToSet = 10; // Replace with the desired hour
        int minuteToSet = 30; // Replace with the desired minute

        // Use PickerActions to set the time in the time picker
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hourToSet, minuteToSet));


        onView(withText("OK")).perform(click());

        // Click the button to open the time picker dialog for the end time
        onView(withId(R.id.btn_end_time)).perform(click());

        // Set the desired end time
        int endHourToSet = 12; // Replace with the desired end hour
        int endMinuteToSet = 0; // Replace with the desired end minute

        // Use PickerActions to set the time in the time picker
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(endHourToSet, endMinuteToSet));

        // Click the "OK" or "Set" button to confirm the end time selection (if applicable)
        onView(withText("OK")).perform(click());

        // Click the "Save" button to save the entry
        onView(withId(R.id.btn_save)).perform(click());

    }
    @Test
    public void testRotation() {
        // Launch the activity
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        // Click the menu_info button to display the desc TextView
        onView(withId(R.id.menu_info)).perform(click());

        // Get the text from the desc TextView (s1)
        onView(withId(R.id.desc)).check(matches(isDisplayed()));
        String s1 = onView(withId(R.id.desc)).check(matches(withText(not(isEmptyString())))).toString();

        // Rotate the device to landscape mode
        scenario.onActivity(activity -> {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        });

        // Get the text from the desc TextView after rotation (s2)
        onView(withId(R.id.desc)).check(matches(isDisplayed()));
    }

    @Test
    public void testDataPersistenceOnClosing() {
        // Create a journal entry
        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.edit_title)).perform(typeText("Data Persistence Test"), closeSoftKeyboard());
        onView(withId(R.id.btn_entry_date)).perform(click());

        // Set a date in the date picker
        int yearToSet = 2023;
        int monthToSet = Calendar.MARCH;
        int dayToSet = 15;
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(yearToSet, monthToSet + 1, dayToSet));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());

        // Set a start time in the time picker
        int hourToSet = 10;
        int minuteToSet = 30;
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hourToSet, minuteToSet));
        onView(withText("OK")).perform(click());

        // Save the entry
        onView(withId(R.id.btn_save)).perform(click());

        // Close the app
         pressBack();

        // Reopen the app
        ActivityScenario<MainActivity> scenario = activityRule.getScenario();

        // Verify that the created entry is still present
        onView(withText("Data Persistence Test")).check(matches(isDisplayed()));
    }
    @Test
    public void testEntryDeletion() {
        // Create a journal entry for deletion
        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.edit_title)).perform(typeText("Entry to Delete"), closeSoftKeyboard());
        onView(withId(R.id.btn_entry_date)).perform(click());

        // Set a date in the date picker
        int yearToSet = 2023;
        int monthToSet = Calendar.MARCH;
        int dayToSet = 15;
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(yearToSet, monthToSet + 1, dayToSet));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());

        // Set a start time in the time picker
        int hourToSet = 10;
        int minuteToSet = 30;
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(hourToSet, minuteToSet));
        onView(withText("OK")).perform(click());

        // Save the entry
        onView(withId(R.id.btn_save)).perform(click());

        // Verify that the created entry is displayed in the journal
        onView(withText("Entry to Delete")).check(matches(isDisplayed()));

        // Delete the entry
        onView(withText("Entry to Delete")).perform(longClick());
        onView(withText(R.string.delete)).perform(click());

        // Verify that the entry is no longer present in the journal
        onView(withText("Entry to Delete")).check(doesNotExist());
    }




    @Test
    public void goToDetails() {
       onView(withId(R.id.btn_add_entry));

    }

}