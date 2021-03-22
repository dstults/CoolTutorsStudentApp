package org.cooltutors.student.network;

// Darren Stults

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class AsyncConnectionLoader extends AsyncTaskLoader<String> {
    private Uri queryUri;

    public AsyncConnectionLoader(Context context, Uri queryUri) {
        super(context);
        this.queryUri = queryUri;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtil.runQuery(queryUri);
    }
}