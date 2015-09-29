package com.salmito.hex.programs.camera;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.salmito.hex.engine.things.geometry.Point2f;
import com.salmito.hex.programs.Program;
import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.geometry.Point3f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by tiago on 8/13/2015.
 */
public abstract class CameraProgram extends Program {

    public final static String mainVertexShader =
            "uniform mat4 u_MVPMatrix;      \n"
                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
                    // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                    + "}                              \n";    // normalized screen coordinates.
    public final static String mainFragmentShader =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";

    private static final String TAG = CameraProgram.class.getName();

    static private final float[] mModelMatrix = new float[16];
    static private final float[] mMVPMatrix = new float[16];

    private final static ArrayList<Thing> things = new ArrayList<Thing>();

    final private Camera camera;

    public CameraProgram() {
        super(mainVertexShader, mainFragmentShader, new String[]{"u_MVPMatrix"}, new String[]{"a_Position", "a_Color"});
        System.out.println("Creating "+this.getClass().getSimpleName());
        this.camera = new Camera();
        things.add(0, camera);
    }

    public int add(Thing t) {
        if (things.add(t))
            return things.size();
        return -1;
    }

    public void remove(Thing t) {
        things.remove(t);
    }

    public void remove(int i) {
        things.remove(i);
    }

    public float[] getModelMatrix() {
        return mModelMatrix;
    }

    @Override
    public void surfaceChanged(int width, int height) {
        super.surfaceChanged(width, height);
        this.use();
        camera.setViewport(0, 0, width, height);
    }

    @Override
    public void draw(long time) {
        for (Thing t : things) {
            t.draw(time, this);
        }
    }

    public Point2f getScreenBottom() {
        return camera.unproject(0, height);
    }


    public Point2f getScreenTop() {
        return camera.unproject(width, 0);
    }

    public void drawBuffer(FloatBuffer mVertices, FloatBuffer mColors, ShortBuffer mIndices, int type) {
        GLES20.glVertexAttribPointer(getAttrib("a_Position"), 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(getAttrib("a_Position"));

        GLES20.glVertexAttribPointer(getAttrib("a_Color"), 4, GLES20.GL_FLOAT, false, 0, mColors);
        GLES20.glEnableVertexAttribArray(getAttrib("a_Color"));

        Matrix.multiplyMM(mMVPMatrix, 0, camera.getViewMatrix(), 0, getModelMatrix(), 0);
        Matrix.multiplyMM(mMVPMatrix, 0, camera.getProjectionMatrix(), 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(getUniform("u_MVPMatrix"), 1, false, mMVPMatrix, 0);
        GLES20.glDrawElements(type, mIndices.capacity(), GLES20.GL_UNSIGNED_SHORT, mIndices);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public Camera getCamera() {
        return camera;
    }


    public float[] getMVPMatrix() {
        return mMVPMatrix;
    }

}
