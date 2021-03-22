package org.cooltutors.student.ui.openings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.*;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cooltutors.student.*;
import org.cooltutors.student.R;
import org.cooltutors.student.network.*;
import org.cooltutors.student.ui.CustomTheme;
import org.cooltutors.student.ui.appointments.Appointment;
import org.cooltutors.student.ui.appointments.AppointmentRecyclerAdapter;
import org.cooltutors.student.ui.openings.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpeningsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, OpeningRecyclerAdapter.OpeningItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private List<Opening> openingList;
    private OpeningRecyclerAdapter openingRecyclerAdapter;
    private RecyclerView openingRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_openings, container, false);

        // Re-set the title color:
        CustomTheme.getCustomTheme().setActionBarForegroundColor();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Views
        //loadingSpinner = view.findViewById(R.id.main_loading_spinner);
        openingRecycler = view.findViewById(R.id.opening_recycler);

        // Recycler
        //openingRecyclerAdapter = new OpeningRecyclerAdapter(openingList, this);
        //openingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //openingRecycler.setAdapter(openingRecyclerAdapter);

        getAllOpenings();
    }

    private void getAllOpenings() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("baseUrl", NetworkUtil.WEBSITE_URL_OPENINGS);
        LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
    }

    //region Implementations
    @NotNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
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
        // ===========================================================================
        // Add Opening Data to Fragment
        // ---------------------------------------------------------------------------
        OpeningsReply or = new OpeningsReply(data);
        openingList = or.getOpenings();
        openingRecyclerAdapter = new OpeningRecyclerAdapter(openingList, this);
        openingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        openingRecycler.setAdapter(openingRecyclerAdapter);

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
    public void onItemClick(Opening opening, View view) {

    }
    //endregion

}