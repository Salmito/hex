package com.salmito.hex.main;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import com.salmito.hex.engine.Program;
import com.salmito.hex.programs.HudProgram;
import com.salmito.hex.programs.hex.HexProgram;
import com.salmito.hex.programs.simple.SimpleProgram;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainRenderer implements GLSurfaceView.Renderer {

    public static final int mBytesPerFloat = 4;
    public static final int mBytesPerShort = 2;

    private static ArrayList<Program> programs;
    private long lastTime;

    public static ArrayList<Program> getPrograms() {
        if (programs == null) {
            programs = new ArrayList<Program>();
        }
        return programs;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        long now = SystemClock.uptimeMillis();
        long dt = now - lastTime;
        lastTime = now;

        for (Program p : getPrograms()) {
            cleanup();
            p.use();
            p.draw(dt);
        }
    }

    private void cleanup() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        for (Program p : getPrograms()) {
            p.surfaceChanged(width, height);
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        lastTime = SystemClock.uptimeMillis();

        getPrograms().add(HudProgram.getProgram());
        getPrograms().add(HexProgram.getProgram());
    }

}
