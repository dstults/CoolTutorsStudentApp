package org.cooltutors.student.ui.services;

import android.graphics.Color;
import android.util.Log;

import org.cooltutors.student.DateHelpers;
import org.cooltutors.student.MainActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.network.NetworkUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Service {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    public final String id;
    public final String name;
    public final String description;
    public final String instructors;

    public Service(JSONObject jo) {

        String id = "(error)";
        String name = "(error)";
        String description = "(error)";
        String instructors = "(error)";
        try {
            id = jo.has("id") ? jo.getString("id") : "???";
            name = jo.has("name") ? jo.getString("name") : "???";
            description = jo.has("description") ? jo.getString("description") : "(missing)";
            instructors = jo.has("instructors") ? jo.getString("instructors") : "(missing)";
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructors = instructors;
    }

    public List<String> getInstructorList() {
        List<String> instructors = new ArrayList<>();
        String[] instructorsSplit = this.instructors.split(",");
        for (String s : instructorsSplit) {
            instructors.add(s.trim());
        }
        return instructors;
    }

}
