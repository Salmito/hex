package com.salmito.hex.programs.camera;

import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.geometry.Line3f;
import com.salmito.hex.engine.things.geometry.Point2f;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.engine.things.geometry.Vector3f;
import com.salmito.hex.math.easing.EasingFunction;
import com.salmito.hex.math.easing.Linear;
import com.salmito.hex.programs.camera.CameraProgram;

public class Camera implements Thing {
    private final Point3f eye;
    private final Point3f look;
    private final Vector3f up;
    private int[] mView;
    long remaining_time = 0L;
    long total_time = 0L;
    private Point3f d_eye;
    private Point3f d_look;
    private Point3f s_eye;
    private Point3f s_look;
    private EasingFunction cF;
    private static Linear defaultEasing = new Linear();
    private float[] mProjectionMatrix;
    private final float[] mViewMatrix;

    public Camera() {
        this.mViewMatrix = new float[16];;
        this.mView  = new int[4];
        this.mProjectionMatrix =   new float[16];

        this.eye = new Point3f(0.0f, -5f, 10f);
        this.look = new Point3f(0f, 0f, 0f);
        this.up = new Vector3f(new Point3f(0f, 1f, 0f));
        lookAt();
    }

    public void lookAt() {
        Matrix.setLookAtM(mViewMatrix, 0,
                eye.getX(), eye.getY(), eye.getZ(),
                look.getX(), look.getY(), look.getZ(),
                up.getX(), up.getY(), up.getZ());
    }

    public void zoom(float amount) {
        eye.setZ(eye.getZ() + amount);
        lookAt();
    }

    public void moveTo(Point3f d_eye, Point3f d_look, float dt, EasingFunction f) {
        Log.d("FancyCamera", "Going to " + d_eye + " looking " + d_look + " in " + dt + "s with " + f.getClass().getSimpleName());
        remaining_time = (long) (dt * 1000f);
        total_time = (long) (dt * 1000f);
        this.d_eye = d_eye;
        this.d_look = d_look;
        this.s_eye = new Point3f(eye);
        this.s_look = new Point3f(look);
        cF = f;
    }


    /**
     * Unproject x and y screen resolution to points in z=0 plane.
     * <p/>
     * Cast a ray along the near -> far line from the eye and determine the
     * intersection of this line on plane z=0.
     *
     * @param x X resolution in pixels
     * @param y Y resolution in pixels
     * @return the xyz position in world coordinates
     */
    public Point2f unproject(int x, int y) {
        float[] objCoordsnear = new float[4];
        float[] objCoordsfar = new float[4];
        GLU.gluUnProject(x, mView[3] - y, 0.0f, mViewMatrix, 0, mProjectionMatrix, 0, mView, 0, objCoordsnear, 0);
        GLU.gluUnProject(x, mView[3] - y, 1.0f, mViewMatrix, 0, mProjectionMatrix, 0, mView, 0, objCoordsfar, 0);

        //Normalizando

        objCoordsnear[0] = objCoordsnear[0] / objCoordsnear[3];
        objCoordsnear[1] = objCoordsnear[1] / objCoordsnear[3];
        objCoordsnear[2] = objCoordsnear[2] / objCoordsnear[3];

        objCoordsfar[0] = objCoordsfar[0] / objCoordsfar[3];
        objCoordsfar[1] = objCoordsfar[1] / objCoordsfar[3];
        objCoordsfar[2] = objCoordsfar[2] / objCoordsfar[3];

        float u = -objCoordsnear[2] / (objCoordsfar[2] - objCoordsnear[2]); //Plane z=0 equation

        //Intersection of near-far line with plane z=0
        float[] ret = new float[4];
        return new Point2f(
                objCoordsnear[0] + u * (objCoordsfar[0] - objCoordsnear[0]),
                objCoordsnear[1] + u * (objCoordsfar[1] - objCoordsnear[1])

        );
    }

    public void move(float x, float y) {
        eye.setX(eye.getX() - x);
        eye.setY(eye.getY() + y);
        look.setX(look.getX() - x);
        look.setY(look.getY() + y);
    }

    @Override
    public void draw(long dt, CameraProgram program) {

        if (remaining_time - dt <= 0) {
            lookAt();
            return;
        }


        remaining_time -= dt;
        float t = 1f - (((float) remaining_time) / total_time);

        t = cF.easy(t);

        Point3f cEye = Line3f.interpolate(s_eye, d_eye, t);
        Point3f cLook = Line3f.interpolate(s_look, d_look, t);
        this.look.set(cLook);
        this.eye.set(cEye);
        //Log.d("camera", "Moving from " + s_eye + " to " + d_eye + " " + t + " current " + cEye);
        //Log.d("camera", "Looking from " + s_look + " to " + d_look + " " + t + " current " + cLook);
        lookAt();

    }

    public void moveTo(Point3f d_eye, Point3f d_look, float time) {
        moveTo(d_eye, d_look, time, defaultEasing);
    }

    @Override
    public void clean() {

    }

    public Point3f getLook() {
        return look;
    }

    public Point3f getEye() {
        return eye;
    }

    public float[] getViewMatrix() {
        return mViewMatrix;
    }

    public float[] getProjectionMatrix() {
        return mProjectionMatrix;
    }

    public int[] getViewport() {
        return mView;
    }

    public void setViewport(int i, int i1, int width, int height) {

        mView[0] = i;
        mView[1] = i1;
        mView[2] = width;
        mView[3] = height;

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }
}
