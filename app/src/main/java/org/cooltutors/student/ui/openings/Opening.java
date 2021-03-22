package org.cooltutors.student.ui.openings;

import android.graphics.Color;
import android.util.Log;

import org.cooltutors.student.*;
import org.cooltutors.student.network.NetworkUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class Opening {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();
    public final String id;
    public int backgroundColor;
    public int foregroundColor;
    public final String date;
    public final String startTime;
    public final String endTime;
    public final String instructorName;
    public final String instructorId;
    public final String dayOfWeek;

    public Opening(String color, JSONObject jo) {
        switch (color) {
            case "lightblue":
            case "graybg":
                this.backgroundColor = MainActivity.me.getColor(R.color.background_light_blue);
                this.foregroundColor = MainActivity.me.getColor(R.color.faded_blue);
                break;
            case "whitebg":
                this.backgroundColor = Color.WHITE;
                this.foregroundColor = MainActivity.me.getColor(R.color.faded_orange);
                break;
        }

        String id = "(error)";
        String date = "(error)";
        String startTime = "(error)";
        String end = "(error)";
        String instructorId = "(error)";
        String instructorName = "(error)";
        String dayOfWeek = "(error)";
        try {
            id = jo.has("id") ? jo.getString("id") : "???";
            date = jo.has("date") ? jo.getString("date") : "???";
            dayOfWeek = jo.has("date") ? DateHelpers.getDayOfWeek(date) : "???";
            startTime = jo.has("startTime") ? jo.getString("startTime") : "(missing)";
            end = jo.has("endTime") ? jo.getString("endTime") : "(missing)";
            instructorId = jo.has("instructorId") ? jo.getString("instructorId") : "???";
            instructorName = jo.has("instructorName") ? jo.getString("instructorName") : "(missing)";
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = end;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.dayOfWeek = dayOfWeek;
    }

}
