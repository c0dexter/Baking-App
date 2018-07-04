package pl.michaldobrowolski.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;

public class RecipeStepListFragment extends Fragment implements RecipeStepListAdapter.StepListAdapterOnClickHandler {
    final static String TAG = RecipeStepListFragment.class.getSimpleName();
    private static final String STEP_LIST_BUNDLE_KEY = "steps_list";
    private static final String RECIPE_NAME_BUNDLE_KEY = "recipe_name";

    // Properties
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private RecipeStepListAdapter mAdapter;
    private Bundle bundle;
    private String mRecipeName;


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

        getRecipeDataFromBundle(STEP_LIST_BUNDLE_KEY, RECIPE_NAME_BUNDLE_KEY);

        // Set a title on NavBar
        ((StepsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName);

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

//        Bundle bundle = new Bundle();
//        bundle.putParcelable("recipeSteps", mStepList.get(stepPosition));
//
//        final Intent intent = new Intent(getContext(), StepDetail.class); // Here should be StepDetail class
//        intent.putExtra("step", bundle);
//        startActivity(intent);
    }

    /**
     * Methods for getting recipe objects from bundle and saving those in the List
     *
     * @param bundleStepListKey   - String key of bundle
     * @param bundleRecipeNameKey - String key of Parcelable objects
     */
    private void getRecipeDataFromBundle(String bundleStepListKey, String bundleRecipeNameKey) {

        bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(bundleStepListKey) && bundle.containsKey(bundleRecipeNameKey)) {
                mRecipeName = bundle.getString(bundleRecipeNameKey);
                mStepList.addAll(bundle.getParcelableArrayList(bundleStepListKey));
            } else {
                Log.i(TAG, "Cannot get data from bundle. Incorrect bundle key/keys string.");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }
    }
}
