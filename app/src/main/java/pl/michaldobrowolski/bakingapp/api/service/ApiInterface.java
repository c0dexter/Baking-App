package pl.michaldobrowolski.bakingapp.api.service;

import java.util.List;

import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

// Interface of ApiClient
public interface ApiInterface {
    // Here are populated endpoints only with proper methods (GET/POST/PUT)
    // Call to the proper endpoint by GET method (because of getting info only)
    @GET("android-baking-app-json/")
    Call<List<Recipe>> fetchRecipes();
}
