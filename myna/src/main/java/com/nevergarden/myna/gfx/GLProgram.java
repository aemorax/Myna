package com.nevergarden.myna.gfx;

import android.opengl.GLES20;

public class GLProgram {
    private static int programInBind = 0;
    public final int nativeProgram;
    private boolean disposed = false;

    public GLProgram(int program) {
        this.nativeProgram = program;
    }

    public static GLProgram createProgramFromSource(String vertex, String fragment) {
        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(vertexShader, vertex);
        GLES20.glShaderSource(fragmentShader, fragment);

        GLES20.glCompileShader(vertexShader);
        GLES20.glCompileShader(fragmentShader);

        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        GLES20.glDeleteShader(vertexShader);
        GLES20.glDeleteShader(fragmentShader);

        return new GLProgram(program);
    }

    public Boolean bind() {
        if (this.disposed)
            return false;
        if (programInBind == this.nativeProgram)
            return false;
        GLES20.glUseProgram(this.nativeProgram);
        return true;
    }

    private void unbind() {
        GLES20.glUseProgram(0);
        programInBind = 0;
    }

    public void dispose() {
        if (programInBind == this.nativeProgram)
            this.unbind();
        GLES20.glDeleteProgram(this.nativeProgram);
        this.disposed = true;
    }
}