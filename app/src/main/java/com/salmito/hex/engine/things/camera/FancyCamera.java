package com.salmito.hex.engine.things.camera;

import android.util.Log;

import com.salmito.hex.engine.things.geometry.Line3f;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.math.easing.EasingFunction;
import com.salmito.hex.math.easing.Linear;

public class FancyCamera extends Camera {
    public FancyCamera(float[] mViewMatrix, int[] mView, float[] mProjectionMatrix) {
        super(mViewMatrix, mView, mProjectionMatrix);
        lookAt();
    }

    long remaining_time=0L;
    long total_time=0L;
    private Point3f d_eye;
    private Point3f d_look;
    private Point3f s_eye;
    private Point3f s_look;
    private EasingFunction cF;
    private static Linear defaultEasing=new Linear();

    public void moveTo(Point3f d_eye, Point3f d_look, float dt, EasingFunction f) {
        Log.d("FancyCamera", "Going to "+d_eye+" looking "+d_look+" in "+dt+"s with "+f.getClass().getSimpleName());
        remaining_time= (long) (dt*1000f);
        total_time= (long) (dt*1000f);
        this.d_eye=d_eye;
        this.d_look=d_look;
        this.s_eye=new Point3f(eye);
        this.s_look=new Point3f(look);
        cF=f;
    }

    public void moveTo(Point3f d_eye, Point3f d_look, float time) {
    moveTo(d_eye, d_look, time, defaultEasing);
    }

    @Override
    public void draw(long dt) {

        if(remaining_time-dt<=0) {
            lookAt();
            return;
        }

        remaining_time -= dt;
        float t=1f-(((float)remaining_time)/total_time);

        t=cF.easy(t);

        Point3f cEye= Line3f.interpolate(s_eye, d_eye, t);
        Point3f cLook=Line3f.interpolate(s_look,d_look,t);
        this.look.set(cLook);
        this.eye.set(cEye);
        //Log.d("camera", "Moving from " + s_eye + " to " + d_eye + " " + t + " current " + cEye);
        //Log.d("camera", "Looking from " + s_look + " to " + d_look + " " + t + " current " + cLook);
        lookAt();
    }

    @Override
    public void clean() {

    }


}
