package org.cooltutors.student.network;

// Darren Stults

import android.util.Log;

import org.cooltutors.student.*;
import org.cooltutors.student.ui.appointments.*;
import org.json.*;

import java.util.*;

public class AppointmentsReply {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private final List<Appointment> pastAppointments = new ArrayList<>();
    private final List<Appointment> futureAppointments = new ArrayList<>();

    public AppointmentsReply(String jsonData) {
        if (jsonData == null) throw new IllegalArgumentException(LOG_TAG + " null json data");
        if (jsonData.isEmpty()) throw new IllegalArgumentException(LOG_TAG + " empty json data");

        // Format: Array of 2 Arrays of N Appointment Objects

        pastAppointments.clear();
        futureAppointments.clear();

        try {
            JSONArray outerArray = new JSONArray(jsonData);
            for (int i = 0; i < 2; i++) { // Max: 2 -- future and past
                JSONArray appointmentList = outerArray.getJSONArray(i);
                for (int j = 0; j < appointmentList.length(); j++) {
                    JSONObject jsonObject = appointmentList.getJSONObject(j);
                    if (i == 0) futureAppointments.add(new Appointment(false, jsonObject));
                    else pastAppointments.add(new Appointment(true, jsonObject));
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }

        // The server doesn't always give us data in the correct order, so we fix that.
        Collections.sort(pastAppointments, (o1, o2) -> -DateHelpers.compareDateStrings(o1.date, o2.date));
        Collections.sort(futureAppointments, (o1, o2) -> DateHelpers.compareDateStrings(o1.date, o2.date));
    }

    public List<Appointment> getPastAppointments() {
        return this.pastAppointments;
    }
    public List<Appointment> getFutureAppointments() {
        return this.futureAppointments;
    }

}
