package com.salmito.hex.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.salmito.hex.engine.Program;

import static com.salmito.hex.main.MainRenderer.getPrograms;


public class MainView extends GLSurfaceView {

    int i = 0;
    private ViewState state = ViewState.IDLE;

    public MainView(Context context) {
        super(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        for (Program p : getPrograms()) {
            p.touchEvent(e);
        }
        return true;
    }

    enum ViewState {
        SELECTING,
        MOVING,
        IDLE,
    }
}
