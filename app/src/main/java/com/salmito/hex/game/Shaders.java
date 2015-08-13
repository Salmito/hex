package com.salmito.hex.game;

/**
 * Created by Tiago on 10/08/2015.
 */
public class Shaders {



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
