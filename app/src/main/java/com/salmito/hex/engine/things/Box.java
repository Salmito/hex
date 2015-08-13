package com.salmito.hex.engine.things;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.main.MainRenderer;
import com.salmito.hex.programs.hex.HexProgram;

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

    private static final FloatBuffer mColors = ByteBuffer.allocateDirect(colors.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(colors);

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
    private static final FloatBuffer mNormals = ByteBuffer.allocateDirect(normals.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(normals);

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
    private static final ShortBuffer mIndices = ByteBuffer.allocateDirect(indices.length * MainRenderer.mBytesPerShort).order(ByteOrder.nativeOrder()).asShortBuffer().put(indices);

    private float dx;
    private float dy;
    private float dz;

    private FloatBuffer mVertices;

    private int indicesBuffer;
    private int normalsBuffer;
    private int verticesBuffer;
    private Point center;

    public Box(Point center, float dx, float dy, float dz) {
        this.center = center;
        this.set(dx, dy, dz);
    }

    public Box(Point center) {
        this(center, 1f, 1f, 1f);
    }

    public Box() {
        this(new Point(0f, 0f, 0f));
    }

    public void move(Point center) {
        this.center=center;
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
        this.mVertices = ByteBuffer.allocateDirect(vertices.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);

        //if (verticesBuffer != 0) {
//            GLES20.glDeleteBuffers(1, new int[]{verticesBuffer}, 0);
//        }

//        int[] i = {0};
//        GLES20.glGenBuffers(1, i, 0);
//        verticesBuffer = i[0];

//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verticesBuffer);
//        mVertices.position(0);
//        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length, mVertices, GLES20.GL_STATIC_DRAW);
    }

    @Override
    public void draw(long time) {
        mVertices.position(0);
        mColors.position(0);
        mIndices.position(0);
        HexProgram program = HexProgram.getProgram();
        Matrix.setIdentityM(program.getmModelMatrix(), 0);
        Matrix.translateM(program.getmModelMatrix(), 0, center.getX(), center.getY(), center.getZ());

        GLES20.glVertexAttribPointer(HexProgram.getProgram().getAttr("a_Position"), 3, GLES20.GL_FLOAT, false, 3 * MainRenderer.mBytesPerFloat, mVertices);
        GLES20.glEnableVertexAttribArray(HexProgram.getProgram().getAttr("a_Position"));
        GLES20.glVertexAttribPointer(HexProgram.getProgram().getAttr("a_Color"), 4, GLES20.GL_FLOAT, false, 4 * MainRenderer.mBytesPerFloat, mColors);
        GLES20.glEnableVertexAttribArray(HexProgram.getProgram().getAttr("a_Color"));
        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmViewMatrix(), 0, program.getmModelMatrix(), 0);
        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmProjectionMatrix(), 0, program.getmMVPMatrix(), 0);
        GLES20.glUniformMatrix4fv(program.getUniform("u_MVPMatrix"), 1, false, program.getmMVPMatrix(), 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, mIndices);
    }

    @Override
    public void clean() {

    }
}
