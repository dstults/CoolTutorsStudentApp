package org.cooltutors.student;

// Darren Stults

import android.content.*;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.View;
import android.widget.*;

import org.cooltutors.student.network.*;
import org.jetbrains.annotations.NotNull;

public class LogInActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private EditText loginNameText;
    private EditText passwordText;
    private ProgressBar loadingSpinner;
    private LogInActivity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        loginNameText = findViewById(R.id.login_name);
        passwordText = findViewById(R.id.login_password);
        loadingSpinner = findViewById(R.id.login_loading_spinner);
        me = this;
        Button loginButton = findViewById(R.id.button_login);
        Button adminDemo = findViewById(R.id.button_login_demo1);
        Button instructorDemo = findViewById(R.id.button_login_demo2);
        Button studentDemo = findViewById(R.id.button_login_demo3);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String loginName = loginNameText.getText().toString();
                String loginPassword = passwordText.getText().toString();
                if (loginName.isEmpty()) {
                    Toast.makeText(me, R.string.provide_login_name, Toast.LENGTH_SHORT).show();
                    return;
                } else if (loginPassword.isEmpty()) {
                    Toast.makeText(me, R.string.provide_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetworkUtil.networkIsConnected(me)) {
                    Toast.makeText(me, R.string.no_network, Toast.LENGTH_SHORT).show();
                } else {
                    tryLogin(loginName, loginPassword);
                }
            }
        });

        adminDemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tryLogin("Darren", "poop");
            }
        });

        instructorDemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tryLogin("Fred", "asdf");
            }
        });

        studentDemo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                tryLogin("Ben", "asdf");
            }
        });

    }

    private void tryLogin(String loginName, String password) {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("baseUrl", NetworkUtil.WEBSITE_URL_LOGIN);
        queryBundle.putString("loginName", loginName);
        queryBundle.putString("password", password);

        LoaderManager.getInstance(this).restartLoader(0, queryBundle, this);
    }

    //region ASYNC
    @NotNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String baseUrl = "";
        String loginName = "";
        String password = "";
        if (args != null) {
            baseUrl = args.getString("baseUrl");
            loginName = args.getString("loginName");
            password = args.getString("password");
        }
        Uri builtURI = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("loginName", loginName)
                .appendQueryParameter("password", password)
                .build();

        loadingSpinner.setVisibility(View.VISIBLE);

        return new AsyncConnectionLoader(this, builtURI);
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<String> loader, String data) {
        LoginReply lr = new LoginReply(data);
        Toast.makeText(this, lr.message, Toast.LENGTH_SHORT).show();
        loadingSpinner.setVisibility(View.INVISIBLE);
        if (lr.loginSuccess()) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<String> loader) {}
    //endregion

}