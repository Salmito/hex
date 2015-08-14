package com.salmito.hex.programs.hex.entities;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.salmito.hex.engine.Thing;
import com.salmito.hex.main.MainRenderer;
import com.salmito.hex.programs.hex.HexProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Hexagon implements Thing {
    public static final short[] indices = {0, 1, 2, 3, 4, 5, 6, 1};
    public static final ShortBuffer mHexagonIndices = ByteBuffer.allocateDirect(indices.length * MainRenderer.mBytesPerShort).order(ByteOrder.nativeOrder()).asShortBuffer().put(indices);
    public static final short[] indicesWire = {1, 2, 3, 4, 5, 6, 1};
    public static final ShortBuffer mHexagonIndicesWire = ByteBuffer.allocateDirect(indicesWire.length * MainRenderer.mBytesPerShort).order(ByteOrder.nativeOrder()).asShortBuffer().put(indicesWire);
    public static final int mPositionDataSize = 3;
    public static final int mStrideBytes = mPositionDataSize * MainRenderer.mBytesPerFloat;
    public static float radius = 1.0f;
    public static final float xOff = radius * (float) (Math.cos(Math.PI / 6));
    public static final float yOff = radius * (float) (Math.sin(Math.PI / 6));
    private static final float vertices[] = {
            0.0f, 0.0f, 0.0f,    //center
            (float) (radius * Math.cos(2 * Math.PI * (0 + 0.5f) / 6)), (float) (radius * Math.sin(2 * Math.PI * (0 + 0.5f) / 6)), 0.0f,    // top
            (float) (radius * Math.cos(2 * Math.PI * (1 + 0.5f) / 6)), (float) (radius * Math.sin(2 * Math.PI * (1 + 0.5f) / 6)), 0.0f,    // left top
            (float) (radius * Math.cos(2 * Math.PI * (2 + 0.5f) / 6)), (float) (radius * Math.sin(2 * Math.PI * (2 + 0.5f) / 6)), 0.0f,    // left bottom
            (float) (radius * Math.cos(2 * Math.PI * (3 + 0.5f) / 6)), (float) (radius * Math.sin(2 * Math.PI * (3 + 0.5f) / 6)), 0.0f,    // bottom
            (float) (radius * Math.cos(2 * Math.PI * (4 + 0.5f) / 6)), (float) (radius * Math.sin(2 * Math.PI * (4 + 0.5f) / 6)), 0.0f,    // right bottom
            (float) (radius * Math.cos(2 * Math.PI * (5 + 0.5f) / 6)), (float) (radius * Math.sin(2 * Math.PI * (5 + 0.5f) / 6)), 0.0f,    // right top
    };
    public static final FloatBuffer mHexagonVertices = ByteBuffer.allocateDirect(vertices.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
    public static float xF = 0.0f;
    public static float yF = 0.0f;
    private static HexBuffers buffers;
    private int color = HexColor.WHITE;
    private float rotateAngle = 90.0f;
    private boolean rotate = false;
    private int flipColor = -1;
    private int flipDirection = -1;
    private long lastFlip = 0L;
    private float upX = 1f, upY = 0f, upZ = 0f;
    private int i = 0;
    private HexMap.Coordinates coordinates;
    private HexProgram program;

    public Hexagon(HexProgram program, int r, int q) {
        this.program = program;
        this.coordinates = new HexMap.Coordinates(r, q);
    }

    public Hexagon(int color) {
        this.setColor(color);
    }

    private static HexBuffers getBuffers() {
        if (buffers == null) {
            buffers = new HexBuffers();
        }
        return buffers;
    }

    public float getRotateAngle() {
        return rotateAngle;
    }

    public void flip(int color, int direction) {
        flipColor = color;
        rotateAngle = 0.0f;
        lastFlip = SystemClock.uptimeMillis();
        upX = (float) Math.cos(2 * Math.PI * (i++ % 6 + 0.5f) / 6);
        upY = (float) Math.sin(2 * Math.PI * (i++ % 6 + 0.5f) / 6);
        rotate = true;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(long dt) {
        HexBuffers buffers = getBuffers();

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers.vertices);
        GLES20.glVertexAttribPointer(program.getAttrib("a_Position"), mPositionDataSize, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(program.getAttrib("a_Position"));

        float xf = xOff * coordinates.getQ() * 2;
        final float yf = yOff * coordinates.getR() * 3;
        if ((coordinates.getR() & 1) != 0)    // if the current line is not even
            xf += xOff;     // extra offset of half the width on xPos axis

        //if(xPos<=2 && yPos<=2) System.out.println("Hexagon ("+xPos+","+yPos+") "+"Center xPos="+xf+" yPos="+yf);


        Matrix.setIdentityM(program.getmModelMatrix(), 0);
        Matrix.translateM(program.getmModelMatrix(), 0, xf, yf, 0.0f);

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
                Matrix.rotateM(program.getmModelMatrix(), 0, rotateAngle, upX, upY, upZ);
            }
        }
        HexColor.setColor(color);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers.color);
        GLES20.glVertexAttribPointer(program.getAttrib("a_Color"), HexColor.mColorDataSize, GLES20.GL_FLOAT, false, 0, HexColor.offset);
        GLES20.glEnableVertexAttribArray(program.getAttrib("a_Color"));

        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmViewMatrix(), 0, program.getmModelMatrix(), 0);
        Matrix.multiplyMM(program.getmMVPMatrix(), 0, program.getmProjectionMatrix(), 0, program.getmMVPMatrix(), 0);
        GLES20.glUniformMatrix4fv(program.getUniform("u_MVPMatrix"), 1, false, program.getmMVPMatrix(), 0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers.index);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_FAN, indices.length, GLES20.GL_UNSIGNED_SHORT, 0);

        HexColor.setColor(HexColor.WHITE);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers.color);
        GLES20.glVertexAttribPointer(program.getAttrib("a_Color"), HexColor.mColorDataSize, GLES20.GL_FLOAT, false, 0, HexColor.offset);
        GLES20.glEnableVertexAttribArray(program.getAttrib("a_Color"));

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers.indexwire);
        GLES20.glDrawElements(GLES20.GL_LINE_LOOP, indicesWire.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void clean() {

    }

    static private class HexBuffers {
        int vertices;
        int index;
        int indexwire;
        int color;

        private HexBuffers() {
            int[] i = new int[4];
            GLES20.glGenBuffers(4, i, 0);
            vertices = i[0];
            index = i[1];
            indexwire = i[2];
            color = i[3];

            mHexagonVertices.position(0);
            mHexagonIndices.position(0);
            mHexagonIndicesWire.position(0);
            HexColor.mHexagonColors.position(0);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertices);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mHexagonVertices.capacity() * MainRenderer.mBytesPerFloat, mHexagonVertices, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, color);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, HexColor.mHexagonColors.capacity() * MainRenderer.mBytesPerFloat, HexColor.mHexagonColors, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, index);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, mHexagonIndices.capacity() * MainRenderer.mBytesPerShort, mHexagonIndices, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexwire);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, mHexagonIndicesWire.capacity() * MainRenderer.mBytesPerShort, mHexagonIndicesWire, GLES20.GL_STATIC_DRAW);

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }
}
