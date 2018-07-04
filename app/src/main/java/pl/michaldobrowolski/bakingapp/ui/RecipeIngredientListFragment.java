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
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeIngredientListAdapter;

public class RecipeIngredientListFragment extends Fragment implements RecipeIngredientListAdapter.IngredientListAdapterOnClickHandler {

    // Properties
    private static final String BUNDLE_INGREDIENTS_LIST_KEY = "ingredient_list";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name";
    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private RecipeIngredientListAdapter mAdapter;
    private Bundle mIngredientsBundle;
    private String mRecipeName;

    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public RecipeIngredientListFragment() {
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
        final View rootView = inflater.inflate(R.layout.recipe_ingredient_list_fragment, container, false);

        getIngredientsDataFromBundle(BUNDLE_INGREDIENTS_LIST_KEY, BUNDLE_RECIPE_NAME_KEY);

        // Set a title on NavBar
        ((IngredientsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName + "'s ingredients");

        // Mapping views
        mRecyclerView = rootView.findViewById(R.id.recipe_ingredient_list_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecipeIngredientListAdapter(RecipeIngredientListFragment.this, mIngredientList);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onClickIngredient(int ingredientPosition) {
        Toast.makeText(mContext, "Step #" + String.valueOf(ingredientPosition + 1) + " has been clicked.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Methods for getting recipe objects from mIngredientsBundle and saving those in the List
     *
     * @param bundleIngredientsListKey - String key of Ingredients
     * @param bundleRecipeNameKey      - String key of Recipe Name
     */
    private void getIngredientsDataFromBundle(String bundleIngredientsListKey, String bundleRecipeNameKey) {
        mIngredientsBundle = getArguments();
        if (mIngredientsBundle != null) {
            if (mIngredientsBundle.containsKey(bundleIngredientsListKey) && mIngredientsBundle.containsKey(bundleRecipeNameKey)) {
                mIngredientList.addAll(mIngredientsBundle.getParcelableArrayList(bundleIngredientsListKey));
                mRecipeName = mIngredientsBundle.getString(bundleRecipeNameKey);
            } else {
                Log.i(TAG, "Cannot get data from mIngredientsBundle. Incorrect mIngredientsBundle key/keys string.");
            }
        } else {
            Log.i(TAG, "Bundle is NULL");
        }
    }
}
