package com.salmito.hex.main;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.salmito.hex.engine.Program;

import static com.salmito.hex.main.MainRenderer.getPrograms;


public class MainView extends GLSurfaceView {

    int i = 0;
    private ViewState state = ViewState.IDLE;
    private GestureDetectorCompat mDetector;

    public MainView(Context context) {
        super(context);
        mDetector = new GestureDetectorCompat(context, new GestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mDetector.onTouchEvent(e);
        for (Program p : getPrograms()) {
            p.touchEvent(e);
        }
        return true;// super.onTouchEvent(e);
    }


    enum ViewState {
        SELECTING,
        MOVING,
        IDLE,
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public void onLongPress(MotionEvent e) {
            for (Program p : getPrograms()) {
                p.onLongPress(e);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            for (Program p : getPrograms()) {
                p.onDoubleTap(e);
            }
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (Program p : getPrograms()) {
                p.onSingleTapConfirmed(e);
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            Log.d("View","Fling");
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            for (Program p : getPrograms()) {
                                p.onSwipeRight();
                            }

                        } else {
                            for (Program p : getPrograms()) {
                                p.onSwipeLeft();
                            }
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        for (Program p : getPrograms()) {
                            p.onSwipeBottom();
                        }
                    } else {
                        for (Program p : getPrograms()) {
                            p.onSwipeTop();
                        }
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }


    }
}
