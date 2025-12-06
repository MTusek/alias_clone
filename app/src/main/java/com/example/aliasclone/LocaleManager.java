package com.example.aliasclone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocaleManager {

    private static final String PREF_NAME = "app_settings";
    private static final String KEY_LANGUAGE = "app_language";

    private static final String DEFAULT_LANGUAGE = "en";


    public static Context setLocale(Context context) {
        String lang = getLanguage(context);
        return updateResources(context, lang);
    }


    public static Context setNewLocale(Context context, String language) {
        persistLanguage(context, language);
        return updateResources(context, language);
    }


    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }

    @SuppressLint("ApplySharedPref")
    private static void persistLanguage(Context context, String language) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, language).commit();
    }


    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}
