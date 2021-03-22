package org.cooltutors.student.ui;

// Darren Stults

import android.graphics.drawable.ColorDrawable;
import android.text.Html;

import androidx.appcompat.app.ActionBar;

public class CustomTheme {

    private static CustomTheme main;
    public final ActionBar actionBar;
    public final int backgroundDark;
    public final int backgroundLight;

    public CustomTheme(int bgDark, int bgLight, ActionBar actionbar) {
        CustomTheme.main = this;
        this.backgroundDark = bgDark;
        this.backgroundLight = bgLight;
        this.actionBar = actionbar;
    }

    public static CustomTheme getCustomTheme() {
        return main;
    }

    public void setActionBarForegroundColor() {
        // This is hacky but it's by far the most reliable way I've found
        // https://stackoverflow.com/a/19592911/11533327
        this.actionBar.setTitle(Html.fromHtml("<font color=\"black\">" + this.actionBar.getTitle() + "</font>"));
    }

    public void setActionBarBackgroundColor() {
        // https://www.geeksforgeeks.org/how-to-change-the-color-of-action-bar-in-an-android-app/
        ColorDrawable colorDrawable = new ColorDrawable(this.backgroundDark);
        this.actionBar.setBackgroundDrawable(colorDrawable);
    }

}