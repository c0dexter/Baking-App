package pl.michaldobrowolski.bakingapp.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import pl.michaldobrowolski.bakingapp.R;

public class UtilityHelper {

    public String removeRedundantCharactersFromText(String regex, String text){
        return text.replaceAll(regex, "");
    }

    public void hideNavButtons(ViewGroup viewGroup){
        ImageButton nextBtn = viewGroup.findViewById(R.id.button_next_step);
        ImageButton backBtn = viewGroup.findViewById(R.id.button_previous_step);
        TextView stepCounterTv = viewGroup.findViewById(R.id.text_step_counter);

        nextBtn.setVisibility(View.GONE);
        backBtn.setVisibility(View.GONE);
        stepCounterTv.setVisibility(View.GONE);

    }
}
