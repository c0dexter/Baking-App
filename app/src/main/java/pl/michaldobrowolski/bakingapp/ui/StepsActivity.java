package pl.michaldobrowolski.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import pl.michaldobrowolski.bakingapp.R;

public class StepsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Bundle bundle = getIntent().getExtras();

        RecipeStepListFragment recipeStepListFragment = new RecipeStepListFragment();
        recipeStepListFragment.setArguments(bundle);
        addFragment(recipeStepListFragment);

    }

    private void addFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.steps_list_container, fragment)
                .commit();
    }
}
