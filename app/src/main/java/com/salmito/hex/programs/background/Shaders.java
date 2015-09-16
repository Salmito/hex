package com.salmito.hex.programs.background;

/**
 * Created by Tiago on 10/08/2015.
 */
public class Shaders {



    public final static String hudVertexShader = "attribute vec3 position; void main() { gl_Position = vec4( position, 1.0 ); }";

    public final static String [] hudFragmentShaders = { "" +
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
            "}",

            //Second
            "#ifdef GL_ES\n" +
            "precision mediump float;\n" +
            "#endif\n" +
            "\n" +
            "uniform float time;\n" +
            "uniform vec2 mouse;\n" +
            "uniform vec2 resolution;\n" +
            "\n" +
            "#define PI 3.14159265359\n" +
            "\n" +
            "vec3 texPoint(vec2 v) {\n" +
            "\treturn vec3(mod(floor((v.x+0.02*sin(v.y+time*2.0))*1.0) + floor((v.y+time)*2.0), 2.0),\n" +
            "\t\t    mod(floor((v.x-0.02*sin(v.y+time*2.0))*10.0) + floor((v.y+time)*71.0), 2.0),\n" +
            "\t\t    mod(floor((v.x+0.02*cos(v.y+time*2.0))*1.0) + floor((v.y+time)*71.0), 7.0));\n" +
            "}\n" +
            "\n" +
            "void main( void ) {\n" +
            "\n" +
            "\tvec2 position = ( gl_FragCoord.xy / resolution.xy ) - vec2(0.5, 0.5);\n" +
            "\tposition.x *= resolution.x / resolution.y;\n" +
            "\tfloat dist = tan(mix(PI/2.2, PI/3.0, length(position)));\n" +
            "\tvec2 tex = vec2(mod(atan(position.x, position.y), 2.0*PI)/(1.0*PI), dist);\n" +
            "\n" +
            "\tvec3 color = texPoint(tex) / pow(dist, 0.1);\n" +
            "\n" +
            "\tgl_FragColor = vec4( color, 7.0 );\n" +
            "}"};
}
