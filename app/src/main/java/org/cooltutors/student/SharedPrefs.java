package org.cooltutors.student;

// Darren Stults

// Note on this file:
// Even though all my stuff that actually needs shared preferences is all in one file
// (MainActivity), and this could all have easily gone in there (in a reduced footprint),
// I wanted it to try it this way as a proof-of-concept, as this format will likely isolate the
// stuff a lot better in a multi-page-remembered-and-saved-prefs scenario.

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor preferencesEditor;
    private static final String sharedPrefsFile = "org.cooltutors.student";
    private static final String RETURN_PAGE = "LAST_PAGE";
    private static final String SELECTED_MEMBER = "SELECTED_MEMBER";

    public static void initialize(Context context) {
        preferences = context.getSharedPreferences(sharedPrefsFile, MODE_PRIVATE);
        preferencesEditor = preferences.edit();
    }

    public static SharedPreferences getPreferences() {
        return preferences;
    }

    public static String getReturnPage() {
        return preferences.getString(RETURN_PAGE, "");
    }

    public static String getSelectedMember() {
        return preferences.getString(SELECTED_MEMBER, "");
    }

    public static void setReturnPage(String returnPage) {
        preferencesEditor.putString(RETURN_PAGE, returnPage);
        preferencesEditor.apply();
    }

    public static void setSelectedMember(String memberName) {
        preferencesEditor.putString(SELECTED_MEMBER, memberName);
        preferencesEditor.apply();
    }

}
