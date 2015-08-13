package com.salmito.hex.main;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.salmito.hex.game.GameState.getMap;
import static com.salmito.hex.game.GameState.getState;

public class MainRenderer implements GLSurfaceView.Renderer {

    public static final int mBytesPerFloat = 4;
    public static final int mBytesPerShort = 2;
    private int mainProgramHandle;
    private int hudProgramHandle;


    public static final float[] mViewMatrix = new float[16];
    public static final float[] mModelMatrix = new float[16];
    public static final float[] mMVPMatrix = new float[16];
    public static final float[] mProjectionMatrix = new float[16];
    public static final int[] mView = new int[4];

    public static int mMVPMatrixHandle;
    public static int mPositionHandle;
    public static int mColorHandle;
    public static int hudResolutionHandle;

    private Hud hud;

    //public static  HexMap map;
    public static float angle = 0.0f;
    private int hudPositionHandle;
    private int height;
    private int width;
    private int secondProgramHandle;

    public MainRenderer() {

        //map.getHexagon(4,4).setRotate(true);
        //map.getHexagon(3,3).setColor(HexColor.RED);
        //map.getHexagon(3,3).setRotate(true);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        long now = SystemClock.uptimeMillis();
        //System.out.println("NOW "+now/1000.f);
        hud.draw(width, height, now);


        //GLES20.glUseProgram(mainProgramHandle);
        //GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        //getMap().draw();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glUseProgram(mainProgramHandle);
        GLES20.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;
        System.out.println("Window Changed " + width + "x" + height);
        //TODO destroy
        hud.setFrontTarget(Target.createTarget(width, height));
        hud.setBackTarget(Target.createTarget(width, height));

        Globals globals = JsePlatform.standardGlobals();
        LuaValue chunk = globals.load(" print('hello, world',os.clock())");
        chunk.call();

        mView[0] = 0;
        mView[1] = 0;
        mView[2] = width;
        mView[3] = height;

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        //Matrix.frustumM(mProjectionMatrix, 0, 0, width, height, 0, 1, 10);
    }

    public static final int compileProgram(String vertex, String fragment) {
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (vertexShaderHandle != 0) {
            GLES20.glShaderSource(vertexShaderHandle, vertex);
            GLES20.glCompileShader(vertexShaderHandle);

            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }
        if (fragmentShaderHandle != 0) {
            GLES20.glShaderSource(fragmentShaderHandle, fragment);
            GLES20.glCompileShader(fragmentShaderHandle);

            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0 || fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating shaders: " + vertexShaderHandle + " " + fragmentShaderHandle);
        }

        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }
        return programHandle;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //GLES20.glClearColor(0.0f, 0.6f, 0.8f, 1.0f);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        getState().getCamera().lookAt();


        mainProgramHandle = compileProgram(Shaders.mainVertexShader, Shaders.mainFragmentShader);

        GLES20.glUseProgram(mainProgramHandle);

        //GLES20.glBindAttribLocation(mainProgramHandle, 0, "a_Position");
        //GLES20.glBindAttribLocation(mainProgramHandle, 1, "a_Color");

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mainProgramHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mainProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mainProgramHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.


        GLES20.glUseProgram(mainProgramHandle);

        hud = new Hud();

    }

}
