package com.salmito.hex.engine.things;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.programs.hex.HexProgram;
import com.salmito.hex.programs.camera.CameraProgram;
import com.salmito.hex.util.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by tiago on 8/13/2015.
 */
public class Box implements Thing {
    static float[] colors = {
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,

            // Left plane
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,
            1, 0, 0, 1,

            // Top plane
            0, 1, 0, 1,
            0, 1, 0, 1,
            0, 1, 0, 1,
            0, 1, 0, 1,

            // Bottom plane
            0, 1, 0, 1,
            0, 1, 0, 1,
            0, 1, 0, 1,
            0, 1, 0, 1,

            // Front plane
            0, 0, 1, 1,
            0, 0, 1, 1,
            0, 0, 1, 1,
            0, 0, 1, 1,

            // Back plane
            0, 0, 1, 1,
            0, 0, 1, 1,
            0, 0, 1, 1,
            0, 0, 1, 1,
    };

    private static final FloatBuffer mColors = ByteBuffer.allocateDirect(colors.length * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(colors);

    static float[] normals = {
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,
            1, 0, 0,

            // Left plane
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,
            -1, 0, 0,

            // Top plane
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,
            0, 1, 0,

            // Bottom plane
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,
            0, -1, 0,

            // Front plane
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,

            // Back plane
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
            0, 0, -1,
    };
    private static final FloatBuffer mNormals = ByteBuffer.allocateDirect(normals.length * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(normals);

    static short[] indices = {
            // Right plane
            0, 1, 2,
            0, 2, 3,

            // Left plane
            4, 5, 6,
            4, 6, 7,

            // Top plane
            8, 9, 10,
            8, 10, 11,

            // Bottom plane
            12, 13, 14,
            12, 14, 15,

            // Front plane
            16, 17, 18,
            16, 18, 19,

            // Back plane
            20, 21, 22,
            20, 22, 23,
    };
    private static final ShortBuffer mIndices = ByteBuffer.allocateDirect(indices.length * Constants.bytesPerShort).order(ByteOrder.nativeOrder()).asShortBuffer().put(indices);

    private float dx;
    private float dy;
    private float dz;

    private final FloatBuffer mVertices;
    private Point3f center;

    public Box(Point3f center, float dx, float dy, float dz) {
        this.center = center;
        this.mVertices = ByteBuffer.allocateDirect((24*3) * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();

        this.set(dx, dy, dz);

        mColors.position(0);
        mIndices.position(0);
        mNormals.position(0);
    }

    public Box(Point3f center) {
        this(center, 1f, 1f, 1f);
    }

    public Box() {
        this(new Point3f(0f, 0f, 0f));
    }

    public void move(Point3f center) {
        this.center.set(center);
    }

    public void set(float dx, float dy, float dz) {
        dx /= 2;
        dy /= 2;
        dz /= 2;

        this.dx = dx;
        this.dy = dy;
        this.dz = dz;

        float vertices[] = {
                dx, -dy, dz,
                dx, -dy, -dz,
                dx, dy, -dz,
                dx, dy, dz,

                // Left plane
                -dx, -dy, -dz,
                -dx, -dy, dz,
                -dx, dy, dz,
                -dx, dy, -dz,

                // Top plane
                -dx, dy, dz,
                dx, dy, dz,
                dx, dy, -dz,
                -dx, dy, -dz,

                // Bottom plane
                -dx, -dy, -dz,
                dx, -dy, -dz,
                dx, -dy, dz,
                -dx, -dy, dz,

                // Front plane
                -dx, -dy, dz,
                dx, -dy, dz,
                dx, dy, dz,
                -dx, dy, dz,

                // Back plane
                dx, -dy, -dz,
                -dx, -dy, -dz,
                -dx, dy, -dz,
                dx, dy, -dz,
        };
        mVertices.position(0);
        mVertices.put(vertices);
    }

    @Override
    public void draw(long time, CameraProgram program) {
        HexProgram p = HexProgram.getProgram();

        Matrix.setIdentityM(p.getModelMatrix(), 0);
        Matrix.translateM(p.getModelMatrix(), 0, center.getX(), center.getY(), center.getZ());

        mVertices.rewind();
        mColors.rewind();
        mIndices.rewind();
        p.drawBuffer(mVertices, mColors, mIndices, GLES20.GL_TRIANGLES);
        Matrix.setIdentityM(p.getModelMatrix(), 0);
    }

    @Override
    public void clean() {

    }
}
