package pl.michaldobrowolski.bakingapp.view.recipies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.service.ApiClient;
import pl.michaldobrowolski.bakingapp.api.service.ApiInterface;
import pl.michaldobrowolski.bakingapp.ui.recipe.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static pl.michaldobrowolski.bakingapp.ui.recipe.RecipeMasterListFragment.BUNDLE_RECIPE_LIST_KEY;
import static pl.michaldobrowolski.bakingapp.ui.recipe.RecipeMasterListFragment.INTENT_RECIPE_KEY_FOR_ESPRESSO;

public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mIntentRule =
            new IntentsTestRule<>(MainActivity.class);
    private Context mContext;
    private ApiInterface mApiInterface;
    private List<Recipe> mRecipeList;

    @Before
    public void loadData() {
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Recipe>> call = mApiInterface.fetchRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.body() != null) {
                    Log.d("LOG: Response Code: ", response.code() + "");
                    //fetchingData(response);
                    mRecipeList = response.body();
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

    @Test
    public void validateIntentSentToPackage() throws InterruptedException {
        int position = 2;

        // Max 3000 millis, no more!
        Thread.sleep(3000);

        onView(withId(R.id.master_list_fragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_RECIPE_LIST_KEY, mRecipeList.get(position));

        intended(toPackage("pl.michaldobrowolski.bakingapp"));
        intended(hasExtra(INTENT_RECIPE_KEY_FOR_ESPRESSO, new Gson().toJson(mRecipeList.get(position))));
    }


}
