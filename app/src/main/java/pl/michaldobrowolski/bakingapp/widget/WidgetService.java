package pl.michaldobrowolski.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import pl.michaldobrowolski.bakingapp.api.model.pojo.Ingredient;
import pl.michaldobrowolski.bakingapp.api.model.pojo.Recipe;

public class WidgetService extends IntentService {

    private static final String ACTION_OPEN_RECIPE = "pl.michaldobrowolski.bakingapp.widget.widget_service";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetService(String name) {
        super(name);
    }

    public WidgetService() {
        super("WidgetService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "channel_1";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BakingApp Widget Service",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) Objects.requireNonNull(getSystemService(NOTIFICATION_SERVICE))).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RECIPE.equals(action)) {
                handleActionOpenRecipe();
            }
        }
    }

    private void handleActionOpenRecipe() {

        //Get data from shared pref
        SharedPreferences sharedpreferences =
                getSharedPreferences("shared_pref_key",MODE_PRIVATE);
        String jsonRecipe = sharedpreferences.getString("json_result_extra_key", "");

        StringBuilder stringBuilder = new StringBuilder();
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
        List<Ingredient> ingredientList = recipe.getmIngredients();
        for(Ingredient ingredient : ingredientList){
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure = ingredient.getMeasure();
            String ingredientName = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + ingredientName;
            stringBuilder.append( line + "\n");
        }
        String ingredientsString = stringBuilder.toString();

        //Here we create AppWidgetManager to update AppWidget state
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        //Pass recipe info into the widget provider
        RecipeWidgetProvider.updateWidgetRecipe(this, ingredientsString, appWidgetManager, appWidgetIds);
    }


    // Trigger the service to perform the action
    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

    // For Android O and above
    public static void startActionOpenRecipeAndroidO(Context context){
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        ContextCompat.startForegroundService(context,intent);
    }
}
