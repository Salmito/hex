package com.salmito.hex.main;

/**
 * Created by Tiago on 10/08/2015.
 */
public class Shaders {

    public final static String mainVertexShader =
            "uniform mat4 u_MVPMatrix;      \n"
                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
                    // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                    + "}                              \n";    // normalized screen coordinates.
    public final static String mainFragmentShader =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";


    public final static String hudVertexShader = "attribute vec3 position; void main() { gl_Position = vec4( position, 1.0 ); }";
    public final static String hudFragmentShader = "" +
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "\n" +
            "uniform float time;\n" +
            "uniform vec2 mouse;\n" +
            "uniform vec2 resolution;\n" +
            "\n" +
            "\n" +
            "void main(void)\n" +
            "{\n" +
            "\tvec2 uv = gl_FragCoord.xy / resolution.xy;   //normalise coordinates (1,1)\n" +
            "\n" +
            "\tuv.x -= 0.5; //center coordinates\n" +
            "\tuv.y -= 0.5; //center coordinates\n" +
            "\t//uv.y *= resolution.y/resolution.x; //correct the aspect ratio\n" +
            "\tuv *= 2.0; //scale  \n" +
            "\tfloat po = 2.0; // amount to power the lengths by\n" +
            "\tfloat px = pow(uv.x * uv.x, po); //squaring the values causes them to rise slower creating a square effect\n" +
            "\tfloat py = pow(uv.y * uv.y, po);\n" +
            "\tfloat a =   2.0* atan(uv.y , uv.x) + time/10.0 ; //this makes the checker board but I still don't get why it works with atan\n" +
            "\t//float a = 2.0; // uncomment to remove the checker board\n" +
            "\tfloat r = pow( px + py, 1.0/(2.0 * po) );  // convert the vector into a length (pythagoras duh)\n" +
            "\tvec2 q = vec2( 1.0/r + time * 0.25 , a ); //flip it so that the bands get wider towards the edge\n" +
            "\t\n" +
            "\tvec2 l = floor(q*4.6); //scale the values higher to make them into cycling integers\n" +
            "\tfloat c = mod(l.x+l.y, 2.0); // now get the modulo to return values between 0 and 1 (ish)\n" +
            "\tc *= pow(r,2.0); // darken everything towards the center\n" +
            "\n" +
            "\tgl_FragColor = vec4( c,c,c, 1.0 ); // set the pixel colour\n" +
            "\n" +
            "\n" +
            "}";

    public final static String secondVertexShader = "attribute vec3 position;\n" +
            "\t\t\tvoid main() {\n" +
            "\t\t\t\tgl_Position = vec4( position, 1.0 );\n" +
            "\n" +
            "\t\t\t}";

    public final static String secondFragmentShader = "#ifdef GL_ES\n" +
            "\t\t\tprecision highp float;\n" +
            "\t\t\t#endif\n" +
            "\n" +
            "\t\t\tuniform vec2 resolution;\n" +
            "\t\t\tuniform sampler2D texture;\n" +
            "\n" +
            "\t\t\tvoid main() {\n" +
            "\n" +
            "\t\t\t\tvec2 uv = gl_FragCoord.xy / resolution.xy;\n" +
            "\t\t\t\tgl_FragColor =  vec4( 1.0,0.0,0.0, 1.0 );//\ntexture2D( texture, uv );\n" +
            "\t\t\t}";
}
