package org.cooltutors.student.ui.services;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.cooltutors.student.LogInActivity;
import org.cooltutors.student.MainActivity;
import org.cooltutors.student.R;
import org.cooltutors.student.network.AsyncConnectionLoader;
import org.cooltutors.student.network.JsonHelpers;
import org.cooltutors.student.network.NetworkUtil;
import org.cooltutors.student.network.OpeningsReply;
import org.cooltutors.student.ui.CustomTheme;
import org.cooltutors.student.ui.openings.OpeningRecyclerAdapter;
import org.jetbrains.annotations.NotNull;

public class ServicesFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>  {

    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_services, container, false);

        // Re-set the title color:
        CustomTheme.getCustomTheme().setActionBarForegroundColor();

        return root;
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
        // Add Service Data to Fragment
        // ---------------------------------------------------------------------------
        //ServicesReply sr = new ServicesReply(data);
        //services = sr.getServices();
        //serviceRecyclerAdapter = new ServiceRecyclerAdapter(services, this);
        //serviceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //serviceRecycler.setAdapter(serviceRecyclerAdapter);

    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<String> loader) {}
    //endregion

}