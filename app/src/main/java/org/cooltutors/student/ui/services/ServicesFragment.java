package org.cooltutors.student.ui.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cooltutors.student.LogInActivity;
import org.cooltutors.student.MainActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.network.AsyncConnectionLoader;
import org.cooltutors.student.network.JsonHelpers;
import org.cooltutors.student.network.NetworkUtil;
import org.cooltutors.student.network.ServicesReply;
import org.cooltutors.student.ui.CustomTheme;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServicesFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, ServiceRecyclerAdapter.ServiceItemClickListener {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    private List<Service> serviceList;
    private ServiceRecyclerAdapter serviceRecyclerAdapter;
    private RecyclerView serviceRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_services, container, false);

        // Re-set the title color:
        CustomTheme.getCustomTheme().setActionBarForegroundColor();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Views
        serviceRecycler = view.findViewById(R.id.service_recycler);

        // Wait until view is loaded to try to buffer data
        getAllServices();
    }

    private void getAllServices() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("baseUrl", NetworkUtil.WEBSITE_URL_SERVICES);
        LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
    }

    //region Implementations
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
        // Add Service Data to Fragment
        // ---------------------------------------------------------------------------
        ServicesReply sr = new ServicesReply(data);
        serviceList = sr.getServices();
        serviceRecyclerAdapter = new ServiceRecyclerAdapter(serviceList, this);
        serviceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        serviceRecycler.setAdapter(serviceRecyclerAdapter);

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<String> loader) {}

    @Override
    public void onItemClick(Service service, View view) {

    }
    //endregion

}