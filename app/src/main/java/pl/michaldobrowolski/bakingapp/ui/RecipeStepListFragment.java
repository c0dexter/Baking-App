package pl.michaldobrowolski.bakingapp.ui;

import android.content.Context;
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

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;

public class RecipeStepListFragment extends Fragment implements RecipeStepListAdapter.StepListAdapterOnClickHandler {
    private final String TAG = this.getClass().getSimpleName();

    // Properties
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Step> mStepList;
    private RecipeStepListAdapter mAdapter;
    private Bundle bundle;

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

        if (getArguments() != null) {
            mStepList = getArguments().getParcelable("steps");
            mAdapter.setSteps(mStepList);
        }
        else {
            Log.i(TAG, "Error with getting argument with bundle");
        }

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
        Toast.makeText(mContext, "Step #" + stepPosition + " has been clicked.", Toast.LENGTH_SHORT).show();
    }
}
