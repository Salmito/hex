package com.salmito.hex.engine;

import android.opengl.GLES20;
import android.view.MotionEvent;

import com.salmito.hex.util.GLHelper;

import java.util.HashMap;

/**
 * Created by tiago on 8/13/2015.
 */
public abstract class Program implements Thing {
    protected final HashMap<String, Integer> uniforms;
    protected final HashMap<String, Integer> attribs;
    protected final int programHandle;

    protected int width;
    protected int height;

    public Program(String s, String f) {
        this(s, f, new String[]{}, new String[]{});
    }

    public Program(String vertex, String fragment, String[] uniforms, String[] attribs) {
        this.uniforms = new HashMap<String, Integer>();
        this.attribs = new HashMap<String, Integer>();
        this.programHandle = compileProgram(vertex, fragment);
        this.use();
        for (String u : uniforms) {
            addUniform(u);
        }
        for (String a : attribs) {
            addAttr(a);
        }
    }

    public static int compileProgram(String vertex, String fragment) {
        int vertexShaderHandle = GLHelper.createShader(vertex, GLES20.GL_VERTEX_SHADER);
        int fragmentShaderHandle = GLHelper.createShader(fragment, GLES20.GL_FRAGMENT_SHADER);

        int programHandle = GLES20.glCreateProgram();

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program: " + GLES20.glGetError());
        }

        GLES20.glAttachShader(programHandle, vertexShaderHandle);
        GLES20.glAttachShader(programHandle, fragmentShaderHandle);

        GLES20.glLinkProgram(programHandle);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programHandle);
            programHandle = 0;
            throw new RuntimeException("Error creating program: " + GLES20.glGetError());
        }

        return programHandle;
    }

    private int getHandle() {
        return programHandle;
    }

    public void use() {
        GLES20.glUseProgram(this.programHandle);
    }

    public void surfaceChanged(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int addUniform(String u) {
        int i = GLES20.glGetUniformLocation(this.programHandle, u);
        uniforms.put(u, i);
        return i;
    }

    public int addAttr(String a) {
        int i = GLES20.glGetAttribLocation(this.programHandle, a);
        attribs.put(a, i);
        return i;
    }

    public int getUniform(String u) {
        Integer i = uniforms.get(u);
        if (i == null) throw new RuntimeException("Uniform '" + u + "' not defined");
        return i;
    }

    public int getAttrib(String a) {
        Integer i = attribs.get(a);
        if (i == null) throw new RuntimeException("Attrib '" + a + "' not defined");
        GLES20.glEnableVertexAttribArray(i);
        return i;
    }

    public void clean() {
        GLES20.glDeleteProgram(this.programHandle);
    }

    public void touchEvent(MotionEvent ev) {

    }

}
