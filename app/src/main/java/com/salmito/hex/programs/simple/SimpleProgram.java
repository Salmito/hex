package com.salmito.hex.programs.simple;


import android.opengl.GLES20;

import com.salmito.hex.engine.Program;
import com.salmito.hex.engine.Utils;
import com.salmito.hex.main.MainRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by tiago on 8/13/2015.
 */
public class SimpleProgram extends Program {

    private static String vertex = "attribute vec2 a_position;\n" +
            "\n" +
            "void main() {\n" +
            "  gl_Position = vec4(a_position, 0, 1);\n" +
            "}";

    private static String fragment = "void main() {\n" +
            "  gl_FragColor = vec4(0,1,0,1);  // green\n" +
            "}";

    private static float[] vertices = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            -1.0f, 1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f};

    private static final FloatBuffer mVertices = ByteBuffer.allocateDirect(vertices.length * MainRenderer.mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
    private static SimpleProgram currentProgram;


    private int buffer;

    public SimpleProgram() {
        super(vertex, fragment, new String[]{}, new String[]{"a_position"});
        this.use();
        //this.buffer = Utils.createBuffer();
        mVertices.position(0);
        //GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffer);
        //GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.length, mVertices, GLES20.GL_STATIC_DRAW);

    }

    public void draw(long time) {
        mVertices.position(0);
        //GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffer);

        GLES20.glEnableVertexAttribArray(getAttrib("a_position"));
        GLES20.glVertexAttribPointer(getAttrib("a_position"), 2, GLES20.GL_FLOAT, false, 2*4, mVertices);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
        GLES20.glDisableVertexAttribArray(getAttrib("a_position"));
    }

    public static SimpleProgram getProgram() {
        if(currentProgram==null) {
            currentProgram=new SimpleProgram();
        }
        return currentProgram;
    }
}
