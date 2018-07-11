package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

public class StepDetailsFragment extends Fragment {
    final static String TAG = StepDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_STEP_ID_KEY = "step_id_bundle_key";
    private static final String BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY = "total_steps_bundle_key";
    private static final String BUNDLE_RECIPE_NAME_KEY = "recipe_name_bundle_key";

    // Properties
    private Context mContext;
    private int mStepId;
    private int mTotalStepsAmount;
    private int mCurrentStep;
    private String mRecipeName;
    private TextView mStepCounterTv;
    private ImageButton backBtn;
    private ImageButton nextBtn;
    private Bundle stepDetailBundle; // ?? Check this if I need it


    // Fragment must have: an empty constructor
    public StepDetailsFragment() {
    }


    // Fragment must have: Handling a proper context property because of FRAGMENT
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    // Fragment must have: onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set a root view
        final View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        // Get data from StepDetailActivity
        getDataFromBundle();

        // Set a title on NavBar
        ((StepDetailsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName + "'s instructions");

        // Mapping views
        backBtn = rootView.findViewById(R.id.button_previous_step);
        nextBtn = rootView.findViewById(R.id.button_next_step);
        mStepCounterTv = rootView.findViewById(R.id.text_step_counter);

        setStepsCounter(mStepId, mTotalStepsAmount);
        showOrHideNavigationButtons();

        // Handling actions on BACK / NEXT button
        setNavigationButtonsBehaviour();

        return rootView;
    }


    private void setStepsCounter(int stepId, int totalStepsAmount) {
        mCurrentStep = stepId;
        int currentStep = stepId + 1; // Add +1 to the real values, because of UX

        String counterValue = currentStep + "/" + totalStepsAmount;
        mStepCounterTv.setText(counterValue);
    }

    private void showOrHideNavigationButtons() {
        if (mCurrentStep + 1 >= mTotalStepsAmount) {
            nextBtn.setVisibility(View.INVISIBLE);
        } else {
            nextBtn.setVisibility(View.VISIBLE);
        }

        // First step has index 0
        if (mCurrentStep - 1 < 0) {
            backBtn.setVisibility(View.INVISIBLE);
        } else {
            backBtn.setVisibility(View.VISIBLE);
        }
    }

    private void getDataFromBundle() {
        stepDetailBundle = getArguments();
        mStepId = Objects.requireNonNull(stepDetailBundle).getInt(BUNDLE_STEP_ID_KEY);
        mTotalStepsAmount = stepDetailBundle.getInt(BUNDLE_RECIPE_TOTAL_STEPS_AMOUNT_KEY);
        mRecipeName = stepDetailBundle.getString(BUNDLE_RECIPE_NAME_KEY);
    }

    private void setNavigationButtonsBehaviour() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "BACK button clicked", Toast.LENGTH_SHORT).show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "NEXT button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
