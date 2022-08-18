package com.nevergarden.myna.gfx;

import android.opengl.GLES20;
import android.util.Log;

import com.nevergarden.myna.display.DisplayObject;
import com.nevergarden.myna.interfaces.IDrawable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class Quad extends DisplayObject implements IDrawable {
    private final String vertexShader =
            "attribute vec3 vPosition;" +
            "uniform mat4 uModel;" +
            "void main() { gl_Position = uModel * vec4(vPosition,1.0); }";

    private final String fragmentShader =
            "precision mediump float; uniform vec4 vColor; void main() { gl_FragColor = vColor; }";

    private static GLProgram program = null;

    private int positionHandler;

    private int modelHandler;

    private int colorHandler;
    private static int COORDS_PER_VERTEX = 3;
    private final int VERTEX_COUNT = 6;
    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private FloatBuffer vertexBuffer;
    private float[] quadCoords;
    private Color quadColor;

    public float[] modelMatrix;

    public Quad(Color color, float width, float height) {
        super();
        this.quadColor = color;
        this.quadCoords = new float[] {
                width, height, 0f, // top right
                0, height, 0.0f, // top left
                0, 0, 0.0f, // bottom left
                0, 0, 0.0f, // bottom left
                width, height, 0.0f, // top right
                width, 0, 0.0f // bottom right
        };

        if(program == null) {
            program = GLProgram.createProgramFromSource(vertexShader, fragmentShader);
        }
        ByteBuffer bb = ByteBuffer.allocateDirect(quadCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(quadCoords);
        vertexBuffer.position(0);
    }

    @Override
    public void recalculateMatrix() {
        super.recalculateMatrix();
        modelMatrix = new float[16];
        this.mainMatrix.get(this.modelMatrix);
        Log.d("Myna", "Quad:" + Arrays.toString(modelMatrix) + Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    @Override
    public void draw() {
        program.bind();
        positionHandler = GLES20.glGetAttribLocation(program.nativeProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glVertexAttribPointer(positionHandler, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer);

        modelHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uModel");

        GLES20.glUniformMatrix4fv(modelHandler, 1, false, this.modelMatrix, 0);

        colorHandler = GLES20.glGetUniformLocation(program.nativeProgram, "vColor");
        GLES20.glUniform4fv(colorHandler, 1, quadColor.getColorRGBAV(), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        GLES20.glDisableVertexAttribArray(positionHandler);
        program.unbind();
    }
}
