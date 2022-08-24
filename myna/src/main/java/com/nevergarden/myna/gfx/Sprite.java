package com.nevergarden.myna.gfx;

import android.opengl.GLES20;
import android.util.Log;

import com.nevergarden.myna.core.Myna;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Sprite extends Quad {
    private final String vertexShader =
                    "uniform mat4 uModel;" + "uniform vec4 uColor;" +
                    "attribute vec3 aPosition;" + "attribute vec2 aTexCoord;" +
                    "varying vec2 vTexCoord;" + "varying vec4 vColor;" +
                    "void main() { " +
                            "gl_Position = uModel * vec4(aPosition,1.0);" +
                            "vTexCoord = aTexCoord;" +
                            "vColor = uColor;" +
                            "}";

    private final String fragmentShader =
            "precision highp float;" +
                    "uniform sampler2D uTex;" +
                    "varying vec2 vTexCoord;" + "varying vec4 vColor;" +
                    "void main() { gl_FragColor = texture2D(uTex, vTexCoord) * vColor; }";


    private FloatBuffer vertexBuffer;
    private FloatBuffer coordBuffer;
    private final Color spriteColor;
    private final float[] spriteCoords;

    private static GLProgram vprogram;
    private int positionHandler = 0;
    private int texCoordHandler = 0;
    private int colorHandler = 0;
    private int modelHandler = 0;
    private int texSamplerHandler = 0;

    private static int COORDS_PER_VERTEX = 5;
    private final int VERTEX_COUNT = 6;

    private final int VERTEX_STRIDE = 3 * 4;
    private final int COORD_STRIDE = 2 * 4;

    private final Texture texture;

    public Sprite(Texture texture, Color color) {
        super(color, texture.width, texture.height);
        Log.d(Myna.TAG, texture.width + " " + texture.height);
        this.texture = texture;
        this.spriteColor = color;

        this.spriteCoords = new float[] {
                texture.width, texture.height, 0, // top right
                0, texture.height, 0,// top left
                0, 0, 0,// bottom left
                0, 0, 0,// bottom left
                texture.width, texture.height, 0, // top right
                texture.width, 0, 0,// bottom right
        };
        float[] textureCoords = new float[] {
                1,1,
                0,1,
                0,0,
                0,0,
                1,1,
                1,0
        };
        /*
        this.spriteCoords = new float[] {
                texture.width, texture.height, 0f, // top right
                0, texture.height, 0.0f, // top left
                0, 0, 0.0f, // bottom left
                0, 0, 0.0f, // bottom left
                texture.width, texture.height, 0.0f, // top right
                texture.width, 0, 0.0f // bottom right
        };

         */

        if(vprogram == null) {
            vprogram = GLProgram.createProgramFromSource(this.vertexShader, this.fragmentShader);
        }
        ByteBuffer bb = ByteBuffer.allocateDirect(this.spriteCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(this.spriteCoords);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(textureCoords.length*4);
        bb.order(ByteOrder.nativeOrder());
        this.coordBuffer = bb.asFloatBuffer();
        coordBuffer.put(textureCoords);
        coordBuffer.position(0);
    }

    @Override
    public void draw() {
        vprogram.bind();
        positionHandler = GLES20.glGetAttribLocation(vprogram.nativeProgram, "aPosition");
        texCoordHandler = GLES20.glGetAttribLocation(vprogram.nativeProgram, "aTexCoord");
        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glEnableVertexAttribArray(texCoordHandler);

        this.vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(positionHandler, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        this.coordBuffer.position(0);
        GLES20.glVertexAttribPointer(texCoordHandler, 2, GLES20.GL_FLOAT, false, 0, coordBuffer);


        modelHandler = GLES20.glGetUniformLocation(vprogram.nativeProgram, "uModel");
        GLES20.glUniformMatrix4fv(modelHandler, 1, false, this.modelMatrix, 0);

        this.texture.bind();
        texSamplerHandler = GLES20.glGetUniformLocation(vprogram.nativeProgram, "uTex");
        GLES20.glUniform1i(texSamplerHandler, 0);

        colorHandler = GLES20.glGetUniformLocation(vprogram.nativeProgram, "uColor");
        Log.d(Myna.TAG, ""+colorHandler);
        GLES20.glUniform4fv(colorHandler, 1, this.spriteColor.getColorRGBAV(), 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, VERTEX_COUNT);
        GLES20.glDisableVertexAttribArray(positionHandler);
        GLES20.glDisableVertexAttribArray(texCoordHandler);
        vprogram.unbind();
    }
}
