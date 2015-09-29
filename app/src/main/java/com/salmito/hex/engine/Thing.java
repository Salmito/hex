package com.salmito.hex.engine;

import com.salmito.hex.programs.mvp.CameraProgram;

/**
 * Created by tiago on 8/13/2015.
 */
public interface Thing {
    public void draw(long dt, CameraProgram program);
    public void clean();
}
