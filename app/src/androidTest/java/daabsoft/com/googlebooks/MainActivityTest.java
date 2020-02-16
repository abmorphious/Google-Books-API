package daabsoft.com.googlebooks;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
@Rule
public ActivityTestRule<MainActivity> activityTestRule =
        new ActivityTestRule(MainActivity.class);
@Test
public void testMenu()
{
    //Why it doesn't work???????

    onView(withId(R.id.recyclerview)).perform(RecyclerViewActions
            .actionOnItemAtPosition(0, click()));

    onView(withId(R.id.book_p_title)).check(matches(withText("Effective java")));
}
}