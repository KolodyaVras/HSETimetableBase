package org.hse.timetablehsebase;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private final static String PREFERENCE_FILE = "org.hse.android.file";
    private final SharedPreferences sharedPref;
    public final int REQUEST_PERMISSION_CODE = 0;
    public PreferenceManager (Context context){
        sharedPref = context.getSharedPreferences(PREFERENCE_FILE,Context.MODE_PRIVATE);
    }
    private void saveValue(String key, String value){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }
    private String getValue(String key, String defaultValue){
        return sharedPref.getString(key,defaultValue);
    }

}
