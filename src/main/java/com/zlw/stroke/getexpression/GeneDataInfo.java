package com.zlw.stroke.getexpression;

import java.io.Serializable;

// Helper classes
public class GeneDataInfo implements Serializable {
    long offset;
    int nonZeroCount;

    GeneDataInfo(long offset, int nonZeroCount) {
        this.offset = offset;
        this.nonZeroCount = nonZeroCount;
    }

    @Override
    public String toString() {
        return "GeneDataInfo{offset=" + offset + ", nonZeroCount=" + nonZeroCount + "}";
    }
}