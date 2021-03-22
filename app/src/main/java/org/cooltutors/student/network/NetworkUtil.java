package org.cooltutors.student.network;

// Darren Stults

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtil {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    public static final String WEBSITE_BASE_URL = "https://cooltutors.org";
    public static final String WEBSITE_BASE_URL2 = "http://darrens.lwtech-csd297.com"; // backup
    public static final String WEBSITE_URL_LOGIN = WEBSITE_BASE_URL + "/log_in?json";
    public static final String WEBSITE_URL_LOGOUT = WEBSITE_BASE_URL + "/log_out?json";
    public static final String WEBSITE_URL_SERVICES = WEBSITE_BASE_URL + "/services?json";
    public static final String WEBSITE_URL_OPENINGS = WEBSITE_BASE_URL + "/openings?json";
    public static final String WEBSITE_URL_APPOINTMENTS = WEBSITE_BASE_URL + "/appointments?json";
    public static final String WEBSITE_URL_PROFILE = WEBSITE_BASE_URL + "/profile?json";

    private static final String COOKIE_HEADER = "Set-Cookie";
    private static final CookieManager cookieManager = new java.net.CookieManager();

    public static void clearCookies() {
        cookieManager.getCookieStore().removeAll();
    }

    public static String runQuery(Uri uri) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {

            // Setup
            URL requestURL = new URL(uri.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            boolean errored = false;

            // Assign session cookie
            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                urlConnection.setRequestProperty("Cookie", TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
            }

            // Establish
            urlConnection.connect();

            // Get Connection Cookie
            Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
            List<String> cookieHeaders = headerFields.get(COOKIE_HEADER);
            if (cookieHeaders != null) {
                for (String cookie : cookieHeaders) {
                    cookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
                }
            }

            // Establish stream, check for error
            InputStream inputStream;
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                errored = true;
                inputStream = urlConnection.getErrorStream();
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            // String
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            if (sb.length() == 0) return "";

            // Done
            response = sb.toString();
            if (errored) {
                Log.d(LOG_TAG, "Error while attempting to access page: " + uri.toString() + "\n" + response);
                if (response.contains("<title>") && response.contains("</title>")) {
                    Pattern pattern = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(response);
                    while (matcher.find()) {
                        response = matcher.group(1);
                    }
                } else {
                    response = "Error connecting to server. Maybe try logging in again?";
                }
            }

        } catch (IOException e) {
            // Network Error
            Log.d(LOG_TAG, "IOException");
            e.printStackTrace();
        } finally {

            // Close loose ends
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        Log.d(LOG_TAG, response);
        return response;
    }

    public static boolean networkIsConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            // android studio says deprecated and use this instead
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                    );
        } else {
            // lab 7.2 way
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

}
