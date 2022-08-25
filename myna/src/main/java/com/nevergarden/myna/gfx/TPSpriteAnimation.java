package com.nevergarden.myna.gfx;

import android.opengl.GLES20;

import com.nevergarden.myna.ds.texturepacker.TPAtlas;
import com.nevergarden.myna.ds.texturepacker.TPFrame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class TPSpriteAnimation extends Quad {
    public final int frameCount;
    public int currentFrame = 0;
    private static GLProgram program = null;

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer textureCoordinationBuffer;

    private final TPAtlas atlas;
    public TPSpriteAnimation(TPAtlas atlas, Color color, int width, int height) {
        super(color, width, height);
        this.atlas = atlas;
        this.frameCount = atlas.atlasInfo.frames.length;

        // TR->TL->BL->BR->TR
        float[] vertexPoints = new float[] {
                width, height, 0,
                0, height, 0,
                0, 0, 0,
                width, 0, 0,
                width, height, 0,
        };

        /*

        float xmin = 0;
        float ymin = 0;
        float xmax = 1;
        float ymax = 1;

        float[] androidPoints = new float[] {
                xmax,ymax,
                xmin,ymax,
                xmin,ymin,
                xmax,ymin,
                xmax,ymax
        };
         */

        int texCoordinationCount = atlas.atlasInfo.frames.length * 10;
        float[] texCoordinationPoints = new float[texCoordinationCount];
        for (int i = 0; i < atlas.atlasInfo.frames.length; i++) {
            TPFrame frame = atlas.atlasInfo.frames[i];
            float xmin = frame.frame.x/(float)atlas.atlasInfo.meta.size.w;
            float xmax = (frame.frame.x + frame.frame.w)/(float)atlas.atlasInfo.meta.size.w;
            float ymin = frame.frame.y/(float)atlas.atlasInfo.meta.size.h;
            float ymax = (frame.frame.y + frame.frame.h)/(float)atlas.atlasInfo.meta.size.h;
            texCoordinationPoints[10*i] = xmax;
            texCoordinationPoints[10*i+1] = ymax;
            texCoordinationPoints[10*i+2] = xmin;
            texCoordinationPoints[10*i+3] = ymax;
            texCoordinationPoints[10*i+4] = xmin;
            texCoordinationPoints[10*i+5] = ymin;
            texCoordinationPoints[10*i+6] = xmax;
            texCoordinationPoints[10*i+7] = ymin;
            texCoordinationPoints[10*i+8] = xmax;
            texCoordinationPoints[10*i+9] = ymax;
        }

        if(program == null) {
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
        ByteBuffer bb = ByteBuffer.allocateDirect(vertexPoints.length*4);
        bb.order(ByteOrder.nativeOrder());
        this.vertexBuffer = bb.asFloatBuffer();
        this.vertexBuffer.put(vertexPoints);
        this.vertexBuffer.position(0);
        bb = ByteBuffer.allocateDirect(texCoordinationPoints.length*4);
        bb.order(ByteOrder.nativeOrder());
        this.textureCoordinationBuffer = bb.asFloatBuffer();
        this.textureCoordinationBuffer.put(texCoordinationPoints);
        this.textureCoordinationBuffer.position(0);
    }

    @Override
    public void draw() {
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
        textureCoordinationBuffer.position((currentFrame%frameCount)*10);
        currentFrame++;
        GLES20.glVertexAttribPointer(texCoordinationHandler, 2, GLES20.GL_FLOAT, false, 0, textureCoordinationBuffer);

        // Set transform matrix
        GLES20.glUniformMatrix4fv(modelHandler, 1, false, this.modelMatrix, 0);

        // Set texture sampler and color
        this.atlas.texture.bind();
        GLES20.glUniform1i(texSamplerHandler, 0);
        GLES20.glUniform4fv(colorHandler, 1, this.color.getColorRGBAV(), 0);

        // Draw array of 6 vertices from position 0
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 5);

        // Clean Up
        this.atlas.texture.unbind();
        GLES20.glDisableVertexAttribArray(positionHandler);
        GLES20.glDisableVertexAttribArray(texCoordinationHandler);
        program.unbind();
    }
}
