package com.salmito.hex.engine.things;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.engine.things.geometry.Point3f;
import com.salmito.hex.programs.hex.HexProgram;
import com.salmito.hex.util.Constants;
import com.salmito.hex.util.GLHelper;

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

    private FloatBuffer mVertices;

    private int indicesBuffer;
    private int normalsBuffer;
    private int verticesBuffer;
    private int colorBuffer;
    private Point3f center;

    public Box(Point3f center, float dx, float dy, float dz) {
        this.center = center;
        int[] i = new int[3];
        GLES20.glGenBuffers(3, i, 0);
        verticesBuffer = 0;
        indicesBuffer = i[0];
        normalsBuffer = i[1];
        colorBuffer = i[2];

        this.set(dx, dy, dz);

        mColors.position(0);
        mIndices.position(0);
        mNormals.position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, mIndices.capacity() * Constants.bytesPerShort, mIndices, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalsBuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mNormals.capacity() * Constants.bytesPerFloat, mNormals, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mColors.capacity() * Constants.bytesPerFloat, mColors, GLES20.GL_DYNAMIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public Box(Point3f center) {
        this(center, 1f, 1f, 1f);
    }

    public Box() {
        this(new Point3f(0f, 0f, 0f));
    }

    public void move(Point3f center) {
        this.center = center;
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
        this.mVertices = ByteBuffer.allocateDirect(vertices.length * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
        mVertices.position(0);
        if (verticesBuffer == 0) {
            verticesBuffer = GLHelper.createBuffer();
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verticesBuffer);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mVertices.capacity() * Constants.bytesPerFloat, mVertices, GLES20.GL_DYNAMIC_DRAW);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        } else {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verticesBuffer);
            GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, mVertices.capacity() * Constants.bytesPerFloat, mVertices);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        }
    }

    @Override
    public void draw(long time) {


        HexProgram program = HexProgram.getProgram();

        Matrix.setIdentityM(program.getmModelMatrix(), 0);
        Matrix.translateM(program.getmModelMatrix(), 0, center.getX(), center.getY(), center.getZ());

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verticesBuffer);
        GLES20.glVertexAttribPointer(HexProgram.getProgram().getAttrib("a_Position"), 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(HexProgram.getProgram().getAttrib("a_Position"));

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorBuffer);
        GLES20.glVertexAttribPointer(HexProgram.getProgram().getAttrib("a_Color"), 4, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(HexProgram.getProgram().getAttrib("a_Color"));

        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmViewMatrix(), 0, program.getmModelMatrix(), 0);
        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmProjectionMatrix(), 0, program.getmMVPMatrix(), 0);

        GLES20.glUniformMatrix4fv(program.getUniform("u_MVPMatrix"), 1, false, program.getmMVPMatrix(), 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        Matrix.setIdentityM(program.getmModelMatrix(), 0);
        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmViewMatrix(), 0, program.getmModelMatrix(), 0);
        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmProjectionMatrix(), 0, program.getmMVPMatrix(), 0);
    }

    @Override
    public void clean() {

    }
}
