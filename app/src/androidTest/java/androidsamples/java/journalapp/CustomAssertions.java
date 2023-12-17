package androidsamples.java.journalapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CustomAssertions {

    public static ViewAssertion doesNotExist() {
        return new ViewAssertion() {
            @Override
            public void check(android.view.View view, NoMatchingViewException noViewException) {
                // If the view is not present, it means the check passes
                if (noViewException != null) {
                    return;
                }

                // If the view is present, check its visibility
                onView(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
                        .check(matches(not(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))));
            }
        };
    }
}
