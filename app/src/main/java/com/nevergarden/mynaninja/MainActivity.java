package com.nevergarden.mynaninja;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nevergarden.mynaninja.R;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Random r = new Random();
        MynaNinjaGame game = findViewById(R.id.game);
    }
}