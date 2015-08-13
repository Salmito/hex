package com.salmito.hex.engine;

import android.opengl.GLES20;

/**
 * Created by Tiago on 10/08/2015.
 */
public class Target {
    public int framebuffer;
    public int renderbuffer;
    public int texture;

    private Target() {

    }

    static public Target createTarget(int width, int height) {
        Target t = new Target();
        int[] i = {0};
        GLES20.glGenFramebuffers(1, i, 0);
        t.framebuffer = i[0];

        GLES20.glGenRenderbuffers(1, i, 0);
        t.renderbuffer = i[0];

        GLES20.glGenTextures(1, i, 0);
        t.texture = i[0];

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, t.texture);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);


        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );

        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );

        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, t.framebuffer );
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, t.texture, 0);

        //set up renderbuffer

        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, t.renderbuffer );

        GLES20.glRenderbufferStorage( GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, width, height );
        GLES20.glFramebufferRenderbuffer( GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, t.renderbuffer );

        // clean up

        GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, 0 );
        GLES20.glBindRenderbuffer( GLES20.GL_RENDERBUFFER, 0 );
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0);

        return t;
    }

}
