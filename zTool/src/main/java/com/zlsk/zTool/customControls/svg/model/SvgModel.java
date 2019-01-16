package com.zlsk.zTool.customControls.svg.model;

/**
 * Created by IceWang on 2018/9/12.
 */

public class SvgModel {
    private String[] glyphs;
    private int[] colors;
    private float width;
    private float height;

    public String[] getGlyphs() {
        return glyphs;
    }

    public void setGlyphs(String[] glyphs) {
        this.glyphs = glyphs;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public SvgModel(String[] glyphs, int[] colors, float width, float height) {
        this.glyphs = glyphs;
        this.colors = colors;
        this.width = width;
        this.height = height;
    }

    public SvgModel() {
    }
}
