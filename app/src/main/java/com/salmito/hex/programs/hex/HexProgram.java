package com.salmito.hex.programs.hex;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.salmito.hex.engine.Program;
import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.Box;
import com.salmito.hex.engine.things.camera.FancyCamera;
import com.salmito.hex.engine.things.geometry.Line3f;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.math.easing.EasingFunction;
import com.salmito.hex.programs.hex.entities.HexColor;
import com.salmito.hex.programs.hex.entities.HexCoord;
import com.salmito.hex.programs.hex.entities.HexMap;
import com.salmito.hex.programs.hex.entities.Hexagon;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tiago on 8/13/2015.
 */
public class HexProgram extends Program {

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
    private static final String TAG = HexProgram.class.getName();
    static private final float[] mViewMatrix = new float[16];
    static private final float[] mModelMatrix = new float[16];
    static private final float[] mMVPMatrix = new float[16];
    static private final float[] mProjectionMatrix = new float[16];

    static private final int[] mView = new int[4];
    private final static ArrayList<Thing> things = new ArrayList<Thing>();
    private static final HexMap map = new HexMap(HexProgram.getProgram(), 100);
    private static final Box box = new Box(new Point3f(0f, 0f, 0f), 1f, 1f, 1f);
    private static HexProgram currentProgram;

    final private FancyCamera camera;
    private final Line3f line;

    public float previousX, previousY;
    private int i = 0;
    private int curEasing = 0;
    private float cameraHeight;
    private boolean drawLine;

    public HexProgram() {
        super(mainVertexShader, mainFragmentShader, new String[]{"u_MVPMatrix"}, new String[]{"a_Position", "a_Color"});
        System.out.println("Creating Hex Program");
        this.camera = new FancyCamera(mViewMatrix, mView, mProjectionMatrix);
        things.add(0, camera);
        cameraHeight = 10f;
        GLES20.glLineWidth(5f);
        line = new Line3f(this, new Point3f(0f, 0f, 0f), new Point3f(10f, 0f, 0f));
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
        camera.draw(time, this);
        map.draw(time, HexProgram.getProgram());
        box.draw(time, HexProgram.getProgram());
        if (drawLine)
            line.draw(time, HexProgram.getProgram());
    }

    private Hexagon touch(final HexCoord t) {
        return touch(t, 0);
    }

    public HexMap getMap() {
        return map;
    }

    @Override
    public void onDoubleTap(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        Point3f coordinates;
        HexCoord t;

        coordinates = camera.unproject(x, y);
        t = HexCoord.geo(coordinates.getX(), coordinates.getY());
        Log.d(TAG, "Double tap detected in " + (t.getQ()) + " " + (t.getR()) + " " + getMap().hasHexagon(t));
        touch(t, 1);
    }

    @Override
    public void onSingleTapConfirmed(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        HexCoord t;

        Log.d(TAG, "Single tap detected in " + e.getX() + ", " + e.getY());
        Log.d(TAG, "Eye pos: " + camera.getEye() + ", " + camera.getLook());
        Point3f c = camera.unproject(x, y);
        t = HexCoord.geo(c.getX(), c.getY());

        Hexagon h = touch(t);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        HexCoord t;

        Log.d(TAG, "Single tap detected in " + e.getX() + ", " + e.getY());
        Log.d(TAG, "Eye pos: " + camera.getEye() + ", " + camera.getLook());
        Point3f c = camera.unproject(x, y);
        t = HexCoord.geo(c.getX(), c.getY());

        Hexagon h = touch(t);

        Point3f center = h.getCenter();
        Point3f p1 = new Point3f(center.getX(), center.getY() - 5f, cameraHeight);
        Point3f p2 = new Point3f(center.getX(), center.getY(), 0f);

        camera.moveTo(p1, p2, 1f, EasingFunction.easings[curEasing++ % EasingFunction.easings.length]);

        if (coord1 == null) {
            coord1 = t;
        } else {
            for (HexCoord l : coord1.getLine(t)) {
                touch(l);
            }
            coord1 = null;
        }


        c = camera.unproject((int) e.getX(), (int) e.getY());
        t = HexCoord.geo(c.getX(), c.getY());
        h = getMap().getHexagon(t);

        Log.d(TAG, "Hexagon info: Coordinates: " + h.getCoordinates());
        Log.d(TAG, "Hexagon info: Center: " + h.getCenter());


    }

    private HexCoord coord1;
    private Point3f startLine;


    @Override
    public void touchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        Point3f point = camera.unproject((int) x, (int) y);

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                this.drawLine = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;
                if (e.getPointerCount() == 2) {
                    camera.move(dx / 100.0f, dy / 100.0f);
                }
                line.setEnd(point);
                break;
            case MotionEvent.ACTION_DOWN:
                this.drawLine = true;
                line.setStart(point);
                line.setEnd(point);
                break;
        }
        previousX = x;
        previousY = y;
    }

    public Point3f getScreenBottom() {
        return new Point3f(camera.unproject(0, height));
    }


    public Point3f getScreenTop() {
        return camera.unproject(width, 0);
    }


    private Hexagon touch(final HexCoord t, final int ttl) {
        if (ttl > 0) {
            for (final int[] n : t.getR() % 2 == 0 ? evenNeighbors : oddNeighbors) {
                getMap().getHexagon(t.getQ() + n[0], t.getR() + n[1]);
                if (getMap().hasHexagon(t.getQ() + n[0], t.getR() + n[1])) {
                    Hexagon h = getMap().getHexagon(t.getQ() + n[0], t.getR() + n[1]);
                    h.flip((h.getColor() + 1) % HexColor.mColorNumber, i);
                    Timer t1 = new Timer("t");

                    t1.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //System.out.println("timer");
                            touch(new HexCoord(t.getQ() + n[0], t.getR() + n[1]), ttl - 1);
                        }
                    }, 500);
                }
            }
        }

        Hexagon hexagon = getMap().getHexagon(t);
        hexagon.flip((hexagon.getColor() + 1) % (HexColor.mColorNumber - 1), i);

        box.move(hexagon.getCenter());

        return hexagon;
    }

}
