package pl.michaldobrowolski.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;

public class RecipeStepListFragment extends Fragment implements RecipeStepListAdapter.StepListAdapterOnClickHandler {
    final static String TAG = RecipeStepListFragment.class.getSimpleName();
    private static final String BUNDLE_KEY = "recipe";
    private static final String BUNDLE_PARCELABLE_KEY = "recipeSteps";

    // Properties
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Step> mStepList;
    private RecipeStepListAdapter mAdapter;
    private Bundle bundle;
    private Recipe mRecipe;


    public RecipeStepListFragment() {
    }

    // Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Set a root view
        final View rootView = inflater.inflate(R.layout.recipe_step_list_fragment, container, false);

        getRecipeFromBundle(BUNDLE_KEY, BUNDLE_PARCELABLE_KEY);
        setStepListFromRecipe(mRecipe);

        // Set a title on NavBar
        ((StepsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipe.getmName());

        // Mapping views
        mRecyclerView = rootView.findViewById(R.id.recipe_steps_list_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecipeStepListAdapter(RecipeStepListFragment.this, mStepList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onClickStep(int stepPosition) {
        Toast.makeText(mContext, "Step #" + String.valueOf(mStepList.get(stepPosition).getId() + 1) + " has been clicked.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Methods for getting recipe objects from bundle and saving those in the List
     * @param bundleKey - String key of bundle
     * @param bundleParcelableKey - String key of Parcelable objects
     */
    private void getRecipeFromBundle(String bundleKey, String bundleParcelableKey) {

        bundle = getArguments();
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

    /**
     * Method for getting steps from Recipe object and setting the list of steps of the recipe
     *
     * @param recipe - recipe object from bundle
     */
    private void setStepListFromRecipe(Recipe recipe) {
        mStepList = recipe != null ? recipe.getmSteps() : null;
    }

}
