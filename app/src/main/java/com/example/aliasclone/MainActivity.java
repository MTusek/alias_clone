package com.example.aliasclone;

import
        androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private View rules_overlay;
    private Button play_button, words_button, rules_button, exit_rules_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_button = findViewById(R.id.button_play);
        words_button = findViewById(R.id.button_word_packs);
        rules_button = findViewById(R.id.button_rules);
        exit_rules_button = findViewById(R.id.button_rulesExit);
        rules_overlay = findViewById(R.id.rulesOverlay);

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
            }
        });
        rules_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rules_overlay.setVisibility(View.VISIBLE);
            }
        });
        exit_rules_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rules_overlay.setVisibility(View.GONE);
            }
        });
    }
}