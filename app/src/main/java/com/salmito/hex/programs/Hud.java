package com.salmito.hex.programs;

import android.opengl.GLES20;
import android.os.SystemClock;

import com.salmito.hex.main.MainRenderer;
import com.salmito.hex.game.Shaders;
import com.salmito.hex.engine.Target;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//import static com.salmito.hex.main.MainRenderer.compileProgram;

/**
 * Created by Tiago on 10/08/2015.
 */
public class Hud {
    private final long startTime;
    private static final float vertices[] = {-1.0f, -1.0f, 1.0f - 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f};
    public static final FloatBuffer mHudVertices = ByteBuffer.allocateDirect(vertices.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
    public static final FloatBuffer mSecondVertices = ByteBuffer.allocateDirect(vertices.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
    private int mBuffer;
    private int secondProgramHandle;
    private int hudProgramHandle;

    private int hudPositionHandle;
    private int hudResolutionHandle;
    private int hudTimeHandle;
    private int hudMouseHandle;

    private int secondPositionHandle;
    private int secondTextureHandle;
    private int secondResolutionHandle;
    //private int mBuffer2;


    public void setBackTarget(Target backTarget) {
        this.backTarget = backTarget;
    }

    public void setFrontTarget(Target frontTarget) {
        this.frontTarget = frontTarget;
    }

    private Target frontTarget;
    private Target backTarget;

    private void initialize() {
        int[] i = {0};
        GLES20.glGenBuffers(1, i, 0);
        this.mBuffer = i[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
        mSecondVertices.position(0);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 12, mSecondVertices, GLES20.GL_STATIC_DRAW);

        frontTarget = Target.createTarget(1920, 1080);
        backTarget = Target.createTarget(1920, 1080);

     //   this.secondProgramHandle = compileProgram(Shaders.secondVertexShader, Shaders.secondFragmentShader);

        this.secondPositionHandle = GLES20.glGetAttribLocation(secondProgramHandle, "position");
        GLES20.glEnableVertexAttribArray(secondPositionHandle);
        this.secondTextureHandle = GLES20.glGetUniformLocation(secondProgramHandle, "texture");
        this.secondResolutionHandle = GLES20.glGetUniformLocation(secondProgramHandle, "resolution");

        GLES20.glUseProgram(secondProgramHandle);

        //GLES20.glGenBuffers(1, i, 0);
        //this.mBuffer2 = i[0];

//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
 //       GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 0, mSecondVertices, GLES20.GL_STATIC_DRAW);
 //       GLES20.glVertexAttribPointer(secondPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mSecondVertices);
  //      GLES20.glEnableVertexAttribArray(secondPositionHandle);

      //  hudProgramHandle = compileProgram(Shaders.hudVertexShader, Shaders.hudFragmentShader);
        GLES20.glUseProgram(hudProgramHandle);

        this.hudPositionHandle = GLES20.glGetAttribLocation(hudProgramHandle, "position");

        System.out.println("POSITION HANDLE "+hudPositionHandle+ " "+secondPositionHandle+" "+mBuffer);

        GLES20.glEnableVertexAttribArray(hudPositionHandle);

        this.hudResolutionHandle = GLES20.glGetUniformLocation(hudProgramHandle, "resolution");
        this.hudTimeHandle = GLES20.glGetUniformLocation(hudProgramHandle, "time");
        this.hudMouseHandle = GLES20.glGetUniformLocation(hudProgramHandle, "mouse");

  //      GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
   //     GLES20.glVertexAttribPointer(hudPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mHudVertices);
   //     GLES20.glEnableVertexAttribArray(hudPositionHandle);
    }

    public Hud() {
        this.startTime = SystemClock.uptimeMillis();
        initialize();
    }

    public void draw(int width, int height, long currentTime) {
        GLES20.glUseProgram(hudProgramHandle);

        GLES20.glUniform2f(hudResolutionHandle, width, height);
        float time = (currentTime - startTime) / 1000.0f;
        GLES20.glUniform1f(hudTimeHandle, time);
        GLES20.glUniform2f(hudMouseHandle, 0, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, backTarget.texture);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frontTarget.framebuffer);


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT + GLES20.GL_DEPTH_BUFFER_BIT);


       GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
        mSecondVertices.position(0);
       GLES20.glVertexAttribPointer(hudPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mSecondVertices);
       GLES20.glEnableVertexAttribArray(hudPositionHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        GLES20.glUseProgram(secondProgramHandle);

        GLES20.glUniform2f(secondResolutionHandle, width, height);
        GLES20.glUniform1i(secondTextureHandle, 1);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBuffer);
        mSecondVertices.position(0);
        GLES20.glVertexAttribPointer(secondPositionHandle, 2, GLES20.GL_FLOAT, false, 0, mSecondVertices);
        GLES20.glEnableVertexAttribArray(secondPositionHandle);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frontTarget.texture);

        //Render front buffer to screen
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT + GLES20.GL_DEPTH_BUFFER_BIT);

        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, mHudVertices);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        Target t = frontTarget;
        frontTarget = backTarget;
        backTarget = t;
        GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, 0 );
        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, 0 );
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0);
    }


}
