package pl.michaldobrowolski.bakingapp.ui.recipe.steps;

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
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsActivity;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsDescFragment;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsExoPlayerFragment;

public class StepListFragment extends Fragment implements RecipeStepListAdapter.StepListAdapterOnClickHandler {
    // ------------------ Properties ------------------ //
    final static String TAG = StepListFragment.class.getSimpleName();
    // Bundle keys
    private static final String STEP_LIST_BUNDLE_KEY = "steps_list";
    private static final String RECIPE_NAME_BUNDLE_KEY = "recipe_name";
    // Properties
    private Context mContext;
    private ArrayList<Step> mStepList = new ArrayList<>();
    private String mRecipeName;
    private boolean mTwoPan;
    private StepDetailsDescFragment mStepDetailsDescFragment;
    private StepDetailsExoPlayerFragment mStepDetailsExoPlayerFragment;

    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public StepListFragment() {
    }

    // Fragment must have: Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // Fragment must have: onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set a root view
        final View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        getRecipeDataFromBundle(STEP_LIST_BUNDLE_KEY, RECIPE_NAME_BUNDLE_KEY);

        // Set a title on NavBar
        ((StepsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName);

        // Mapping views
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recipe_steps_list_rv);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Adapter + RecyclerView
        RecipeStepListAdapter mAdapter = new RecipeStepListAdapter(StepListFragment.this, mStepList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onClickStep(int stepPosition) {
        Bundle stepDetailBundle = new Bundle();
        stepDetailBundle.putString("recipe_name_bundle_key", mRecipeName);
        stepDetailBundle.putParcelableArrayList("step_array_bundle_key", mStepList);
        stepDetailBundle.putInt("step_position_bundle_key", stepPosition);

        final Intent intent = new Intent(getContext(), StepDetailsActivity.class);
        intent.putExtra("step_detail", stepDetailBundle);
        startActivity(intent);
    }

    private void getRecipeDataFromBundle(String bundleStepListKey, String bundleRecipeNameKey) {
        Bundle bundle = getArguments();
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
