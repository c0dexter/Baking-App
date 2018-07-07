package pl.michaldobrowolski.bakingapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.service.ApiClient;
import pl.michaldobrowolski.bakingapp.api.service.ApiInterface;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeMasterListAdapter;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.StepsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeMasterListFragment extends android.support.v4.app.Fragment implements RecipeMasterListAdapter.MasterListAdapterOnClickHandler {
    private final String TAG = this.getClass().getSimpleName();

    // Items mapping
    public Context mContext;
    private ApiInterface mApiInterface;
    private List<Recipe> mRecipeList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecipeMasterListAdapter mAdapter;
    private Call<List<Recipe>> call;

    public RecipeMasterListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Set a root view
        final View rootView = inflater.inflate(R.layout.fragment_recipe_master_list, container, false);

        // Mapping views
        mRecyclerView = rootView.findViewById(R.id.recipe_master_list_rv);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        mRecyclerView.setHasFixedSize(true);

        //mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager = new GridLayoutManager(mContext, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        fetchRecipes();
        return rootView;
    }

    private void fetchRecipes() {
        call = mApiInterface.fetchRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.body() != null) {
                    Log.d("LOG: Response Code: ", response.code() + "");
                    //fetchingData(response);
                    mRecipeList = response.body();
                    mAdapter = new RecipeMasterListAdapter(mRecipeList, RecipeMasterListFragment.this);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(mContext, "Error. Fetching data failed :(", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                call.cancel();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    @Override
    public void onClickRecipe(int recipeCardPosition) {
        Log.i(TAG, "The " + mRecipeList.get(recipeCardPosition).getmName() + "has been clicked");
        Toast.makeText(mContext, "Recipe of " + mRecipeList.get(recipeCardPosition).getmName(), Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putParcelable("recipeSteps", mRecipeList.get(recipeCardPosition));

        final Intent intent = new Intent(getContext(), StepsActivity.class);
        intent.putExtra("recipe", bundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
