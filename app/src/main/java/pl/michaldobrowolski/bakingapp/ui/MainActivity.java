package pl.michaldobrowolski.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.ui.adapters.RecipeMasterListAdapter;

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
