package org.cooltutors.student.network;

// Darren Stults

import android.util.Log;

import org.cooltutors.student.R;
import org.cooltutors.student.ui.openings.Opening;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpeningsReply {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private final List<Opening> allOpenings = new ArrayList<>();

    public OpeningsReply(String jsonData) {
        if (jsonData == null) throw new IllegalArgumentException(LOG_TAG + " null json data");
        if (jsonData.isEmpty()) throw new IllegalArgumentException(LOG_TAG + " empty json data");

        // Format: Array of n1=5 x [7 days x [ n2 x openings ] ]

        allOpenings.clear();

        try {
            JSONArray severalWeeks = new JSONArray(jsonData);
            for (int i = 0; i < severalWeeks.length(); i++) { // N>1=5 weeks of N=7 days
                JSONArray weekOfDays = severalWeeks.getJSONArray(i);
                for (int j = 0; j < weekOfDays.length(); j++) { // 1 day of N>=0 openings
                    JSONObject day = weekOfDays.getJSONObject(j);
                    String color = day.has("color") ? day.getString("color") : "light_blue";
                    String date = day.has("name") ? day.getString("name") : "???";
                    if (day.has("openings")) {
                        JSONArray dayOpenings = day.getJSONArray("openings");
                        for (int k = 0; k < dayOpenings.length(); k++) {
                            JSONObject jsonObject = dayOpenings.getJSONObject(k);
                            allOpenings.add(new Opening(color, jsonObject));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
    }

    public List<Opening> getOpenings() {
        return this.allOpenings;
    }

}
