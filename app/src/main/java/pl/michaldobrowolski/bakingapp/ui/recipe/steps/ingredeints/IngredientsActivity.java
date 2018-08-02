package pl.michaldobrowolski.bakingapp.ui.recipe.steps.ingredeints;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

public class IngredientsActivity extends AppCompatActivity {

    private boolean fragmentAdded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_ingredients);

        if(savedInstanceState != null){
            fragmentAdded = savedInstanceState.getBoolean("fragment_added");
        }
        addIngredientListFragment(fragmentAdded);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private void addIngredientListFragment(boolean fragmentExist) {
        Bundle ingredientsBundle = getIntent().getExtras();
        IngredientListFragment ingredientListFragment = new IngredientListFragment();
        ingredientListFragment.setArguments(ingredientsBundle);

        FragmentManager fm = getSupportFragmentManager();
        if(!fragmentExist){
            fm.beginTransaction()
                    .add(R.id.ingredients_list_container, ingredientListFragment)
                    .commit();
            fragmentAdded = true;
        } else {
            fm.beginTransaction()
                    .replace(R.id.ingredients_list_container, ingredientListFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("fragment_added", fragmentAdded);
    }
}
