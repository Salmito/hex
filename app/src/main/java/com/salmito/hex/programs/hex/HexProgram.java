package com.salmito.hex.programs.hex;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.salmito.hex.engine.Program;
import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.Box;
import com.salmito.hex.engine.things.camera.Camera;
import com.salmito.hex.engine.things.geometry.Line3f;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.math.easing.EasingFunction;
import com.salmito.hex.programs.hex.entities.HexColor;
import com.salmito.hex.programs.hex.entities.HexCoord;
import com.salmito.hex.programs.hex.entities.HexMap;
import com.salmito.hex.programs.hex.entities.Hexagon;
import com.salmito.hex.programs.mvp.CameraProgram;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tiago on 8/13/2015.
 */
public class HexProgram extends CameraProgram {

    final static int[][] evenNeighbors = {{1, 0}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
    final static int[][] oddNeighbors = {{1, 0}, {1, -1}, {0, -1}, {-1, 0}, {0, 1}, {1, 1}};
    private static final String TAG = HexProgram.class.getName();

    private static final HexMap map = new HexMap(HexProgram.getProgram(), 100);
    private static final Box box = new Box(new Point3f(0f, 0f, 0f), 1f, 1f, 1f);

    private static HexProgram currentProgram;

    private final Line3f line;

    public float previousX, previousY;
    private int i = 0;
    private int curEasing = 0;
    private float cameraHeight;
    private boolean drawLine;

    public HexProgram() {
        super();
        System.out.println("Creating Hex Program");
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

    @Override
    public void surfaceChanged(int width, int height) {
        super.surfaceChanged(width, height);
    }

    @Override
    public void draw(long time) {
        super.draw(time);
        map.draw(time, this);
        box.draw(time, this);
        if (drawLine) {
            line.draw(time, this);
        }
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

        coordinates = getCamera().unproject(x, y);
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
        Log.d(TAG, "Eye pos: " + getCamera().getEye() + ", " + getCamera().getLook());
        Point3f c = getCamera().unproject(x, y);
        t = HexCoord.geo(c.getX(), c.getY());

        Hexagon h = touch(t);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        HexCoord t;

        Log.d(TAG, "Single tap detected in " + e.getX() + ", " + e.getY());
        Log.d(TAG, "Eye pos: " + getCamera().getEye() + ", " + getCamera().getLook());
        Point3f c = getCamera().unproject(x, y);
        t = HexCoord.geo(c.getX(), c.getY());

        Hexagon h = touch(t);

        Point3f center = h.getCenter();
        Point3f p1 = new Point3f(center.getX(), center.getY() - 5f, cameraHeight);
        Point3f p2 = new Point3f(center.getX(), center.getY(), 0f);

        getCamera().moveTo(p1, p2, 1f, EasingFunction.easings[curEasing++ % EasingFunction.easings.length]);

        if (coord1 == null) {
            coord1 = t;
        } else {
            for (HexCoord l : coord1.getLine(t)) {
                touch(l);
            }
            coord1 = null;
        }


        c = getCamera().unproject((int) e.getX(), (int) e.getY());
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
        Point3f point = getCamera().unproject((int) x, (int) y);

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                this.drawLine = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;
                if (e.getPointerCount() == 2) {
                    getCamera().move(dx / 100.0f, dy / 100.0f);
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
        return new Point3f(getCamera().unproject(0, height));
    }


    public Point3f getScreenTop() {
        return getCamera().unproject(width, 0);
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


    public void drawBuffer(FloatBuffer mVertices, FloatBuffer mColors, ShortBuffer mIndices, int type) {
        GLES20.glVertexAttribPointer(getAttrib("a_Position"), 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(getAttrib("a_Position"));

        GLES20.glVertexAttribPointer(getAttrib("a_Color"), 4, GLES20.GL_FLOAT, false, 0, mColors);
        GLES20.glEnableVertexAttribArray(getAttrib("a_Color"));

        Matrix.multiplyMM(getMVPMatrix(), 0, getCamera().getViewMatrix(), 0, getModelMatrix(), 0);
        Matrix.multiplyMM(getMVPMatrix(), 0, getCamera().getProjectionMatrix(), 0, getMVPMatrix(), 0);

        GLES20.glUniformMatrix4fv(getUniform("u_MVPMatrix"), 1, false, getMVPMatrix(), 0);
        GLES20.glDrawElements(type, mIndices.capacity(), GLES20.GL_UNSIGNED_SHORT, mIndices);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
