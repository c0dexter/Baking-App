package pl.michaldobrowolski.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.michaldobrowolski.bakingapp.R;

public class RecipeStepListFragment extends Fragment {

    // Properties
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

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

        // Mapping views
        mRecyclerView = rootView.findViewById(R.id.recipe_steps_list_rv);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false); // implement step view
        mRecyclerView.setLayoutManager(mLayoutManager);



        return rootView;
    }
}
