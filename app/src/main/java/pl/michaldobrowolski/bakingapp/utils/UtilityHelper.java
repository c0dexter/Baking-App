package pl.michaldobrowolski.bakingapp.utils;

public class UtilityHelper {

    public String removeRedundantCharactersFromText(String regex, String text) {
        return text.replaceAll(regex, "");
    }
}
