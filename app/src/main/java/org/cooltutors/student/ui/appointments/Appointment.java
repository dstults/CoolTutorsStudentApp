package org.cooltutors.student.ui.appointments;

import android.graphics.Color;
import android.util.Log;

import org.cooltutors.student.DateHelpers;
import org.cooltutors.student.MainActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.network.NetworkUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class Appointment implements Comparator<Appointment> {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    public final String id;
    public final String studentId;
    public final String studentName;
    public final String instructorId;
    public final String instructorName;
    public final String date;
    public final String dayOfWeek;
    public final String startTime;
    public final String endTime;
    public final boolean isMyAppointment;
    public final int backgroundColor;
    public final int foregroundColor;

    public Appointment(boolean isPast, JSONObject jo) {

        String id = "(error)";
        String instructorId = "(error)";
        String instructorName = "(error)";
        String studentId = "(error)";
        String studentName = "(error)";
        String date = "(error)";
        String dayOfWeek = "(error)";
        String startTime = "(error)";
        String endTime = "(error)";
        int backgroundColor = isPast ? MainActivity.me.getColor(R.color.background_light_blue) : Color.WHITE;
        int foregroundColor = isPast ? MainActivity.me.getColor(R.color.faded_blue) : MainActivity.me.getColor(R.color.faded_orange);
        boolean isMyAppointment = false;
        try {
            id = jo.has("id") ? jo.getString("id") : "???";
            instructorId = jo.has("instructorId") ? jo.getString("instructorId") : "???";
            instructorName = jo.has("instructorName") ? jo.getString("instructorName") : "(missing)";
            studentId = jo.has("studentId") ? jo.getString("studentId") : "???";
            studentName = jo.has("studentName") ? jo.getString("studentName") : "(missing)";
            date = jo.has("date") ? jo.getString("date") : "???";
            dayOfWeek = jo.has("date") ? DateHelpers.getDayOfWeek(date) : "???";
            startTime = jo.has("startTime") ? jo.getString("startTime") : "(missing)";
            endTime = jo.has("endTime") ? jo.getString("endTime") : "(missing)";
            isMyAppointment = jo.has("isMyAppointment") && jo.getBoolean("isMyAppointment");
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }

        if (isMyAppointment && !isPast) {
            backgroundColor = MainActivity.me.getColor(R.color.background_really_light_yellow);
            foregroundColor = MainActivity.me.getColor(R.color.faded_orange);
        }

        this.id = id;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isMyAppointment = isMyAppointment;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public int compare(Appointment o1, Appointment o2) {
        return DateHelpers.compareDateStrings(o1.date, o2.date);
    }
}
