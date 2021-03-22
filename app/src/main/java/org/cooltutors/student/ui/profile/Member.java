package org.cooltutors.student.ui.profile;

import android.util.Log;

import org.cooltutors.student.R;
import org.cooltutors.student.network.NetworkUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class Member {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();
    public final String id;
    public final String loginName;
    public final String displayName;
    public final String birthdate;
    public final String gender;
    public final String instructorNotes;
    public final String phone1;
    public final String phone2;
    public final String email;
    public final boolean isStudent;
    public final boolean isInstructor;
    public final boolean isAdmin;

    public Member(String jsonData) {
        JSONObject jo = null;
        try {
            jo = new JSONObject(jsonData);
        } catch (JSONException e1) {
            try {
                jo = new JSONObject("{}"); // empty object on fail -- uses same code below, fills with "error"
            } catch (JSONException e2) {
                e1.printStackTrace();
                e2.printStackTrace();
            }
        }

        String id = "???";
        String loginName = "(error)";
        String displayName = "(error)";
        String birthdate = "(error)";
        String gender = "(error)";
        String instructorNotes = "(error)";
        String phone1 = "(error)";
        String phone2 = "(error)";
        String email = "(error)";
        boolean isStudent = false;
        boolean isInstructor = false;
        boolean isAdmin = false;
        try {
            id = jo.has("id") ? jo.getString("id") : "???";
            loginName = jo.has("loginName") ? jo.getString("loginName") : "(missing)";
            displayName = jo.has("displayName") ? jo.getString("displayName") : "(missing)";
            birthdate = jo.has("birthdate") ? jo.getString("birthdate") : "(missing)";
            gender = jo.has("gender") ? jo.getString("gender") : "(missing)";
            instructorNotes = jo.has("instructorNotes") ? jo.getString("instructorNotes") : "(missing)";
            phone1 = jo.has("phone1") ? jo.getString("phone1") : "(missing)";
            phone2 = jo.has("phone2") ? jo.getString("phone2") : "(missing)";
            email = jo.has("email") ? jo.getString("email") : "(missing)";
            isStudent = jo.has("isStudent") && jo.getBoolean("isStudent");
            isInstructor = jo.has("isInstructor") && jo.getBoolean("isInstructor");
            isAdmin = jo.has("isAdmin") && jo.getBoolean("isAdmin");
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
        this.id = id;
        this.loginName = loginName;
        this.displayName = displayName;
        this.birthdate = birthdate;
        this.gender = gender;
        this.instructorNotes = instructorNotes;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
        this.isStudent = isStudent;
        this.isInstructor = isInstructor;
        this.isAdmin = isAdmin;
    }

}
