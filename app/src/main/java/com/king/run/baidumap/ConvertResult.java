package com.king.run.baidumap;

/**
 * Created by weiguo.ren on 2014/9/10.
 */
public class ConvertResult {
    private int error;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }


    private String x;
    private String y;
}
