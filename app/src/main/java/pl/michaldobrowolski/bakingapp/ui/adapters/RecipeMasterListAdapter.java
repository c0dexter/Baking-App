package pl.michaldobrowolski.bakingapp.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.widget.WidgetService;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;

import static android.content.Context.MODE_PRIVATE;

public class RecipeMasterListAdapter extends RecyclerView.Adapter<RecipeMasterListAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final MasterListAdapterOnClickHandler masterListAdapterOnClickHandler;
    private List<Recipe> mRecipeItems;
    private Recipe mRecipe;
    private String mJsonResult;
    private Context mContext;

    // Constructor
    public RecipeMasterListAdapter(Context context, List<Recipe> recipeList, String jsonResult, MasterListAdapterOnClickHandler listClickHandler) {
        this.mContext = context;
        this.mRecipeItems = recipeList;
        this.masterListAdapterOnClickHandler = listClickHandler;
        this.mJsonResult = jsonResult;
    }

    @NonNull
    @Override
    public RecipeMasterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeMasterListAdapter.ViewHolder holder, int position) {
        TextView mainRecipeNameTv = holder.tvCardRecipeName;
        ImageView mainRecipePhotoIv = holder.ivCardPhotoRecipe;

        mRecipe = mRecipeItems.get(position);
        mainRecipeNameTv.setText(mRecipe.getmName());
        setProperCakePhoto(mRecipe, mainRecipePhotoIv);
    }

    private void setProperCakePhoto(Recipe recipe, ImageView imageView) {
        this.mRecipe = recipe;
        ImageView recipePhotoCardImageView = imageView;

        String recipeCakeName = recipe.getmName();

        switch (recipeCakeName) {
            case "Nutella Pie":
                imageView.setImageResource(R.drawable.nutella);
                break;
            case "Brownies":
                imageView.setImageResource(R.drawable.brownie);
                break;
            case "Yellow Cake":
                imageView.setImageResource(R.drawable.yellowcake);
                break;
            case "Cheesecake":
                imageView.setImageResource(R.drawable.cheesecake);
                break;
            default:
                imageView.setImageResource(R.drawable.defaultimage);
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "Item count: " + String.valueOf(mRecipeItems.size()));
        return mRecipeItems.size();
    }

    public interface MasterListAdapterOnClickHandler {
        void onClickRecipe(int recipeCardPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivCardPhotoRecipe;
        TextView tvCardRecipeName;

        ViewHolder(View itemView) {
            super(itemView);
            tvCardRecipeName = itemView.findViewById(R.id.text_card_cake_name);
            ivCardPhotoRecipe = itemView.findViewById(R.id.image_card_cake);
            ivCardPhotoRecipe.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int recipePosition = getAdapterPosition();
            masterListAdapterOnClickHandler.onClickRecipe(recipePosition);

            SharedPreferences.Editor sharedPrefEditor = mContext.getSharedPreferences("shared_pref_key", MODE_PRIVATE).edit();
            sharedPrefEditor.putString("json_result_extra_key", mJsonResult);
            sharedPrefEditor.apply();

            if(Build.VERSION.SDK_INT > 25){
                //Start the widget service to update the widget
                WidgetService.startActionOpenRecipeAndroidO(mContext);
            }
            else{
                //Start the widget service to update the widget
                WidgetService.startActionOpenRecipe(mContext);
            }

            // TODO: mJsonResult -> I have to pass this value as EXTRA to the RecipeDetailsActivity
        }
    }
}
