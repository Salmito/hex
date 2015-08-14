package com.salmito.hex.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainScreen extends Activity {

    private MainView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGLSurfaceView = new MainView(this);

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2) {
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setRenderer(new MainRenderer());
        } else {
            return;
        }

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        mGLSurfaceView.setVisibility(View.GONE);
        super.onPause();
       // mGLSurfaceView.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mGLSurfaceView.getVisibility() == View.GONE) {
            mGLSurfaceView.setVisibility(View.VISIBLE);
        }
    }

}
