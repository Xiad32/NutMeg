package com.example.nutmeg;


import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class UIMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<> (MainActivity.class, false, false);

    @Test
    public void clickSwitchTabs(){
        Intent callerIntent = new Intent();
        callerIntent.putExtra(RecipesList.RECIPE_KEY, 1);
        mainActivityActivityTestRule.launchActivity(callerIntent);

        //check if it is on the recipe tab:
        onView((withId(R.id.ingredientsLabelTV))).check(matches(isDisplayed()));
        //swipe right anywhere:
        onView((withId(R.id.container))).perform(swipeLeft());
        //check if the correct tab instructions exist:
        onView((withId(R.id.nextTV))).check(matches(isDisplayed()));

        //move back to first tab
        onView((withId(R.id.container))).perform(swipeRight());
        //check if the correct tab recipe exist:
        onView((withId(R.id.ingredientsLabelTV))).check(matches(isDisplayed()));


    }

}
