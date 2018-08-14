package pl.michaldobrowolski.bakingapp.ui.recipe.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsActivity;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsFragment;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.ingredeints.IngredientsActivity;

public class StepsActivity extends AppCompatActivity implements StepListFragment.OnStepClickListener {
    // -------------------- Properties --------------------//
    final static String TAG = StepsActivity.class.getSimpleName();
    // Bundle Keys
    private static final String BUNDLE_KEY = "recipe";
    private static final String BUNDLE_PARCELABLE_KEY = "recipeSteps";
    private static final String SAVE_INSTANCE_STATE_FRAGMENT_ADDED = "fragment_added";
    private static final String SAVE_INSTANCE_STATE_CLICKED_STEP = "step_number_clicked";

    // Properties
    Bundle stepDetailDefaultBundle;
    boolean mTwoPane;
    private Bundle bundle;
    private Bundle stepsBundle;
    private Recipe mRecipe;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private boolean mFragmentAdded;
    private StepDetailsFragment stepDetailsFragment;
    private FragmentManager mFragmentManager;
    private int mSelectedStepNumber;
    // ------------------ End Of Properties ------------------ //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_steps);

        if (savedInstanceState != null) {
            mFragmentAdded = savedInstanceState.getBoolean(SAVE_INSTANCE_STATE_FRAGMENT_ADDED);
            mSelectedStepNumber = savedInstanceState.getInt(SAVE_INSTANCE_STATE_CLICKED_STEP);
        }

        bundle = getIntent().getExtras();
        stepsBundle = new Bundle();
        Bundle ingredientsBundle = new Bundle();

        getRecipeFromBundle(BUNDLE_KEY, BUNDLE_PARCELABLE_KEY);
        getStepsFromRecipe(mRecipe);
        getIngredientsFromRecipe(mRecipe);

        // https://discussions.udacity.com/t/doubt-put-list-object-in-bundle/263599/8
        stepsBundle.putParcelableArrayList("steps_list", mStepList);
        stepsBundle.putString("recipe_name", mRecipe.getmName());
        ingredientsBundle.putParcelableArrayList("fragment_ingredient_list", mIngredientList);
        ingredientsBundle.putString("recipe_name", mRecipe.getmName());

        addStepListFragment(mFragmentAdded);

        if (findViewById(R.id.steps_activity_tablet_layout) != null) {
            mTwoPane = true;
            stepDetailsFragment = new StepDetailsFragment();
            mFragmentManager = getSupportFragmentManager();

            if (savedInstanceState == null) {
                stepDetailsFragment.setArguments(sendDefaultData(mSelectedStepNumber));
                mFragmentManager.beginTransaction()
                        .add(R.id.step_details_container, stepDetailsFragment)
                        .commit();
            } else {
                stepDetailsFragment.setArguments(sendDefaultData(mSelectedStepNumber));
                mFragmentManager.beginTransaction()
                        .replace(R.id.step_details_container, stepDetailsFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        // Attach the Bundle to an intent
        final Intent ingredientIntent = new Intent(this, IngredientsActivity.class);
        ingredientIntent.putExtras(ingredientsBundle);

        CardView ingredientsCardView = findViewById(R.id.ingredients_card_view);
        ingredientsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CARD VIEW", "Ingredients Card View has been  clicked");
                startActivity(ingredientIntent);
            }
        });
    }

    public Bundle sendDefaultData(int stepNumber) {
        stepDetailDefaultBundle = new Bundle();
        stepDetailDefaultBundle.putString("recipe_name_bundle_key", mRecipe.getmName());
        stepDetailDefaultBundle.putString("desc_bundle", mRecipe.getmSteps().get(stepNumber).getmDescription());
        stepDetailDefaultBundle.putString("video_url_bundle", mRecipe.getmSteps().get(stepNumber).getmVideoURL());
        stepDetailDefaultBundle.putString("thumbnail_url_bundle", mRecipe.getmSteps().get(stepNumber).getThumbnailURL());
        stepDetailDefaultBundle.putInt("step_id_bundle_key", mRecipe.getmSteps().get(stepNumber).getId());
        stepDetailDefaultBundle.putInt("total_steps_bundle_key", mRecipe.getmSteps().size());
        stepDetailDefaultBundle.putBoolean("two_pane_key", mTwoPane);

        return stepDetailDefaultBundle;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addStepListFragment(boolean fragmentExist) {
        StepListFragment stepListFragment = new StepListFragment();
        stepListFragment.setArguments(stepsBundle);
        FragmentManager fm = getSupportFragmentManager();

        if (!fragmentExist) {
            fm.beginTransaction()
                    .add(R.id.steps_list_container, stepListFragment)
                    .commit();
            mFragmentAdded = true;
        } else {
            fm.beginTransaction()
                    .replace(R.id.steps_list_container, stepListFragment)
                    .commit();
        }
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private void getRecipeFromBundle(String bundleKey, String bundleParcelableKey) {

        if (bundle != null) {
            if (bundle.containsKey(bundleKey)) {
                bundle = bundle.getBundle(bundleKey);
                mRecipe = bundle != null ? bundle.getParcelable(bundleParcelableKey) : null;
            } else {
                Log.i(TAG, "Cannot get object from bundle. Incorrect bundle key string.");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }
    }

    private void getStepsFromRecipe(Recipe recipe) {
        if (recipe != null) {
            mStepList.addAll(recipe.getmSteps());
        } else mStepList = null;
    }

    private void getIngredientsFromRecipe(Recipe recipe) {
        if (recipe != null) {
            mIngredientList.addAll(recipe.getmIngredients());
        } else {
            mIngredientList = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragment_added", mFragmentAdded);
        outState.putInt("step_number_clicked", mSelectedStepNumber);
    }

    @Override
    public void onStepSelected(String recipeName, int stepPosition) {
        mSelectedStepNumber = stepPosition;
        if (stepDetailsFragment != null) {
            stepDetailsFragment.loadData(mStepList.get(stepPosition));
        } else {
            Bundle stepDetailBundle = new Bundle();
            stepDetailBundle.putString("recipe_name_bundle_key", recipeName);
            stepDetailBundle.putParcelableArrayList("step_array_bundle_key", mStepList);
            stepDetailBundle.putInt("step_position_bundle_key", stepPosition);
            final Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("step_detail", stepDetailBundle);
            startActivity(intent);
        }
    }
}
