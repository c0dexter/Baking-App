package pl.michaldobrowolski.bakingapp.ui.recipe.steps.ingredeints;

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

public class IngredientListFragment extends Fragment implements RecipeIngredientListAdapter.IngredientListAdapterOnClickHandler {
    // Bundle keys
    private static final String BUNDLE_INGREDIENTS_LIST_KEY = "fragment_ingredient_list";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name";
    // -------------------- Properties --------------------//
    private final String TAG = this.getClass().getSimpleName();
    // Properties
    private Context mContext;
    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private String mRecipeName;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public IngredientListFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        getIngredientsDataFromBundle(BUNDLE_INGREDIENTS_LIST_KEY, BUNDLE_RECIPE_NAME_KEY);

        // Set a title on NavBar
        ((IngredientsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName + "'s ingredients");

        // Mapping views
        RecyclerView mRecyclerView = rootView.findViewById(R.id.recipe_ingredient_list_rv);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Adapter + Rv
        RecipeIngredientListAdapter mAdapter = new RecipeIngredientListAdapter(IngredientListFragment.this, mIngredientList);
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
        Bundle mIngredientsBundle = getArguments();
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
