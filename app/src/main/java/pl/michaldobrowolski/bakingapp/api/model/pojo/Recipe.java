package pl.michaldobrowolski.bakingapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    public final static Parcelable.Creator<Recipe> CREATOR = new Creator<Recipe>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return (new Recipe[size]);
        }

    };

    // JSON keys
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_INGREDIENTS = "ingredients";
    private static final String JSON_KEY_STEPS = "steps";
    private static final String JSON_KEY_SERVINGS = "servings";
    private static final String JSON_KEY_IMAGE = "image";

    // Fields
    @SerializedName(JSON_KEY_ID)
    private int mId;
    @SerializedName(JSON_KEY_NAME)
    private String mName;
    @SerializedName(JSON_KEY_INGREDIENTS)
    private List<Ingredient> mIngredients = new ArrayList<Ingredient>();
    @SerializedName(JSON_KEY_STEPS)
    private List<Step> mSteps = new ArrayList<Step>();
    @SerializedName(JSON_KEY_SERVINGS)
    private int mServings;
    @SerializedName(JSON_KEY_IMAGE)
    private String mImage;

    private Recipe(Parcel in) {
        this.mId = ((int) in.readValue((int.class.getClassLoader())));
        this.mName = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.mIngredients, (Ingredient.class.getClassLoader()));
        in.readList(this.mSteps, (Step.class.getClassLoader()));
        this.mServings = ((int) in.readValue((int.class.getClassLoader())));
        this.mImage = ((String) in.readValue((String.class.getClassLoader())));
    }


    // Empty constructor
    public Recipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, int servings, String imagePath) {
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImage = imagePath;
    }

    // Getters
    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public List<Ingredient> getmIngredients() {
        return mIngredients;
    }

    public List<Step> getmSteps() {
        return mSteps;
    }

    public int getmServings() {
        return mServings;
    }

    public String getmImage() {
        return mImage;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mId);
        dest.writeValue(mName);
        dest.writeList(mIngredients);
        dest.writeList(mSteps);
        dest.writeValue(mServings);
        dest.writeValue(mImage);
    }

    public int describeContents() {
        return 0;
    }

}