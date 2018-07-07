package pl.michaldobrowolski.bakingapp.ui.recipe.steps.details;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import pl.michaldobrowolski.bakingapp.R;

public class StepDetailsFullDescFragment extends Fragment {

    // -------------------- Properties --------------------//
    final static String TAG = StepDetailsFullDescFragment.class.getSimpleName();

    private Context mContext;
    private Bundle stepDetailBundle;
    private int mStepId;
    private String mDescription;
    private String videoUrl;
    private String thumbnailURL;
    private String mRecipeName;
    // ------------------ End Of Properties ------------------ //

    // Fragment must have: an empty constructor
    public StepDetailsFullDescFragment() {
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
        final View rootView = inflater.inflate(R.layout.fragment_step_detail_full_desc_item, container, false); //fragment_step_detail


        // Set a title on NavBar
        ((StepDetailsActivity) Objects.requireNonNull(getActivity()))
                .setActionBarTitle(mRecipeName + "'s instructions");

        // Mapping views
        TextView fullDescTv = (TextView) rootView.findViewById(R.id.text_step_full_desc);
        fullDescTv.setText(mDescription);

        return rootView;
    }


    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setRecipeName(String recipeName) {
        this.mRecipeName = recipeName;
    }
}




