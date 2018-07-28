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

import java.util.ArrayList;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.details.StepDetailsActivity;

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
    // Listener
    OnStepClickListener mCallback;
    // ------------------ End Of Properties ------------------ //

    // OnStepClickListener interface, calls a method in the host activity named onStepSelected
    public interface OnStepClickListener {
        void onStepSelected(String recipeName, int stepPosition);
    }

    // Fragment must have: an empty constructor
    public StepListFragment() {
    }

    // Fragment must have: Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
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
        mCallback.onStepSelected(mRecipeName, stepPosition);
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
