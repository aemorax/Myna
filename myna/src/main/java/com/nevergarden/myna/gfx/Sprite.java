package com.nevergarden.myna.gfx;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sprite extends Quad {
    private static GLProgram program;
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer textureCoordinationBuffer;
    private final Color spriteColor;
    private final Texture texture;

    public Sprite(Texture texture, Color color) {
        super(color, texture.width, texture.height);
        this.texture = texture;
        this.spriteColor = color;

        // Counter clockwise rendering: TR->TL->BL / TR->BL->BR
        float[] spriteVertex = new float[]{
                texture.width, texture.height, 0,
                0, texture.height, 0,
                0, 0, 0,
                texture.width, texture.height, 0,
                0, 0, 0,
                texture.width, 0, 0
        };
        // Texture Coordinates for single sprite face
        float[] textureCoordination = new float[]{
                1, 1,
                0, 1,
                0, 0,
                1, 1,
                0, 0,
                1, 0
        };

        // Create shader program for sprite if it doesn't exist
        if (program == null) {
            String vertexShader = "uniform mat4 uModel;" + "uniform vec4 uColor;" +
                    "attribute vec3 aPosition;" + "attribute vec2 aTexCoordination;" +
                    "varying vec2 vTexCoordination;" + "varying vec4 vColor;" +
                    "void main() { " +
                    "gl_Position = uModel * vec4(aPosition,1.0);" +
                    "vTexCoordination = aTexCoordination;" +
                    "vColor = uColor;" +
                    "}";
            String fragmentShader = "precision mediump float;" +
                    "uniform sampler2D uTex;" +
                    "varying vec2 vTexCoordination;" + "varying vec4 vColor;" +
                    "void main() { gl_FragColor = texture2D(uTex, vTexCoordination) * vColor; }";
            program = GLProgram.createProgramFromSource(vertexShader, fragmentShader);
        }

        // Allocate byte buffers
        ByteBuffer bb = ByteBuffer.allocateDirect(spriteVertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.vertexBuffer = bb.asFloatBuffer();
        this.vertexBuffer.put(spriteVertex);
        this.vertexBuffer.position(0);
        bb = ByteBuffer.allocateDirect(textureCoordination.length * 4);
        bb.order(ByteOrder.nativeOrder());
        this.textureCoordinationBuffer = bb.asFloatBuffer();
        this.textureCoordinationBuffer.put(textureCoordination);
        this.textureCoordinationBuffer.position(0);
    }

    @Override
    public void draw(int frame) {
        // Setup Shader
        program.bind();
        int positionHandler = GLES20.glGetAttribLocation(program.nativeProgram, "aPosition");
        int texCoordinationHandler = GLES20.glGetAttribLocation(program.nativeProgram, "aTexCoordination");
        int modelHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uModel");
        int texSamplerHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uTex");
        int colorHandler = GLES20.glGetUniformLocation(program.nativeProgram, "uColor");

        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glEnableVertexAttribArray(texCoordinationHandler);

        // Set vertex and texture buffers
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(texCoordinationHandler, 2, GLES20.GL_FLOAT, false, 0, textureCoordinationBuffer);

        // Set transform matrix
        GLES20.glUniformMatrix4fv(modelHandler, 1, false, this.modelMatrix, 0);

        // Set texture sampler and color
        this.texture.bind();
        GLES20.glUniform1i(texSamplerHandler, 0);
        GLES20.glUniform4fv(colorHandler, 1, this.spriteColor.getColorRGBAV(), 0);

        // Draw array of 6 vertices from position 0
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

        // Clean Up
        this.texture.unbind();
        GLES20.glDisableVertexAttribArray(positionHandler);
        GLES20.glDisableVertexAttribArray(texCoordinationHandler);
    }
}