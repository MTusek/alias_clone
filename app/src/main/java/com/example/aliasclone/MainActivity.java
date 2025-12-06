package com.example.aliasclone;

import
        androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private View rules_overlay;
    private Button play_button, words_button, rules_button, exit_rules_button;
    private ImageButton btnLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_button = findViewById(R.id.button_play);
        words_button = findViewById(R.id.button_word_packs);
        rules_button = findViewById(R.id.button_rules);
        exit_rules_button = findViewById(R.id.button_rulesExit);
        rules_overlay = findViewById(R.id.rulesOverlay);
        btnLanguage = findViewById(R.id.btnLanguage);

        final String[] supportedLanguages = {"en", "hr", "de", "it", "fr"};

        final Map<String, Integer> flagMap = new HashMap<String, Integer>() {{
            put("en", R.drawable.flag_en);
            put("hr", R.drawable.flag_hr);
            put("de", R.drawable.flag_de);
            put("it", R.drawable.flag_it);
            put("fr", R.drawable.flag_fr);
        }};

        String currentLang = LocaleManager.getLanguage(this);

        btnLanguage.setImageResource(flagMap.get(currentLang));


        btnLanguage.setOnClickListener(v -> {
            String lang = LocaleManager.getLanguage(this);


            int index = 0;
            for (int i = 0; i < supportedLanguages.length; i++) {
                if (supportedLanguages[i].equals(lang)) {
                    index = i;
                    break;
                }
            }

            index = (index + 1) % supportedLanguages.length;
            String nextLang = supportedLanguages[index];


            LocaleManager.setNewLocale(this, nextLang);


            btnLanguage.setImageResource(flagMap.get(nextLang));

            recreate();
        });

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