package pl.michaldobrowolski.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;

public class StepsActivity extends AppCompatActivity implements RecipeStepListAdapter.StepListAdapterOnClickHandler {

    private Bundle bundle;
    private Recipe mRecipe;
    private List<Step> mStepList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        getRecipeFromBundleAndExtractStepList("recipe");

    }

    @Override
    public void onClickStep(int stepPosition) {
        Toast.makeText(this, "Step: " + stepPosition + "clicked!", Toast.LENGTH_SHORT).show();
        // TODO: make a BUNDLE and save data which I need in the Ingredients detail screen
        // probably I need save a Step List with Step objects
    }

    /**
     * This method get a recipe form bundle and extracts list of Steps
     * @param bundleKey - a declared key of bundle, enter proper key to extracting bundle object
     * @return List of steps of the specific recipe
     */
    private List<Step> getRecipeFromBundleAndExtractStepList(String bundleKey){
        bundle = getIntent().getExtras();
        if(bundle != null){
            bundle.getParcelable(bundleKey);
        }
        return mStepList = mRecipe != null ? mRecipe.getmSteps() : null;
    }
}
