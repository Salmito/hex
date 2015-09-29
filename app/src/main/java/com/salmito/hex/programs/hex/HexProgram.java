package com.salmito.hex.programs.hex;

import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import com.salmito.hex.engine.things.Box;
import com.salmito.hex.engine.things.geometry.Line3f;
import com.salmito.hex.engine.things.geometry.Point2f;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.math.easing.EasingFunction;
import com.salmito.hex.programs.camera.CameraProgram;
import com.salmito.hex.programs.hex.entities.HexColor;
import com.salmito.hex.programs.hex.entities.HexCoord;
import com.salmito.hex.programs.hex.entities.HexLayout;
import com.salmito.hex.programs.hex.entities.HexMap;
import com.salmito.hex.programs.hex.entities.HexOrientation;
import com.salmito.hex.programs.hex.entities.Hexagon;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tiago on 8/13/2015.
 */
public class HexProgram extends CameraProgram {

    public static HexLayout layout = new HexLayout(HexOrientation.POINTY, new Point2f(1f, 1f), new Point2f(0f, 0f));

    private static final String TAG = HexProgram.class.getName();

    private static final HexMap map = new HexMap(100);
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
        line = new Line3f(new Point3f(0f, 0f, 0f), new Point3f(10f, 0f, 0f));
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
        t = HexCoord.geo_to_hex(coordinates.getX(), coordinates.getY(), layout);
        Log.d(TAG, "Double tap detected in " + (t.getQ()) + " " + (t.getR()) + " " + getMap().hasHexagon(t));
        Hexagon h = touch(t, 1);

        Point3f center = h.getCenter();
        Point3f p1 = new Point3f(center.getX(), center.getY() - 5f, cameraHeight);
        Point3f p2 = new Point3f(center.getX(), center.getY(), 0f);

        getCamera().moveTo(p1, p2, 1f, EasingFunction.easings[18]);
    }

    @Override
    public void onSingleTapConfirmed(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        HexCoord t;

        Log.d(TAG, "Single tap detected in " + e.getX() + ", " + e.getY());
        Log.d(TAG, "Eye pos: " + getCamera().getEye() + ", " + getCamera().getLook());
        Point3f c = getCamera().unproject(x, y);
        t = HexCoord.geo_to_hex(c.getX(), c.getY(), layout);

        Hexagon h = touch(t);
    }

    //    HexLayout l1 = new HexLayout(HexOrientation.FLAT, new Point2f(1f, 1f), new Point2f(0f, 0f));
//    HexLayout l2 = new HexLayout(HexOrientation.POINTY, new Point2f(1f, 1f), new Point2f(0f, 0f));
////    HexLayout cur = l2;
    @Override
    public void onLongPress(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        HexCoord t;

        Log.d(TAG, "Single tap detected in " + e.getX() + ", " + e.getY());
        Log.d(TAG, "Eye pos: " + getCamera().getEye() + ", " + getCamera().getLook());

        HexCoord bottomLeftCoord = HexCoord.geo_to_hex(getCamera().unproject(0, height), layout);
        HexCoord topLeftCoord = HexCoord.geo_to_hex(getCamera().unproject(0, 0), layout);
        HexCoord bottomRightCoord = HexCoord.geo_to_hex(getCamera().unproject(width, height), layout);
        HexCoord topRightCoord = HexCoord.geo_to_hex(getCamera().unproject(width, 0), layout);

        touch(bottomLeftCoord);
        touch(topLeftCoord);
        touch(bottomRightCoord);
        touch(topRightCoord);

        for (HexCoord l : bottomLeftCoord.getLine(topLeftCoord)) {
            touch(l);
        }
        for (HexCoord l : topLeftCoord.getLine(topRightCoord)) {
            touch(l);
        }

        for (HexCoord l : topRightCoord.getLine(bottomRightCoord)) {
            touch(l);
        }

        for (HexCoord l : bottomRightCoord.getLine(bottomLeftCoord)) {
            touch(l);
        }

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

    private Hexagon touch(final HexCoord t, final int ttl) {
        if (ttl > 0) {
            for (int i = 0; i < 6; i++) {
                Timer t1 = new Timer("t");
                final int p = i;
                t1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //System.out.println("timer");
                        touch(t.neighbor(p), ttl - 1);
                    }
                }, 500);
            }
        }

        Hexagon hexagon = getMap().getHexagon(t);
        //   hexagon.setColor((hexagon.getColor() + 1) % (HexColor.mColorNumber - 1));
        hexagon.flip((hexagon.getColor() + 1) % (HexColor.mColorNumber - 1), i);

        box.move(hexagon.getCenter());

        return hexagon;
    }
}
