package pl.michaldobrowolski.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import pl.michaldobrowolski.bakingapp.R;
import pl.michaldobrowolski.bakingapp.ui.recipe.steps.ingredeints.IngredientsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, String jsonIngredients, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // Create the intent
        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra("widget_extra","CAME_FROM_WIDGET");

        // Create the pending intent that will wrap our intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        if(jsonIngredients.equals("")){
            jsonIngredients = "No ingredients yet!";
        }

        views.setTextViewText(R.id.appwidget_text, jsonIngredients);

        // OnClick intent for textview
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            WidgetService.startActionOpenRecipe(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateWidgetRecipe(Context context, String jsonIngredients, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, jsonIngredients, appWidgetManager, appWidgetId);
        }
    }

}

