package com.salmito.hex.programs.background;

import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.salmito.hex.engine.Program;
import com.salmito.hex.main.MainRenderer;
import com.salmito.hex.util.Constants;
import com.salmito.hex.util.GLHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//import static com.salmito.hex.main.MainRenderer.compileProgram;

/**
 * Created by Tiago on 10/08/2015.
 */
public class BackgroundProgram extends Program {

    private static float[] vertices = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f};
    public static final FloatBuffer mHudVertices = ByteBuffer.allocateDirect(vertices.length * Constants.bytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
    private static BackgroundProgram currentProgram;
    private final long startTime;

    public int getBufferId() {
        return bufferId;
    }

    private int bufferId;
    private float y;
    private float x;
    private int current = 0;

    public BackgroundProgram() {
        super(Shaders.hudVertexShader, Shaders.hudFragmentShaders[0]);
        init();
        this.startTime = SystemClock.uptimeMillis();
    }

    public static BackgroundProgram getProgram() {
        if (currentProgram == null) currentProgram = new BackgroundProgram();
        return currentProgram;
    }

    private void init() {
        use();
        addUniform("time");
        addUniform("mouse");
        addUniform("resolution");

        addAttr("position");
        this.bufferId = GLHelper.createBuffer();
        mHudVertices.position(0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mHudVertices.capacity() * Constants.bytesPerFloat, mHudVertices, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void touchEvent(MotionEvent ev) {
        super.touchEvent(ev);
        this.x = ev.getX();
        this.y = ev.getY();
    }

    public void draw(long currentTime) {
        GLES20.glUniform2f(getUniform("resolution"), width, height);
        float time = (SystemClock.uptimeMillis() - startTime) / 1000.0f;
        GLES20.glUniform1f(getUniform("time"), time);
        GLES20.glUniform2f(getUniform("mouse"), x, y);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);
        GLES20.glEnableVertexAttribArray(getAttrib("position"));
        GLES20.glVertexAttribPointer(getAttrib("position"), 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    private void setShader(final int i) {
        MainRenderer.getView().queueEvent(new Runnable() {
            @Override
            public void run() {
                clean();
                programHandle = compileProgram(Shaders.hudVertexShader, Shaders.hudFragmentShaders[i]);
                use();
                init();
            }
        });
    }

    @Override
    public void onSwipeLeft() {
        Log.d("Bacgkound", "Swipe left");

        current = (current - 1);
        if (current == -1) current = Shaders.hudFragmentShaders.length - 1;
        setShader(current);
    }

    @Override
    public void onSwipeRight() {
        Log.d("Bacgkound", "Swipe right");
        current = (current + 1) % Shaders.hudFragmentShaders.length;
        setShader(current);

    }


    @Override
    public void surfaceChanged(int width, int height) {
        super.surfaceChanged(width, height);
    }
}
