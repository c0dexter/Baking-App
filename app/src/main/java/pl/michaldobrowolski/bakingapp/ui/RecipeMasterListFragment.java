package pl.michaldobrowolski.bakingapp.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.service.ApiClient;
import pl.michaldobrowolski.bakingapp.api.service.ApiInterface;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeMasterListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class RecipeMasterListFragment extends Fragment implements RecipeMasterListAdapter.MasterListAdapterOnClickHandler {
    private final String TAG = this.getClass().getSimpleName();

    public Context mContext;
    //RecipeMasterListAdapter.MasterListAdapterOnClickHandler masterListAdapterOnClickHandler;
    // Items mapping
    private ApiInterface mApiInterface;
    private List<Recipe> mRecipeList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecipeMasterListAdapter mAdapter;
    private Call<List<Recipe>> call;

    public RecipeMasterListFragment() {
    }
    // TODO: check butterknife here

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        // Set a root view
        final View rootView = inflater.inflate(R.layout.recipe_master_list_fragment, container, false);
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
        //mRecyclerView.setLayoutManager(mLayoutManager);
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
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                call.cancel();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }


    @Override
    public void onClickRecipe(int recipeCardPosition) {
        Log.i(TAG, "The " + mRecipeList.get(recipeCardPosition).getmName() + "has been clicked" );
        Toast.makeText(mContext, "AAAAAAAA", Toast.LENGTH_SHORT).show();
    }
}
