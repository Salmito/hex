package com.salmito.hex.programs.hex.entities;

import android.graphics.Color;

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

    private static final int mColorDataSize = 4;
    private static final int mColorSize = 7;
    private static final int mColorBytes = mColorDataSize * mColorSize * Constants.bytesPerFloat;
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
    private static final FloatBuffer mHexagonColors = ByteBuffer.allocateDirect(colors.length * Constants.bytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().put(colors);

    private static FloatBuffer currentColor = ByteBuffer.allocateDirect(mColorBytes)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();

    public static FloatBuffer getColor(int color) {
        mHexagonColors.position(color * mColorSize * mColorDataSize);
        return mHexagonColors;
    }

    public static FloatBuffer getAndroidColor(int color) {
        currentColor.rewind();
        for (int i = 0; i < mColorSize; i++) {
            currentColor.put(new float[]{
                    Color.red(color) / 255f, Color.green(color) / 255f, Color.blue(color) / 255f, Color.alpha(color) / 255f
            });
        }
        return currentColor;
    }

    public static FloatBuffer getColor(float r, float g, float b, float a) {
         currentColor.rewind();
        for (int i = 0; i < mColorSize; i++) {
            currentColor.put(r);
            currentColor.put(g);
            currentColor.put(b);
            currentColor.put(a);
        }
        currentColor.rewind();
        return currentColor;
    }


}
