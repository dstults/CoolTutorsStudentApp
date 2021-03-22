package org.cooltutors.student.network;

// Darren Stults

import android.util.Log;

import org.cooltutors.student.*;
import org.json.*;

public class LoginReply {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();
    public final String message;
    public final String loginName;

    public LoginReply(String jsonResponse) {
        String message = "(error)";
        String loginName = "(error)";
        try {
            JSONObject replyJo = new JSONObject(jsonResponse);
            message = replyJo.has("message") ? replyJo.getString("message") : "(missing)";
            loginName = replyJo.has("loginName") ? replyJo.getString("loginName") : "(missing)";
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
        this.message = message;
        this.loginName = loginName;
    }

    public boolean loginSuccess() {
        return message.toLowerCase().contains("welcome") || message.toLowerCase().contains("already logged in");
    }

}
