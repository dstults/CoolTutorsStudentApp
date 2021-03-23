package org.cooltutors.student;

// Darren Stults

import android.content.*;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import org.cooltutors.student.network.*;
import org.jetbrains.annotations.NotNull;

public class LogInActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private EditText loginNameText;
    private EditText passwordText;
    private ProgressBar loadingSpinner;
    private LogInActivity me;
    private Button loginButton;
    private Button adminDemo;
    private Button instructorDemo;
    private Button studentDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        SharedPrefs.initialize(this);

        loginNameText = findViewById(R.id.login_name);
        passwordText = findViewById(R.id.login_password);
        loadingSpinner = findViewById(R.id.login_loading_spinner);
        me = this;
        loginButton = findViewById(R.id.button_login);
        adminDemo = findViewById(R.id.button_login_demo1);
        instructorDemo = findViewById(R.id.button_login_demo2);
        studentDemo = findViewById(R.id.button_login_demo3);
        TextView registerLink = findViewById(R.id.login_register_link);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String loginName = loginNameText.getText().toString();
                String loginPassword = passwordText.getText().toString();

                // Hide the keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null ) {
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

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

        registerLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cooltutors.org/register"));
                if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
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

    private void doBusy() {
        loadingSpinner.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        adminDemo.setEnabled(false);
        instructorDemo.setEnabled(false);
        studentDemo.setEnabled(false);
    }

    private void doneBusy() {
        loadingSpinner.setVisibility(View.INVISIBLE);

        // This will only run if user not logged in so this can be left always enabled
        loginButton.setEnabled(true);
        adminDemo.setEnabled(true);
        instructorDemo.setEnabled(true);
        studentDemo.setEnabled(true);
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

        doBusy();

        return new AsyncConnectionLoader(this, builtURI);
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<String> loader, String data) {
        LoginReply lr = new LoginReply(data);
        Toast.makeText(this, lr.message, Toast.LENGTH_SHORT).show();

        if (lr.loginSuccess()) {
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        } else {
            // this will only run if user not logged in (no startActivity)
            doneBusy();
        }
    }

    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<String> loader) {}
    //endregion

}