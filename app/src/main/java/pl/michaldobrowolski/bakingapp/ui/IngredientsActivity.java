package pl.michaldobrowolski.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_ingredients);
        addIngredientListFragment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    private void addIngredientListFragment() {
        Bundle ingredientsBundle = getIntent().getExtras();
        IngredientListFragment ingredientListFragment = new IngredientListFragment();
        ingredientListFragment.setArguments(ingredientsBundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.ingredients_list_container, ingredientListFragment)
                .commit();
    }
}
