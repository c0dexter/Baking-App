package pl.michaldobrowolski.bakingapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeMasterListAdapter;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeStepListAdapter;

public class MainActivity extends AppCompatActivity implements RecipeMasterListAdapter.MasterListAdapterOnClickHandler {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClickRecipe(int recipeCardPosition) {

    }
}
