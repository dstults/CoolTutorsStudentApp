package org.cooltutors.student.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.cooltutors.student.LogInActivity;
import org.cooltutors.student.MainActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.network.AsyncConnectionLoader;
import org.cooltutors.student.network.JsonHelpers;
import org.cooltutors.student.network.LogoutReply;
import org.cooltutors.student.network.NetworkUtil;
import org.cooltutors.student.ui.CustomTheme;
import org.cooltutors.student.ui.appointments.Appointment;
import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private Button logoutButton;
    private ProgressBar loadingSpinner;

    private TextView userLoginName;
    private TextView userDisplayName;
    private TextView userBirthdate;
    private TextView userPhone1;
    private TextView userPhone2;
    private TextView userEmail;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //loadingSpinner = root.findViewById(R.id.main_loading_spinner);

        // Re-set the title color:
        CustomTheme.getCustomTheme().setActionBarForegroundColor();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userLoginName = view.findViewById(R.id.profile_login_name);
        userDisplayName = view.findViewById(R.id.profile_display_name);
        userBirthdate = view.findViewById(R.id.profile_birthdate);
        userPhone1 = view.findViewById(R.id.profile_phone1);
        userPhone2 = view.findViewById(R.id.profile_phone2);
        userEmail = view.findViewById(R.id.profile_email);
        GetProfileInfo(); // don't try to run before variables have been assigned above

        //loadingSpinner = getActivity().findViewById(R.id.main_loading_spinner);
        logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TryLogout();
            }
        });

    }

    private void GetProfileInfo() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("baseUrl", NetworkUtil.WEBSITE_URL_PROFILE);
        LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
    }

    private void TryLogout() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("baseUrl", NetworkUtil.WEBSITE_URL_LOGOUT);
        LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
    }

    //region implementations
    @NotNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String baseUrl = "";
        if (args != null) {
            baseUrl = args.getString("baseUrl");
        }
        Uri builtURI = Uri.parse(baseUrl).buildUpon().build();

        MainActivity.me.showSpinner();

        return new AsyncConnectionLoader(getContext(), builtURI);
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<String> loader, String data) {
        MainActivity.me.hideSpinner();

        // ===========================================================================
        String jsonError = JsonHelpers.hasError(LOG_TAG, data);
        if (!jsonError.isEmpty()) {
            Toast.makeText(getContext(), jsonError, Toast.LENGTH_SHORT).show();
            return;
        }
        // ===========================================================================
        String lostLogin = JsonHelpers.isNotLoggedIn(LOG_TAG, data);
        if (!lostLogin.isEmpty()) {
            Toast.makeText(getContext(), lostLogin, Toast.LENGTH_SHORT).show();
            NetworkUtil.clearCookies();
            Intent i = new Intent(getContext(), LogInActivity.class);
            startActivity(i);
            return;
        }
        // ===========================================================================
        // Add Profile Data to Fragment or Log Out
        // ---------------------------------------------------------------------------
        String lcaseData = data.toLowerCase();
        if (lcaseData.contains("logged out") || lcaseData.contains("have a nice day")) {
            LogoutReply lr = new LogoutReply(data);
            Toast.makeText(getContext(), lr.message, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), LogInActivity.class);
            startActivity(i);
        } else if (lcaseData.contains("name") || lcaseData.contains("id")) {
            Member member = new Member(data);
            userLoginName.setText(member.loginName);
            userDisplayName.setText(member.displayName);
            userBirthdate.setText(member.birthdate);
            userPhone1.setText(member.phone1);
            userPhone2.setText(member.phone2);
            userEmail.setText(member.email);
        }

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<String> loader) {}

    //endregion

}