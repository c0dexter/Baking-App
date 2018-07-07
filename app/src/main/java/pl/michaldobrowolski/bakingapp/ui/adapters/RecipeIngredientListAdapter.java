package pl.michaldobrowolski.bakingapp.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;

// MUST EXTENDS a View Holder
public class RecipeIngredientListAdapter extends RecyclerView.Adapter<RecipeIngredientListAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private final IngredientListAdapterOnClickHandler ingredientListAdapterOnClickHandler;
    private List<Ingredient> mIngredientList;

    // Constructor
    public RecipeIngredientListAdapter(IngredientListAdapterOnClickHandler ingredientListAdapterOnClickHandler, List<Ingredient> mIngredientList) {
        this.ingredientListAdapterOnClickHandler = ingredientListAdapterOnClickHandler;
        this.mIngredientList = mIngredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ingredient_item, parent, false);
        view.setFocusable(true);
        return new RecipeIngredientListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView ingredientDescriptionTv = holder.ingredientDescription;
        TextView ingredientQuantityTv = holder.ingredientQuantity;
        TextView ingredientUnitOfMeasureTv = holder.ingredientUnitOfMeasure;

        Ingredient ingredient = mIngredientList.get(position);
        ingredientDescriptionTv.setText(ingredient.getIngredient());
        ingredientQuantityTv.setText(String.valueOf(ingredient.getQuantity()));
        ingredientUnitOfMeasureTv.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        int itemCount = mIngredientList.size();
        Log.i(TAG, "Ingredients count: " + String.valueOf(itemCount));
        return itemCount;
    }

    // OnCLick interface on the ingredient, for the future feature "Checklist to buy"
    public interface IngredientListAdapterOnClickHandler {
        void onClickIngredient(int ingredientPosition);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Mapping elements on a view
        TextView ingredientDescription;
        TextView ingredientQuantity;
        TextView ingredientUnitOfMeasure;

        ViewHolder(View itemView) {
            super(itemView);
            ingredientDescription = itemView.findViewById(R.id.text_ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.text_ingredient_qty);
            ingredientUnitOfMeasure = itemView.findViewById(R.id.text_ingredient_measure_unit);
        }

        @Override
        public void onClick(View v) {
            int ingredientPosition = getAdapterPosition();
            ingredientListAdapterOnClickHandler.onClickIngredient(ingredientPosition);
        }
    }
}
