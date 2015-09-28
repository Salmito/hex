package com.salmito.hex.engine;

/**
 * Created by tiago on 8/13/2015.
 */
public interface Thing {
    public void draw(long dt, Program program);
    public void clean();
}
