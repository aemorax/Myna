package com.nevergarden.myna.gfx;

import android.opengl.GLES20;

import com.nevergarden.myna.interfaces.IDrawable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Quad implements IDrawable {
    private final String vertexShader =
            "attribute vec3 vPosition;" +
            "uniform mat4 uModel;" +
            "uniform mat4 uView;" +
            "uniform mat4 uProjection;" +
            "void main() { gl_Position = uProjection * uView * uModel * vec4(vPosition,1.0); }";

    private final String fragmentShader =
            "precision mediump float; uniform vec4 vColor; void main() { gl_FragColor = vColor; }";

    private static GLProgram program;

    private int positionHandler;

    private int modelHandler;
    private int projectionHandler;

    private int colorHandler;
    private static int COORDS_PER_VERTEX = 3;
    private final int VERTEX_COUNT = 6;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private FloatBuffer vertexBuffer;
    private float[] quadCoords;
    private float[] quadColor;

    private final float[] tempIdentity = new float[] {
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1
    };

    public Quad(float[] color, float[] coords) {
        this.quadColor = color;
        this.quadCoords = coords;

        program = GLProgram.createProgramFromSource(vertexShader, fragmentShader);
        ByteBuffer bb = ByteBuffer.allocateDirect(quadCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(quadCoords);
        vertexBuffer.position(0);
    }

    @Override
    public void draw() {
        program.bind();
        positionHandler = GLES20.glGetAttribLocation(program.nativeProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        Renderer.view.bind(program.nativeProgram);

        modelHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uModel");
        projectionHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uProjection");

        GLES20.glUniformMatrix4fv(modelHandler, 1, false, tempIdentity, 0);
        GLES20.glUniformMatrix4fv(projectionHandler, 1, false, Renderer.PROJECTION, 0);

        colorHandler = GLES20.glGetUniformLocation(program.nativeProgram, "vColor");
        GLES20.glUniform4fv(colorHandler, 1, quadColor, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        GLES20.glDisableVertexAttribArray(positionHandler);
        program.unbind();
    }
}
