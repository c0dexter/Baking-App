package pl.michaldobrowolski.bakingapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;

public class RecipeMasterListAdapter extends RecyclerView.Adapter<RecipeMasterListAdapter.ViewHolder> {
    // TODO: Get this shit done
    private final String TAG = this.getClass().getSimpleName();
    private final MasterListAdapterOnClickHandler masterListAdapterOnClickHandler;
    private List<Recipe> mRecipeItems;
    //private Context mContext;

    public RecipeMasterListAdapter(List<Recipe> recipeList, MasterListAdapterOnClickHandler listClickHandler) {
        this.mRecipeItems = recipeList;
        this.masterListAdapterOnClickHandler = listClickHandler;
    }


    @Override
    public RecipeMasterListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeMasterListAdapter.ViewHolder holder, int position) {

        TextView recipeNameCard = holder.tvCardRecipeName;
        ImageView recipePhotoCard = holder.ivCardPhotoRecipe;

        Recipe recipe = mRecipeItems.get(position);

        String recipeName = recipe.getmName();
        String recipePhoto = recipe.getmImage(); //TODO: Make sure how to load data from drawable

        recipeNameCard.setText(recipeName);
        recipePhotoCard.setImageResource(R.drawable.cheesecake); // HARDCODED value

                // TODO: recipePhoto - SET PHOTO HERE DEPENDS ON POSITION (name of recipe in the SwitchCase) and get the stuff from drawable

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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int recipePosition = getAdapterPosition();
            masterListAdapterOnClickHandler.onClickRecipe(recipePosition);
        }
    }
}
