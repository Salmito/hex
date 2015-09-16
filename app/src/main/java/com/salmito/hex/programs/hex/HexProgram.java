package com.salmito.hex.programs.hex;

import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.salmito.hex.engine.Program;
import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.Box;
import com.salmito.hex.engine.things.FancyCamera;
import com.salmito.hex.engine.things.Point3f;
import com.salmito.hex.programs.hex.entities.HexColor;
import com.salmito.hex.programs.hex.entities.HexMap;
import com.salmito.hex.programs.hex.entities.Hexagon;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tiago on 8/13/2015.
 */
public class HexProgram extends Program {

    private static final String TAG = HexProgram.class.getName();

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
    final static int[][] evenNeighbors = {{1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
    final static int[][] oddNeighbors = {{1, 0}, {1, -1}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}};

    static private final float[] mViewMatrix = new float[16];
    static private final float[] mModelMatrix = new float[16];
    static private final float[] mMVPMatrix = new float[16];
    static private final float[] mProjectionMatrix = new float[16];

    static private final int[] mView = new int[4];

    final private FancyCamera camera;

    private final static ArrayList<Thing> things = new ArrayList<Thing>();
    private static HexProgram currentProgram;
    private static final HexMap map = new HexMap(HexProgram.getProgram(), 100);
    ;
    private static final Box box = new Box(new Point3f(0f, 0f, 0f), 1f, 1f, 1f);
    ;
    public float previousX, previousY;
    private int i = 0;


    public HexProgram() {
        super(mainVertexShader, mainFragmentShader, new String[]{"u_MVPMatrix"}, new String[]{"a_Position", "a_Color"});
        System.out.println("Creating Hex Program");
        this.camera = new FancyCamera(mViewMatrix, mView, mProjectionMatrix) {{
            this.lookAt();
        }};
        things.add(0, camera);
    }

    public static HexProgram getProgram() {
        if (currentProgram == null) {
            currentProgram = new HexProgram();
        }
        return currentProgram;
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

    public float[] getmViewMatrix() {
        return mViewMatrix;
    }

    public float[] getmModelMatrix() {
        return mModelMatrix;
    }

    public float[] getmMVPMatrix() {
        return mMVPMatrix;
    }

    public float[] getmProjectionMatrix() {
        return mProjectionMatrix;
    }

    public int[] getmView() {
        return mView;
    }

    @Override
    public void surfaceChanged(int width, int height) {
        super.surfaceChanged(width, height);

        this.use();

        mView[0] = 0;
        mView[1] = 0;
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

    @Override
    public void draw(long time) {
        camera.draw(time);
        map.draw(time);
        box.draw(time);
    }

    private void touch(final HexMap.Coordinates t) {
        touch(t, 1);
    }

    public HexMap getMap() {
        return map;
    }

    @Override
    public void onDoubleTap(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        float[] coordinates;
        HexMap.Coordinates t;

        coordinates = camera.unproject(x, y);
        t = HexMap.Coordinates.geo(coordinates[0], coordinates[1]);
        Log.d(TAG, "Double tap detected in " + (t.getQ()) + " " + (t.getR()) + " " + getMap().hasHexagon(t));

        touch(t);
    }

    @Override
    public void onSingleTapConfirmed(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        float[] coordinates;
        HexMap.Coordinates t;

        Log.d(TAG, "Single tap detected in " + e.getX() + ", " + e.getY());
        coordinates = camera.unproject(x, y);
        t = HexMap.Coordinates.geo(coordinates[0], coordinates[1]);
        if (t.getR() >= 0 && t.getQ() >= 0) {
            Hexagon h = getMap().getHexagon(t);
            h.flip((h.getColor() + 1) % HexColor.mColorNumber, 0);
        }


    }

    @Override
    public void touchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        float[] coordinates;
        HexMap.Coordinates t;

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                //System.out.println("ACTION MOVE");
                float dx = x - previousX;
                float dy = y - previousY;
                if (e.getPointerCount() == 1) {
                    camera.move(dx / 100.0f, dy / 100.0f);
                    //renderer.onTouch(x, y);
                    //System.out.println("Moved "+dx+" "+dy);
                    //MainRenderer.angle+=dx;
                    //MainRenderer.mainCamera.zoom(dy/1000.0f);

                } else {
                    if (e.getPointerCount() == 2) {
                        camera.zoom(dy / 100.0f);
                        System.out.println("Screen limits: " + getScreenBottom() + " -> " + getScreenTop());
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                coordinates = camera.unproject((int) x, (int) y);
                t = HexMap.Coordinates.geo(coordinates[0], coordinates[1]);
                System.out.println("ACTION DOWN on: " + (t.getQ()) + " " + (t.getR()) + " " + getMap().hasHexagon(t));

                //  coordinates = getCamera().unproject(x, y);
                //  t = HexMap.Coordinates.geo(coordinates[0], coordinates[1]);
                //  touch(t);

                box.move(new Point3f(coordinates));
                break;
        }
        previousX = x;
        previousY = y;
    }

    public Point3f getScreenBottom() {
        return new Point3f(camera.unproject(0, height));
    }

    @Override
    public void onLongPress(MotionEvent ev) {
        super.onLongPress(ev);
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        float[] coordinates;
        HexMap.Coordinates t;

        Log.d(TAG, "Long press detected in " + ev.getX() + ", " + ev.getY());
        coordinates = camera.unproject(x, y);
        t = HexMap.Coordinates.geo(coordinates[0], coordinates[1]);
        if (t.getR() >= 0 && t.getQ() >= 0) {
            Hexagon h = getMap().getHexagon(t);

            Point3f p1 = new Point3f(h.getXCenter(), h.getYCenter(), 0f);
            Point3f p2 = new Point3f(h.getXCenter(), h.getYCenter(), 10f);

            camera.moveTo(p1, p2, 10f);
        }


    }

    public Point3f getScreenTop() {
        return new Point3f(camera.unproject(width, 0));
    }


    private void touch(final HexMap.Coordinates t, final int ttl) {
        if (t.getR() >= 0 && t.getQ() >= 0) {

            for (final int[] n : t.getR() % 2 == 0 ? evenNeighbors : oddNeighbors) {
                System.out.println("Getting hex: " + (t.getQ() + n[0]) + " " + (t.getR() + n[1]) + " " + getMap().hasHexagon(t.getQ() + n[0], t.getR() + n[1]));
                getMap().getHexagon(t.getQ() + n[0], t.getR() + n[1]);
                if (getMap().hasHexagon(t.getQ() + n[0], t.getR() + n[1])) {
                    Hexagon h = getMap().getHexagon(t.getQ() + n[0], t.getR() + n[1]);
                    h.flip((h.getColor() + 1) % HexColor.mColorNumber, i);
                    Timer t1 = new Timer("t");
                    if (ttl > 0) {
                        t1.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //System.out.println("timer");
                                touch(new HexMap.Coordinates(t.getQ() + n[0], t.getR() + n[1]), ttl - 1);
                            }
                        }, 500);
                    }
                }
            }

            Hexagon hexagon = getMap().getHexagon(t);
            hexagon.flip((hexagon.getColor() + 1) % (HexColor.mColorNumber - 1), i);
        }
    }

}
