package com.salmito.hex.programs;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.salmito.hex.engine.Program;
import com.salmito.hex.engine.Utils;
import com.salmito.hex.game.Shaders;
import com.salmito.hex.main.MainRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//import static com.salmito.hex.main.MainRenderer.compileProgram;

/**
 * Created by Tiago on 10/08/2015.
 */
public class HudProgram extends Program {

    private static float[] vertices = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f};
    public static final FloatBuffer mHudVertices = ByteBuffer.allocateDirect(vertices.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
    private static HudProgram currentProgram;
    private final long startTime;
    private int mBuffer;

    public HudProgram() {
        super(Shaders.hudVertexShader, Shaders.hudFragmentShader);
        use();
        addUniform("time");
        addUniform("mouse");
        addUniform("resolution");

        addAttr("position");
        this.mBuffer = Utils.createBuffer();
        mHudVertices.position(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mHudVertices.capacity() * MainRenderer.mBytesPerFloat, mHudVertices, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        this.startTime = SystemClock.uptimeMillis();
    }

    public static HudProgram getProgram() {
        if (currentProgram == null) currentProgram = new HudProgram();
        return currentProgram;
    }

    public void draw(long currentTime) {
        use();

        GLES20.glUniform2f(getUniform("resolution"), width, height);
        float time = (SystemClock.uptimeMillis() - startTime) / 1000.0f;
        GLES20.glUniform1f(getUniform("time"), time);
        GLES20.glUniform2f(getUniform("mouse"), 0, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
        GLES20.glEnableVertexAttribArray(getAttrib("position"));
        GLES20.glVertexAttribPointer(getAttrib("position"), 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void surfaceChanged(int width, int height) {
        super.surfaceChanged(width, height);
    }
}
