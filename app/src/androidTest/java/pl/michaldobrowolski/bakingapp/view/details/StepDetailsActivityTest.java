package pl.michaldobrowolski.bakingapp.view.details;

import android.content.Intent;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.RecipeStaticObjectPattern;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class StepDetailsActivityTest {
    private Recipe mRecipe;
    private final int STEP_INDEX = 3;

    @Rule
    // launch activity: false
    public ActivityTestRule<StepDetailsActivity> ActivityRule =
            new ActivityTestRule<>(StepDetailsActivity.class, false, false);

    @Before
    public void setUp() {
        mRecipe = RecipeStaticObjectPattern.getRecipeStaticData();
        final Intent intent = new Intent();
        intent.putExtra(StepDetailsActivity.MAIN_BUNDLE_KEY, new Gson().toJson(mRecipe.getmSteps()));
        intent.putExtra(StepDetailsActivity.BUNDLE_STEP_ID_KEY, STEP_INDEX);
        ActivityRule.launchActivity(intent);
    }

    @Test
    public void checkStepDetailsForClickedStep(){

        onView(withId(R.id.text_step_full_desc)).
                check(matches(withText(mRecipe.getmSteps().get(STEP_INDEX).getmDescription())));
    }
}
