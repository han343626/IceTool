package com.zlsk.zTool.baiduMap.model;

import android.os.Bundle;
import android.view.View;

/**
 * Created by IceWang on 2018/11/30.
 */

public class MarkPoint {
    public static final String KEY_IN_BUNDLE = "key";

    private String key;
    private double x;
    private double y;
    private View view;
    private Bundle bundle;

    public MarkPoint(String key, double x, double y, View view, Bundle bundle) {
        this.key = key;
        this.x = x;
        this.y = y;
        this.view = view;
        this.bundle = bundle;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
