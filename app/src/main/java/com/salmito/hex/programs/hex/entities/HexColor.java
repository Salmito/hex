package com.salmito.hex.programs.hex.entities;

import com.salmito.hex.main.MainRenderer;
import com.salmito.hex.util.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class HexColor {
    final public static int RED = 0;
    final public static int GREEN = 1;
    final public static int BLUE = 2;
    final public static int WHITE = 3;
    final public static int MAGENTA = 4;
    final public static int RAINBOW = 5;
    final public static int BLACK = 6;
    final public static int mColorNumber = 7;

    public static final int mColorDataSize = 4;
    public static final int mColorStrideBytes = mColorDataSize * Constants.bytesPerFloat;
    public static final int mColorSize = 7;
    public static final int mColorByteSize = mColorDataSize * mColorSize;

    public static int offset = 0;
    private static final float colors[] = {
            //RED
            1.0f, 0.0f, 0.0f, 1.0f,    //center
            1.0f, 0.0f, 0.0f, 1.0f,    // top
            1.0f, 0.0f, 0.0f, 1.0f,    // left top
            1.0f, 0.0f, 0.0f, 1.0f,   // left bottom
            1.0f, 0.0f, 0.0f, 1.0f,   // bottom
            1.0f, 0.0f, 0.0f, 1.0f,  // right bottom
            1.0f, 0.0f, 0.0f, 1.0f,   // right top

            //GREEN
            0.0f, 1.0f, 0.0f, 1.0f,    //center
            0.0f, 1.0f, 0.0f, 1.0f,    // top
            0.0f, 1.0f, 0.0f, 1.0f,    // left top
            0.0f, 1.0f, 0.0f, 1.0f,   // left bottom
            0.0f, 1.0f, 0.0f, 1.0f,   // bottom
            0.0f, 1.0f, 0.0f, 1.0f,  // right bottom
            0.0f, 1.0f, 0.0f, 1.0f,   // right top

            //BLUE
            0.0f, 0.0f, 1.0f, 1.0f,    //center
            0.0f, 0.0f, 1.0f, 1.0f,    // top
            0.0f, 0.0f, 1.0f, 1.0f,    // left top
            0.0f, 0.0f, 1.0f, 1.0f,   // left bottom
            0.0f, 0.0f, 1.0f, 1.0f,   // bottom
            0.0f, 0.0f, 1.0f, 1.0f,  // right bottom
            0.0f, 0.0f, 1.0f, 1.0f,   // right top

            //WHITE
            1.0f, 1.0f, 1.0f, 1.0f,    //center
            1.0f, 1.0f, 1.0f, 1.0f,    // top
            1.0f, 1.0f, 1.0f, 1.0f,    // left top
            1.0f, 1.0f, 1.0f, 1.0f,   // left bottom
            1.0f, 1.0f, 1.0f, 1.0f,   // bottom
            1.0f, 1.0f, 1.0f, 1.0f,  // right bottom
            1.0f, 1.0f, 1.0f, 1.0f,   // right top

            //MAGENTA
            1.0f, 0.0f, 1.0f, 1.0f,    //center
            1.0f, 0.0f, 1.0f, 1.0f,    // top
            1.0f, 0.0f, 1.0f, 1.0f,    // left top
            1.0f, 0.0f, 1.0f, 1.0f,   // left bottom
            1.0f, 0.0f, 1.0f, 1.0f,   // bottom
            1.0f, 0.0f, 1.0f, 1.0f,  // right bottom
            1.0f, 0.0f, 1.0f, 1.0f,   // right top


            //RAINBOW
            1.0f, 1.0f, 1.0f, 1.0f,    //center
            0.0f, 0.0f, 1.0f, 1.0f,    // top
            0.0f, 1.0f, 0.0f, 1.0f,    // left top
            1.0f, 0.0f, 0.0f, 1.0f,   // left bottom
            1.0f, 1.0f, 0.0f, 1.0f,   // bottom
            1.0f, 0.0f, 1.0f, 1.0f,  // right bottom
            0.0f, 1.0f, 1.0f, 1.0f,  // right top

            //Black
            0.0f, 0.0f, 0.0f, 1.0f,    //center
            0.0f, 0.0f, 0.0f, 1.0f,    // top
            0.0f, 0.0f, 0.0f, 1.0f,    // left top
            0.0f, 0.0f, 0.0f, 1.0f,   // left bottom
            0.0f, 0.0f, 0.0f, 1.0f,   // bottom
            0.0f, 0.0f, 0.0f, 1.0f,  // right bottom
            0.0f, 0.0f, 0.0f, 1.0f,  // right top
    };
    public static final FloatBuffer mHexagonColors = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(colors);

    public static void setColor(int color) {
        offset=color*mColorByteSize*Constants.bytesPerFloat;
        //mHexagonColors.position(color * mColorSize);
    }

}
