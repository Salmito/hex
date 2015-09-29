package com.salmito.hex.programs.buffer;

import android.opengl.GLES20;

/**
 * Created by Tiago on 10/08/2015.
 */
public class Target {
    public final int framebuffer;
    public final int renderbuffer;
    public final int texture;

    public Target(int width, int height) {
        int[] i = {0};
        GLES20.glGenFramebuffers(1, i, 0);
        framebuffer = i[0];

        GLES20.glGenRenderbuffers(1, i, 0);
        renderbuffer = i[0];

        GLES20.glGenTextures(1, i, 0);
        texture = i[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);


        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );

        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );

        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, framebuffer );
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture, 0);

        //set up renderbuffer

        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, renderbuffer );

        GLES20.glRenderbufferStorage( GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height );
        GLES20.glFramebufferRenderbuffer( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderbuffer );

        // clean up

        GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, 0 );
        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, 0 );
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0);
    }

    static public Target createTarget(int width, int height) {
        Target t = new Target(width, height);
        return t;
    }

}
