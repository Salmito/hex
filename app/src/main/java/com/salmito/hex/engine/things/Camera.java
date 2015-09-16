package com.salmito.hex.engine.things;

import android.opengl.GLU;
import android.opengl.Matrix;

import com.salmito.hex.engine.Thing;

public class Camera implements Thing {
    protected final Point3f eye;
    protected final Point3f look;
    protected final Vector3f up;
    protected float[] mViewMatrix;
    protected int[] mView;
    protected float[] mProjectionMatrix;

    public Camera(float[] mViewMatrix, int[] mView, float[] mProjectionMatrix) {
        this.mViewMatrix = mViewMatrix;
        this.mView = mView;
        this.mProjectionMatrix = mProjectionMatrix;

        this.eye = new Point3f(0.0f, -5f, 10f);
        this.look = new Point3f(0f, 0f, 0f);
        this.up = new Vector3f(new Point3f(0f, 1f, 0f));
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
    public float[] unproject(int x, int y) {
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
        ret[0] = objCoordsnear[0] + u * (objCoordsfar[0] - objCoordsnear[0]);
        ret[1] = objCoordsnear[1] + u * (objCoordsfar[1] - objCoordsnear[1]);
        ret[2] = 0.0f;
        ret[3] = 1.0f;

        return ret;
    }

    public void move(float x, float y) {
        eye.setX(eye.getX() - x);
        eye.setY(eye.getY() + y);
        look.setX(look.getX()-x);
        look.setY(look.getY() + y);
    }

    @Override
    public void draw(long time) {
        lookAt();
    }

    @Override
    public void clean() {

    }
}
