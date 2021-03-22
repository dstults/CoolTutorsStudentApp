package org.cooltutors.student.network;

import android.util.Log;

import org.cooltutors.student.R;
import org.cooltutors.student.ui.services.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServicesReply {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private final List<Service> allServices = new ArrayList<>();

    public ServicesReply(String jsonData) {
        if (jsonData == null) throw new IllegalArgumentException(LOG_TAG + " null json data");
        if (jsonData.isEmpty()) throw new IllegalArgumentException(LOG_TAG + " empty json data");

        try {
            JSONArray services = new JSONArray(jsonData);
            for (int i = 0; i < services.length(); i++) {
                JSONObject jo = services.getJSONObject(i);
                this.allServices.add(new Service(jo));
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, R.string.bad_json + ": " + e.getStackTrace().toString());
        }
    }

    public List<Service> getServices() {
        return this.allServices;
    }

}
