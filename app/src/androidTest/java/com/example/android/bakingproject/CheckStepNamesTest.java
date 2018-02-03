package com.example.android.bakingproject;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.ViewActions;
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

/**
 * Created by Denis on 03/02/2018.
 */

@RunWith(AndroidJUnit4.class)
public class CheckStepNamesTest {

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
    public void CheckStepsNames() {

        //Its necessary to run a onView before accessing any variable of MainActivity
        //If we access globalRecipeDetailsList() directly, it does not check IdlingResource variable
        onView(ViewMatchers.withId(R.id.rv_recipes_list)).
                perform(RecyclerViewActions.scrollToPosition(0));

        for(int recipeId = 0; recipeId< globalRecipeDetailsList.size(); recipeId++){

            onView(ViewMatchers.withId(R.id.rv_recipes_list)).
                    perform(RecyclerViewActions.scrollToPosition(recipeId));

            //choose recipe button in MainActivity
            onView(ViewMatchers.withId(R.id.rv_recipes_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(recipeId, ViewActions.click()));

            //check is all step names are correct
            for (int stepId = 0; stepId < globalRecipeDetailsList.get(recipeId).getRecipeSteps().size(); stepId++) {

                //first scroll to the specific position of the recyclerView
                onView(ViewMatchers.withId(R.id.rv_recipe_steps_list)).
                        perform(RecyclerViewActions.scrollToPosition(stepId));

                //then check if the name is correct
                if (stepId == 0) {
                    onView(withRecyclerView(R.id.rv_recipe_steps_list).atPosition(stepId)).
                            check(matches(hasDescendant(withText(activityTestRule.
                                    getActivity().getString(R.string.ingredient_list)))));

                }else {

                    onView(withRecyclerView(R.id.rv_recipe_steps_list).atPosition(stepId)).
                            check(matches(hasDescendant(withText(globalRecipeDetailsList.get(recipeId).
                                    getRecipeSteps().get(stepId - 1).getShortDescription()))));
                }

            }
            //back to main activity
            onView(ViewMatchers.withId(R.id.tv_recipe_name)).perform(ViewActions.pressBack());
        }
    }
}
