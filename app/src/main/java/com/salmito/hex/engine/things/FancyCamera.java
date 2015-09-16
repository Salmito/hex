package com.salmito.hex.engine.things;

import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.salmito.hex.engine.Thing;

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


    public void moveTo(Point3f d_eye, Point3f d_look, float time) {
        remaining_time= (long) (time*1000f);
        total_time= (long) (time*1000f);
        this.d_eye=d_eye;
        this.d_look=d_look;
        this.s_eye=new Point3f(eye);
        this.s_look=new Point3f(look);
    }

    @Override
    public void draw(long dt) {

        if(remaining_time-dt<=0) {
            lookAt();
            return;
        }

        remaining_time -= dt;
        float t=1f-(((float)remaining_time)/total_time);
        if(t<0f) t=0f;
        else if(t>1f) t=1f;
        System.out.println(total_time+" "+t);
        Point3f cEye=Line3f.interpolate(s_eye,d_eye,t);
        Point3f cLook=Line3f.interpolate(s_eye,d_eye,t);
        this.look.set(cLook);
        this.eye.set(cEye);
        Log.d("camera", "Moving from " + s_eye + " to " + d_eye + " " + t + " current " + cEye);
        lookAt();
    }

    @Override
    public void clean() {

    }
}
