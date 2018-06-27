package pl.michaldobrowolski.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;

public class StepsActivity extends AppCompatActivity implements RecipeStepListAdapter.StepListAdapterOnClickHandler {
    private static final String BUNDLE_KEY = "recipe";
    private static final String BUNDLE_PARCELABLE_KEY = "recipeSteps";
    private final String TAG = this.getClass().getSimpleName();
    private Bundle bundle;
    private Recipe mRecipe;
    private List<Step> mStepList;
    private List<Ingredient> mIngredientList;

    // Getters
    public List<Step> getmStepList() {
        return mStepList;
    }
    public List<Ingredient> getmIngredientList() {
        return mIngredientList;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        getRecipeFromBundle(BUNDLE_KEY, BUNDLE_PARCELABLE_KEY);
        setStepListFromRecipe(mRecipe);
        setIngredientListFromRecipe(mRecipe);
    }

    @Override
    public void onClickStep(int stepPosition) {
        Toast.makeText(this, "Step: " + stepPosition + "click" +
                "ed!", Toast.LENGTH_SHORT).show();
        // TODO: make a BUNDLE and save data which I need in the Ingredients detail screen
        // probably I need save a Step List with Step objects
    }

    /**
     * This method get a recipe form bundle and extracts list of Steps
     *
     * @param bundleKey           - String key for extracting bundle object
     * @param bundleParcelableKey - String key for extracting parcelable object from bundle
     */
    private void getRecipeFromBundle(String bundleKey, String bundleParcelableKey) {

        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(bundleKey)) {
                bundle = bundle.getBundle(bundleKey);
                mRecipe = bundle != null ? bundle.getParcelable(bundleParcelableKey) : null;
            } else {
                Log.i(TAG, "Cannot get object from bundle, because the bundle is NULL");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }
    }

    /**
     * Method for getting steps from Recipe object and setting the list of steps of the recipe
     *
     * @param recipe - recipe object from bundle
     */
    private void setStepListFromRecipe(Recipe recipe) {
        mStepList = recipe != null ? recipe.getmSteps() : null;
    }

    /**
     * Method for getting steps from Recipe object and setting the list of ingredients of the recipe
     *
     * @param recipe - recipe object from bundle
     */
    private void setIngredientListFromRecipe(Recipe recipe) {
        mIngredientList = recipe != null ? recipe.getmIngredients() : null;
    }

    //TODO: Send steps list to steps Adapter

}
