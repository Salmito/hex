package com.salmito.hex.programs.hex.entities;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.geometry.Point2f;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.programs.hex.HexProgram;
import com.salmito.hex.programs.mvp.CameraProgram;
import com.salmito.hex.util.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Hexagon implements Thing {
    public static final short[] indices = {0, 1, 2, 3, 4, 5, 0};
    public static final ShortBuffer mHexagonIndices = ByteBuffer.allocateDirect(indices.length * Constants.bytesPerShort).order(ByteOrder.nativeOrder()).asShortBuffer().put(indices);
    public static final FloatBuffer mHexagonVertices = ByteBuffer.allocateDirect(6 * 3 * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
    private final Point3f center;
    private int color = HexColor.WHITE;
    private float rotateAngle = 90.0f;
    private boolean rotate = false;
    private int flipColor = -1;
    private int flipDirection = -1;
    private long lastFlip = 0L;
    private float upX = 1f, upY = 0f, upZ = 0f;
    private int i = 0;
    private HexCoord coordinates;
    private HexProgram program;

    public HexCoord getCoordinates() {
        return coordinates;
    }


    public Hexagon(HexProgram program, int r, int q) {
        this.program = program;
        this.coordinates = new HexCoord(r, q);
        this.center = new Point3f(coordinates.to_geo(HexProgram.layout));
    }

    public Point3f getCenter() {
        return center;
    }

    public void flip(int color, int direction) {
        flipColor = color;
        rotateAngle = 0.0f;
        lastFlip = SystemClock.uptimeMillis();
        upX = (float) Math.cos(2 * Math.PI * (i++ % 6 + 0.5f) / 6);
        upY = (float) Math.sin(2 * Math.PI * (i++ % 6 + 0.5f) / 6);
        rotate = true;
    }

    private Point2f hexCornerOffset(HexLayout layout, int corner) {
        Point2f size = layout.getSize();
        double angle = 2.0 * Math.PI *
                (corner + layout.getOrientation().start_angle) / 6;
        return new Point2f((float) (size.getX() * Math.cos(angle)), (float) (size.getY() * Math.sin(angle)));
    }

    private FloatBuffer getHexCorners(HexLayout layout) {
        mHexagonVertices.rewind();

        Point2f center = coordinates.to_geo(layout);

        for (int i = 0; i < 6; i++) {
            Point2f p = hexCornerOffset(layout, i);
            mHexagonVertices.put(center.getX() + p.getX());
            mHexagonVertices.put(center.getY() + p.getY());
            mHexagonVertices.put(0f);
        }
        return mHexagonVertices;

    }

    @Override
    public void draw(long dt, CameraProgram program) {
        if (rotate) {

            long time = SystemClock.uptimeMillis() - lastFlip;
            if (time > 500L || rotateAngle > 360f) {
                rotate = false;
                rotateAngle = 0f;
            } else {
                rotateAngle += (360f * time) / 500f;
                if (rotateAngle >= 180f && flipColor >= 0) {
                    color = flipColor;
                    flipColor = -1;
                }
                Matrix.setIdentityM(this.program.getModelMatrix(), 0);
                Matrix.translateM(this.program.getModelMatrix(), 0, center.getX(), center.getY(), 0.0f);
                Matrix.rotateM(this.program.getModelMatrix(), 0, rotateAngle, upX, upY, upZ);
                Matrix.translateM(this.program.getModelMatrix(), 0, -center.getX(), -center.getY(), 0.0f);
            }

        }

        getHexCorners(HexProgram.layout);
        mHexagonVertices.rewind();
        mHexagonIndices.rewind();
        program.drawBuffer(mHexagonVertices, HexColor.getColor(color), mHexagonIndices, GLES20.GL_TRIANGLE_FAN);
        mHexagonVertices.rewind();
        mHexagonIndices.rewind();
//        Matrix.setIdentityM(this.program.getModelMatrix(), 0);
//        Matrix.translateM(this.program.getModelMatrix(), 0, center.getX(), center.getY(), 0.0f);
        program.drawBuffer(mHexagonVertices, HexColor.getColor(1f,0f,0f,1f), mHexagonIndices, GLES20.GL_LINE_LOOP);
        Matrix.setIdentityM(this.program.getModelMatrix(), 0);
    }

    @Override
    public void clean() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
