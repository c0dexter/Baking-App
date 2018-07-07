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
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.ingredeints.IngredientsActivity;

public class StepsActivity extends AppCompatActivity {
    final static String TAG = StepsActivity.class.getSimpleName();
    private static final String BUNDLE_KEY = "recipe";
    private static final String BUNDLE_PARCELABLE_KEY = "recipeSteps";

    private Bundle bundle;
    private Bundle stepsBundle;
    private Bundle ingredientsBundle;
    private Recipe mRecipe;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_steps);
        bundle = getIntent().getExtras();
        stepsBundle = new Bundle();
        ingredientsBundle = new Bundle();

        getRecipeFromBundle(BUNDLE_KEY, BUNDLE_PARCELABLE_KEY);
        getStepsFromRecipe(mRecipe);
        getIngredientsFromRecipe(mRecipe);

        // https://discussions.udacity.com/t/doubt-put-list-object-in-bundle/263599/8
        stepsBundle.putParcelableArrayList("steps_list", mStepList);
        stepsBundle.putString("recipe_name", mRecipe.getmName());
        ingredientsBundle.putParcelableArrayList("ingredient_list", mIngredientList);
        ingredientsBundle.putString("recipe_name", mRecipe.getmName());

        addStepListFragment();

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void addStepListFragment() {
        StepListFragment stepListFragment = new StepListFragment();
        stepListFragment.setArguments(stepsBundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.steps_list_container, stepListFragment)
                .commit();
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
}