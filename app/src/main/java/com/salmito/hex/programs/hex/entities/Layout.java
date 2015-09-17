package com.salmito.hex.programs.hex.entities;

import com.salmito.hex.engine.things.geometry.Point2f;

/**
 * Created by Tiago on 17/09/2015.
 */
public class Layout {
    private HexOrientation orientation;
    private Point2f size;
    private Point2f origin;

    public Layout(HexOrientation orientation_, Point2f size_, Point2f origin_) {
        this.size = size_;
        this.origin = origin_;
        this.orientation = orientation_;
    }

    public HexOrientation getOrientation() {
        return orientation;
    }

    public Point2f getSize() {
        return size;
    }

    public Point2f getOrigin() {
        return origin;
    }
}
