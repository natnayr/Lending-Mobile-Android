package com.crowdo.p2pmobile.helper;

/**
 * Created by cwdsg05 on 8/12/16.
 */

public class AmountRounder {

    public static Long roundUpToNearestUnit(long value, long unit){
        return ((value + (unit-1)) / unit) * unit;
    }
}
