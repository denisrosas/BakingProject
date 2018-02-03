package com.example.android.bakingproject;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;

@RunWith(AndroidJUnit4.class)
public class CheckRecipeNamesTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void RegisterIdlingResource(){
        IdlingResource mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void CheckRecipeNames(){

        onView(ViewMatchers.withId(R.id.rv_recipes_list)).
                perform(RecyclerViewActions.scrollToPosition(0));

        for(int recipeId=0; recipeId< globalRecipeDetailsList.size(); recipeId++){

            //first scroll to the specific position of the recyclerView
            onView(ViewMatchers.withId(R.id.rv_recipes_list)).
                    perform(RecyclerViewActions.scrollToPosition(recipeId));

            //check if recipe name is correct
            onView(withRecyclerView(R.id.rv_recipes_list).atPosition(recipeId)).
                    check(matches(hasDescendant(withText(globalRecipeDetailsList.get(recipeId).getName()))));
        }
    }
}
