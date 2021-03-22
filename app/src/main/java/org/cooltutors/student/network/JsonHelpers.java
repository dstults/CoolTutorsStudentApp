package org.cooltutors.student.network;

import android.util.Log;

public class JsonHelpers {

    public static String hasError(String LOG_TAG, String jsonData) {
        String response = "";
        if (jsonData == null) {
            response = "null json data";
        } else if (jsonData.isEmpty()) {
            response = "empty json data";
        } else if (jsonData.toLowerCase().contains("http status") && jsonData.toLowerCase().contains("error")) {
            response = "error connecting to server";
        }
        if (!response.isEmpty()) Log.d(LOG_TAG, response);
        return response;
    }

    public static String isNotLoggedIn(String LOG_TAG, String jsonData) {
        String response = "";
        if (jsonData.toLowerCase().contains("not logged in")) {
            response = "You are no longer logged in, please log in again.";
        }
        if (!response.isEmpty()) Log.d(LOG_TAG, response);
        return response;
    }

}
