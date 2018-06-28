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
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Step;

public class RecipeStepListAdapter extends RecyclerView.Adapter<RecipeStepListAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    private final RecipeStepListAdapter.StepListAdapterOnClickHandler stepListAdapterOnClickHandler;
    private List<Step> mStepList;
    private Recipe recipe; // TODO: is this useful?

    // Constructor
    public RecipeStepListAdapter(StepListAdapterOnClickHandler stepListAdapterOnClickHandler, List<Step> mStepList) {
        this.stepListAdapterOnClickHandler = stepListAdapterOnClickHandler;
        this.mStepList = mStepList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_step_item, parent, false);
        view.setFocusable(true);
        return new RecipeStepListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView stepDescriptionTv = holder.stepDescription;
        TextView stepNumber = holder.stepNumber;

        Step step = mStepList.get(position);
        stepDescriptionTv.setText(step.getmShortDescription());
        stepNumber.setText(String.format("Step #%s", String.valueOf(position + 1))); //Here I set the step#
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "Item count: " + String.valueOf(mStepList.size()));
        return mStepList.size();
    }

    // OnCLick interface on the step
    public interface StepListAdapterOnClickHandler {
        void onClickStep(int stepPosition);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Mapping elements on a view
        TextView stepDescription;
        TextView stepNumber;

        ViewHolder(View itemView) {
            super(itemView);
            stepDescription = itemView.findViewById(R.id.text_step_short_desc);
            stepNumber = itemView.findViewById(R.id.step_number);
            stepDescription.setOnClickListener(this); // NOTICE: setOnclickListener on the specific item
        }

        @Override
        public void onClick(View v) {
            int stepPosition = getAdapterPosition();
            stepListAdapterOnClickHandler.onClickStep(stepPosition);
        }
    }

    // ----- TEMPORARY REJECTED ------ //
//    public void setSteps(List<Step> steps) {
//        this.mStepList.clear();
//        this.mStepList.addAll(steps);
//        notifyDataSetChanged();
//    }

}
