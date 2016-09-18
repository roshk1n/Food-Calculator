package com.example.roshk1n.foodcalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

public class Localization {
    private static final String LANGUAGE_KEY = "Language";
    private static String language;

    public static void onCreate(Context context, String defaultLanguage) {
        language = getPersistedData(context, defaultLanguage);
        setLocale(context);
    }

    public static String getLanguage() {
        return language;
    }

    public static void setLanguage(String language) {
        Localization.language = language;
    }

    public static void setLocale(Context context) {
        persist(context,language);
        updateResources(context,language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(LANGUAGE_KEY, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(LANGUAGE_KEY, language);
        editor.apply();
    }

    private static void updateResources(Context context, String languages) {
        Locale locale = new Locale(languages);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.locale = locale;

        resources.updateConfiguration(config,
                resources.getDisplayMetrics());
    }

}
