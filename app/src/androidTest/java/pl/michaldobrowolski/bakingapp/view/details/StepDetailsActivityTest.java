package pl.michaldobrowolski.bakingapp.view.details;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

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
public class StepDetailsActivityTest extends ActivityInstrumentationTestCase2<StepDetailsActivity> {
    @Rule
    // launch activity: false
    public ActivityTestRule<StepDetailsActivity> ActivityRule =
            new ActivityTestRule<>(StepDetailsActivity.class, false, false);
    private Recipe mRecipe;
    private int mStepPosition;
    private boolean isTest;
    private String mRecipeName;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private UtilityHelper mUtilityHelper = new UtilityHelper();

    public StepDetailsActivityTest() {
        super(StepDetailsActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        mRecipe = RecipeStaticObjectPattern.getRecipeStaticData();
        mStepList = (ArrayList<Step>) mRecipe.getmSteps();
        mRecipeName = mRecipe.getmName();
        mStepPosition = mRecipe.getmId();
        isTest = true;

        Bundle stepDetailBundle = new Bundle();
        stepDetailBundle.putString(StepDetailsActivity.BUNDLE_RECIPE_NAME_KEY, mRecipeName);
        stepDetailBundle.putParcelableArrayList(StepDetailsActivity.BUNDLE_ARRAY_STEPS_KEY, mStepList);
        stepDetailBundle.putInt(StepDetailsActivity.BUNDLE_STEP_ID_KEY, mStepPosition);
        stepDetailBundle.putBoolean(StepDetailsActivity.BUNDLE_TABLET_TEST_KEY, isTest);

        final Intent intent = new Intent();
        intent.putExtra(StepDetailsActivity.MAIN_BUNDLE_KEY, stepDetailBundle);
        ActivityRule.launchActivity(intent).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void checkStepDetailsForClickedStep() throws InterruptedException {

        // Use utilityHelper.removeRedundantCharactersFromText, because I use it on UI
        String excepted = mUtilityHelper.removeRedundantCharactersFromText(
                "^(\\d*.\\s)",
                mRecipe.getmSteps().get(mStepPosition).getmDescription());
        Thread.sleep(10000);
        onView(withId(R.id.text_step_full_desc)).
                check(matches(withText(excepted)));
    }
}
