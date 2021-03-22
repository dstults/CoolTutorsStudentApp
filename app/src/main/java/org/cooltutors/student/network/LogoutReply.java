package org.cooltutors.student.network;

// Darren Stults

import android.util.Log;

import org.cooltutors.student.R;
import org.json.JSONException;
import org.json.JSONObject;

public class LogoutReply {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();
    public final String message;

    public LogoutReply(String jsonResponse) {
        String message = "(error)";
        try {
            JSONObject replyJo = new JSONObject(jsonResponse);
            message = replyJo.has("message") ? replyJo.getString("message") : "(missing)";
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
        this.message = message;
    }

}
