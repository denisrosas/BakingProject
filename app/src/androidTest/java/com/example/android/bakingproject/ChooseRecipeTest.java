package com.example.android.bakingproject;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingproject.IndlingResource.SimpleIdlingResource;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingproject.MainActivity.globalRecipeDetailsList;

@RunWith(AndroidJUnit4.class)
public class ChooseRecipeTest {

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

        ArrayList<String> recipeNames = new ArrayList<>();
        int index;

        for(index=0; index< globalRecipeDetailsList.size(); index++){
            recipeNames.add((globalRecipeDetailsList.get(index).getName()));
        }

        index = 0;
        for(String recipeName : recipeNames) {

            onView(withRecyclerView(R.id.rv_recipes_list).atPosition(index)).
                    check(matches(hasDescendant(withText(recipeName))));
            index++;
        }

    }

    @Test
    public void CheckStepsNames() {

        for(int index = 0; index< globalRecipeDetailsList.size(); index++){

            //choose recipe button in MainActivity
            onView(ViewMatchers.withId(R.id.rv_recipes_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(index, ViewActions.click()));

            //check is all step names are correct
            for (int index2 = 0; index2 < globalRecipeDetailsList.get(index).getRecipeSteps().size(); index2++) {

                if (index2 == 0) {
                    onView(withRecyclerView(R.id.rv_recipe_steps_list).atPosition(index2)).
                            check(matches(hasDescendant(withText("Ingredient List"))));
                }else {
                    onView(withRecyclerView(R.id.rv_recipe_steps_list).atPosition(index2)).
                            check(matches(hasDescendant(withText(globalRecipeDetailsList.get(index).
                                    getRecipeSteps().get(index2 - 1).getShortDescription()))));
                }
            }

            //back to main activity
            onView(ViewMatchers.withId(R.id.tv_recipe_name)).perform(ViewActions.pressBack());
        }
    }
}
