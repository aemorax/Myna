package com.nevergarden.myna.gfx;

import java.util.Random;

public class Color {
    public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
    private int r = 0;
    private int g = 0;
    private int b = 0;
    private int a = 0;

    private float fr = 0.0f;
    private float fg = 0.0f;
    private float fb = 0.0f;
    private float fa = 0.0f;

    public Color(float r, float g, float b, float a) {
        setFRed(r);
        setFGreen(g);
        setFBlue(b);
        setFAlpha(a);
    }

    public Color(int r, int g, int b, int a) {
        setRed(r);
        setGreen(g);
        setBlue(b);
        setAlpha(a);
    }

    public static Color random() {
        Random r = new Random();
        return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 255);
    }

    public int getColorRGBA() {
        return (r << 24) | (g << 16) | (b << 8) | a;
    }

    public int getColorARGB() {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public int getColorRGB() {
        return (r << 16) | (g << 8) | b;
    }

    public float[] getColorRGBAV() {
        return new float[]{fr, fg, fb, fa};
    }

    public int getRed() {
        return this.r;
    }

    public void setRed(int r) {
        this.r = r;
        this.fr = (float) r / 255.0f;
    }

    public int getGreen() {
        return this.g;
    }

    public void setGreen(int g) {
        this.g = g;
        this.fg = (float) g / 255.0f;
    }

    public int getBlue() {
        return this.b;
    }

    public void setBlue(int b) {
        this.b = b;
        this.fb = (float) b / 255.0f;
    }

    public int getAlpha() {
        return this.a;
    }

    public void setAlpha(int a) {
        this.a = a;
        this.fa = (float) a / 255.0f;
    }

    public float getFRed() {
        return this.fr;
    }

    public void setFRed(float fr) {
        this.fr = fr;
        this.r = (int) (fr * 255);
    }

    public float getFGreen() {
        return this.fg;
    }

    public void setFGreen(float fg) {
        this.fg = fg;
        this.g = (int) (this.fg * 255);
    }

    public float getFBlue() {
        return this.fb;
    }

    public void setFBlue(float fb) {
        this.fb = fb;
        this.b = (int) (fb * 255);
    }

    public float getFAlpha() {
        return this.fa;
    }

    public void setFAlpha(float fa) {
        this.fa = fa;
        this.a = (int) (a * 255);
    }
}
