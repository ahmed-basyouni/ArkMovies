package com.ark.movieapp.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.ark.movieapp.app.MovieAppApplicationClass;

/**
 * a class that is used to get screen dimensions at runtime
 * Created by ahmedb on 11/26/16.
 */

public class DisplayUtils {

    private Display display;

    public DisplayUtils(){
        WindowManager wm = (WindowManager) MovieAppApplicationClass.getInstance().getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
    }

    public int getDisplayWidth(){
        return display.getWidth();
    }

    public int getDisplayHeight(){
        return display.getHeight();
    }
}
