package com.salmito.hex.engine;

import android.opengl.GLES20;

/**
 * Created by tiago on 8/13/2015.
 */
public class Shader {

    static public int createShader(String source, int type) {
        int handle;
        handle = GLES20.glCreateShader(type);
        if (handle == 0) {
            throw new RuntimeException("Unable to Create Shader Object: glGetError: " + GLES20.glGetError());
        }
        GLES20.glShaderSource(handle, source);
        GLES20.glCompileShader(handle);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(handle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            String log = GLES20.glGetShaderInfoLog(handle);
            GLES20.glDeleteShader(handle);
            handle = 0;
            throw new RuntimeException("Unable to compile Shader Object: glGetError: " + log);
        }
        return handle;
    }
}
