package pl.michaldobrowolski.bakingapp.ui.recipe.steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    Bundle stepDetailDefaultBundle;
    boolean mTwoPane;
    // Properties
    private Bundle bundle;
    private Bundle stepsBundle;
    private Recipe mRecipe;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private boolean mFragmentAdded;
    private StepDetailsFragment stepDetailsFragment;
    private FragmentManager mFragmentManager;
    // ------------------ End Of Properties ------------------ //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_steps);

        if (savedInstanceState != null) {
            mFragmentAdded = savedInstanceState.getBoolean("fragment_added");
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
            mFragmentManager = getSupportFragmentManager();
            if (savedInstanceState == null) {
                stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setArguments(sendDefaultData());
                mFragmentManager.beginTransaction()
                        .add(R.id.step_details_container, stepDetailsFragment)
                        .commit();
            } else {
                stepDetailsFragment.setArguments(sendDefaultData()); // Here bundle with Id
                mFragmentManager.beginTransaction()
                        .add(R.id.step_details_container, stepDetailsFragment)
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
                Toast.makeText(getApplicationContext(), "Ingredients Card View", Toast.LENGTH_SHORT).show();
                Log.i("CARD VIEW", "Ingredients Card View has been  clicked");
                startActivity(ingredientIntent);
            }
        });
    }

    public Bundle sendDefaultData() {
        stepDetailDefaultBundle = new Bundle();
        stepDetailDefaultBundle.putString("recipe_name_bundle_key", mRecipe.getmName());
        stepDetailDefaultBundle.putString("desc_bundle", mRecipe.getmSteps().get(0).getmDescription());
        stepDetailDefaultBundle.putString("video_url_bundle", mRecipe.getmSteps().get(0).getmVideoURL());
        stepDetailDefaultBundle.putString("thumbnail_url_bundle", mRecipe.getmSteps().get(0).getThumbnailURL());
        stepDetailDefaultBundle.putInt("step_id_bundle_key", mRecipe.getmSteps().get(0).getId());
        stepDetailDefaultBundle.putInt("total_steps_bundle_key", mRecipe.getmSteps().size());
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

    /**
     * Methods for getting recipe objects from bundle and saving those in the List
     *
     * @param bundleKey           - String key of bundle
     * @param bundleParcelableKey - String key of Parcelable objects
     */
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
    }

    @Override
    public void onStepSelected(String recipeName, int stepPosition) {
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
