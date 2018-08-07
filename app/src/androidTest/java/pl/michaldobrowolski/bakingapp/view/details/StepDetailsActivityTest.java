package pl.michaldobrowolski.bakingapp.view.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.RecipeStaticObjectPattern;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsActivity;
import pl.michaldobrowolski.bakingapp.utils.UtilityHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StepDetailsActivityTest {
    private Recipe mRecipe;
    private int mStepPosition;
    private String mRecipeName;
    private ArrayList<Step> mStepList = new ArrayList<>();
    UtilityHelper utilityHelper = new UtilityHelper();

    @Rule
    // launch activity: false
    public ActivityTestRule<StepDetailsActivity> ActivityRule =
            new ActivityTestRule<>(StepDetailsActivity.class, false, false);

    @Before
    public void setUp() {
        mRecipe = RecipeStaticObjectPattern.getRecipeStaticData();
        mStepList = (ArrayList<Step>) mRecipe.getmSteps();
        mRecipeName = mRecipe.getmName();
        mStepPosition = mRecipe.getmId();

        Bundle stepDetailBundle = new Bundle();
        stepDetailBundle.putString(StepDetailsActivity.BUNDLE_RECIPE_NAME_KEY, mRecipeName);
        stepDetailBundle.putParcelableArrayList(StepDetailsActivity.BUNDLE_ARRAY_STEPS_KEY, mStepList);
        stepDetailBundle.putInt(StepDetailsActivity.BUNDLE_STEP_ID_KEY, mStepPosition);

        final Intent intent = new Intent();
        intent.putExtra(StepDetailsActivity.MAIN_BUNDLE_KEY, stepDetailBundle);
        ActivityRule.launchActivity(intent);
    }

    @Test
    public void checkStepDetailsForClickedStep(){
        onView(withId(R.id.text_step_full_desc)).
                // Use utilityHelper.removeRedundantCharactersFromText, because I use it on UI
                check(matches(withText(utilityHelper.removeRedundantCharactersFromText("^(\\d*.\\s)", mRecipe.getmSteps().get(mStepPosition).getmDescription()))));
    }
}
