package pl.michaldobrowolski.bakingapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    public final static Parcelable.Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return (new Ingredient[size]);
        }

    };

    // JSON keys
    private static final String JSON_KEY_QUANTITY = "quantity";
    private static final String JSON_KEY_MEASURE = "measure";
    private static final String JSON_KEY_INGREDIENT = "ingredient";

    @SerializedName(JSON_KEY_QUANTITY)
    private double mQuantity;
    @SerializedName(JSON_KEY_MEASURE)
    private String mMeasure;
    @SerializedName(JSON_KEY_INGREDIENT)
    private String mIngredient;


    // Getters
    public double getQuantity() {
        return mQuantity;
    }

    public String getIngredient() {
        return mIngredient;
    }

    public String getMeasure() {
        return mMeasure;
    }

    private Ingredient(Parcel in) {
        this.mQuantity = (Double) in.readValue(int.class.getClassLoader());
        this.mMeasure = ((String) in.readValue((String.class.getClassLoader())));
        this.mIngredient = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Ingredient() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mQuantity);
        dest.writeValue(mMeasure);
        dest.writeValue(mIngredient);
    }

    public int describeContents() {
        return 0;
    }

}