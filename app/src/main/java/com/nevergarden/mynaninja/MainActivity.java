package com.nevergarden.mynaninja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nevergarden.myna.R;
import com.nevergarden.myna.gfx.Color;
import com.nevergarden.myna.gfx.Sprite;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random r = new Random();
        MynaNinjaGame game = findViewById(R.id.game);

        FloatingActionButton fab = findViewById(R.id.eventDispatcher);
        fab.setOnClickListener(v -> {
            Sprite s = new Sprite(game.assetManager.loadTexture(R.drawable.pngegg), new Color(1f,1f,1f,1f));
            s.setPosition(r.nextInt(game.getWidth()-200), r.nextInt(game.getHeight()-200));
            game.currentStage.addChild(s);
        });
    }
}