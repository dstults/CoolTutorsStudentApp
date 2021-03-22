package org.cooltutors.student.ui.appointments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cooltutors.student.LogInActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.network.AppointmentsReply;
import org.cooltutors.student.network.AsyncConnectionLoader;
import org.cooltutors.student.network.JsonHelpers;
import org.cooltutors.student.network.NetworkUtil;
import org.cooltutors.student.ui.CustomTheme;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppointmentsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, AppointmentRecyclerAdapter.AppointmentItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private List<Appointment> appointmentListPast;
    private List<Appointment> appointmentListFuture;
    private AppointmentRecyclerAdapter appointmentRecyclerAdapterPast;
    private AppointmentRecyclerAdapter appointmentRecyclerAdapterFuture;
    private RecyclerView appointmentRecycler;
    private Button appointmentsToggleButton;
    private TextView appointmentTypeLabel;
    private ProgressBar loadingSpinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Assignments
        View root = inflater.inflate(R.layout.fragment_appointments, container, false);

        // Re-set the title color:
        CustomTheme.getCustomTheme().setActionBarForegroundColor();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Launch initial async
        if (!NetworkUtil.networkIsConnected(getContext())) {
            Toast.makeText(getContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
        } else {
            tryGetAppointments();
        }

        // Views
        //loadingSpinner = view.findViewById(R.id.main_loading_spinner);
        appointmentRecycler = view.findViewById(R.id.appointment_recycler);
        appointmentsToggleButton = view.findViewById(R.id.button_past_appointments);
        appointmentTypeLabel = view.findViewById(R.id.label_appointment_type);

        // Recycler
        //appointmentRecyclerAdapterPast = new AppointmentRecyclerAdapter(appointmentListPast, this);
        //appointmentRecyclerAdapterFuture = new AppointmentRecyclerAdapter(appointmentListFuture, this);
        //appointmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //appointmentRecycler.setAdapter(appointmentRecyclerAdapterFuture);

        // Listeners
        appointmentsToggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                swapAdapter();
            }
        });

    }

    private void swapAdapter() {
        if (appointmentRecycler.getAdapter().equals(appointmentRecyclerAdapterFuture)) {
            appointmentRecycler.setAdapter(appointmentRecyclerAdapterPast);
            appointmentTypeLabel.setText(getText(R.string.label_past_appointments));
            appointmentsToggleButton.setText(getText(R.string.label_upcoming_appointments));
        } else {
            appointmentRecycler.setAdapter(appointmentRecyclerAdapterFuture);
            appointmentTypeLabel.setText(getText(R.string.label_upcoming_appointments));
            appointmentsToggleButton.setText(getText(R.string.label_past_appointments));
        }
    }

    private void tryGetAppointments() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("baseUrl", NetworkUtil.WEBSITE_URL_APPOINTMENTS);
        LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
    }

    //region Async
    @NotNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String baseUrl = "";
        if (args != null) {
            baseUrl = args.getString("baseUrl");
        }
        Uri builtURI = Uri.parse(baseUrl).buildUpon().build();
        //loadingSpinner.setVisibility(View.VISIBLE);
        return new AsyncConnectionLoader(getContext(), builtURI);
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<String> loader, String data) {
        //loadingSpinner.setVisibility(View.INVISIBLE);
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
        Log.d("!!", "C yay?!");
        // ===========================================================================
        // Add Appointment Data to Fragment
        // ---------------------------------------------------------------------------
        AppointmentsReply ar = new AppointmentsReply(data);
        appointmentListPast = ar.getPastAppointments();
        appointmentListFuture = ar.getFutureAppointments();
        appointmentRecyclerAdapterPast = new AppointmentRecyclerAdapter(appointmentListPast, this);
        appointmentRecyclerAdapterFuture = new AppointmentRecyclerAdapter(appointmentListFuture, this);
        appointmentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        appointmentRecycler.setAdapter(appointmentRecyclerAdapterFuture);

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<String> loader) {}
    //endregion

    //region Listeners
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(Appointment appointment, View view) {

    }
    //endregion

}