package com.nevergarden.myna.display;

import android.opengl.GLES20;

import org.joml.Matrix4f;

public class View {
    public float[] matrix;

    private Matrix4f mMainMatrix;
    private int viewHandler = 0;

    public View() {
        this.matrix = new float[16];
        this.mMainMatrix = new Matrix4f();
        this.mMainMatrix.identity();
        this.convert();
    }

    public void bind(int program) {
        this.viewHandler = GLES20.glGetUniformLocation(program, "uView");
        GLES20.glUniformMatrix4fv(this.viewHandler, 1, false, this.matrix, 0);
    }

    private void convert() {
        this.mMainMatrix.get(this.matrix);
    }
}
