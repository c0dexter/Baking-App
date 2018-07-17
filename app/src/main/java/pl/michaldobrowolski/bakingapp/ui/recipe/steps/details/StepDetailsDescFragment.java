package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.michaldobrowolski.bakingapp.R;

public class StepDetailsDescFragment extends Fragment {

    // -------------------- Properties --------------------//
    final static String TAG = StepDetailsDescFragment.class.getSimpleName();

    private Context mContext;
    private String mDescription;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public StepDetailsDescFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_step_detail_full_desc_item, container, false); //fragment_step_detail_vertical

        // Get data from StepDetailActivity
        getDataFromBundle();

        // Mapping views
        TextView fullDescTv = rootView.findViewById(R.id.text_step_full_desc);
        fullDescTv.setText(mDescription);

        return rootView;
    }

    private void getDataFromBundle() {
        Bundle stepDetailBundle = getArguments();
        if (stepDetailBundle != null && stepDetailBundle.containsKey("desc_bundle")) {
            mDescription = stepDetailBundle.getString("desc_bundle");
        } else {
            Log.i(TAG, "Incorrect bundle key!");
        }
    }

}




