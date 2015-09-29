package com.salmito.hex.programs.buffer;

import android.opengl.GLES20;
import android.view.MotionEvent;

import com.salmito.hex.programs.Program;
import com.salmito.hex.programs.background.BackgroundProgram;

/**
 * Created by Tiago on 17/08/2015.
 */
public class BufferProgram extends Program {

    private Target back;
    private Target front;
    BackgroundProgram background;

    private static final String vertex="attribute vec3 position; void main() { gl_Position = vec4( position, 1.0 ); }";
    private static final String fragment="#ifdef GL_ES\n" +
            "\t\t\tprecision highp float;\n" +
            "\t\t\t#endif\n" +
            "\n" +
            "\t\t\tuniform vec2 resolution;\n" +
            "\t\t\tuniform sampler2D texture;\n" +
            "\n" +
            "\t\t\tvoid main() {\n" +
            "\n" +
            "\t\t\t\tvec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
            "\t\t\t\tgl_FragColor = texture2D( texture, uv );\n" +
            "\t\t\t}";

    public BufferProgram(BackgroundProgram program) {
        super(vertex, fragment);
        addUniform("resolution");
        addUniform("texture");
        addAttr("position");

        this.front=new Target(1920,1080);
        this.back=new Target(1920,1080);

        this.background = program;
    }

    @Override
    public void touchEvent(MotionEvent ev) {
        background.touchEvent(ev);
    }

    @Override
    public void draw(long currentTime) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, back.texture );
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, front.framebuffer );
        background.use();
        background.draw(currentTime);
        use();
        GLES20.glUniform2f(getUniform("resolution"), width, height);
        GLES20.glUniform1i(getUniform("texture"), 1);

        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, background.getBufferId());
        GLES20.glVertexAttribPointer(getAttrib("position"), 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(getAttrib("position"));
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, front.texture);
        GLES20.glBindFramebuffer( GLES20.GL_FRAMEBUFFER, 0);
        //GLES20.glClear( gl.GL_COLOR_BUFFER_BIT + gl.GL_DEPTH_BUFFER_BIT );
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        Target temp=front;
        front=back;
        back=temp;

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void onSwipeLeft() {
        background.onSwipeLeft();
    }

    @Override
    public void onSwipeRight() {
        background.onSwipeRight();
    }

    @Override
    public void surfaceChanged(int width, int height) {
        super.surfaceChanged(width,height);
        this.front=new Target(width, height);
        this.back=new Target(width, height);
        background.surfaceChanged(width, height);
    }
}
