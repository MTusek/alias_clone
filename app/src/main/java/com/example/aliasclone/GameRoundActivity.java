package com.example.aliasclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameRoundActivity extends AppCompatActivity {

    private TextView tvWord, tvTimer;
    private Button btnCorrect, btnSkip;

    private Settings settings;

    private ImageView timerGif;

    private boolean keepLastWord;

    private AnimatedImageDrawable timerAnimation;

    private CountDownTimer timer;
    private String[] words;
    private int index;
    private int currentTeamIndex;

    private ArrayList<String> usedWords = new ArrayList<>();
    private ArrayList<Boolean> wordResults = new ArrayList<>();

    private String currentWord;

    private int scoreThisRound = 0;

    private ArrayList<Team> teams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            timerGif = findViewById(R.id.imageView);
            timerAnimation =  (AnimatedImageDrawable) getResources().getDrawable(R.drawable.game_timer, null);
            timerGif.setImageDrawable(timerAnimation);
        }

        tvWord = findViewById(R.id.tvWord);
        tvTimer = findViewById(R.id.tvTimer);
        btnCorrect = findViewById(R.id.btnCorrect);
        btnSkip = findViewById(R.id.btnSkip);

        settings = (Settings) getIntent().getSerializableExtra("SETTINGS");
        if (settings == null) {
            settings = new Settings();
        }

        Settings settings = (Settings) getIntent().getSerializableExtra("SETTINGS");


        if (settings != null) {
            switch (settings.getWordPack()) {
                case "Animals":
                    words = getResources().getStringArray(R.array.pack_animals);
                    break;
                case "Movies":
                    words = getResources().getStringArray(R.array.pack_movies);
                    break;
                case "Sports":
                    words = getResources().getStringArray(R.array.pack_sports);
                    break;
                case "Technology":
                    words = getResources().getStringArray(R.array.pack_technology);
                    break;
                default:
                    words = getResources().getStringArray(R.array.pack_default);
            }
        } else {
            words = getResources().getStringArray(R.array.pack_default);
        }


        keepLastWord = settings != null && settings.isLastWordKept();


        int roundDuration = settings.getRoundTime();

        teams = (ArrayList<Team>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);


        showNextWord();
        startRoundTimer(roundDuration+1);

        btnCorrect.setOnClickListener(v -> {
            wordResults.add(true);
            usedWords.add(currentWord);
            showNextWord();
        });

        btnSkip.setOnClickListener(v -> {
            if (scoreThisRound <= 0) scoreThisRound = 0;
            wordResults.add(false);
            usedWords.add(currentWord);
            showNextWord();
        });

    }

    private void showNextWord() {
        tvWord.setText(words[index % words.length]);
        currentWord=tvWord.getText().toString();
        index++;
    }

    private void startRoundTimer(int seconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && timerAnimation != null)
            timerAnimation.start();
        timer = new CountDownTimer(seconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText(""+millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && timerAnimation != null)
                    timerAnimation.stop();

                tvTimer.setText(R.string.time_up);
                btnCorrect.setEnabled(false);
                btnSkip.setEnabled(false);

                if (keepLastWord && !usedWords.contains(currentWord)) {
                    usedWords.add(currentWord);
                    wordResults.add(false);
                }

                if(usedWords.isEmpty()){
                    usedWords.add(currentWord);
                    wordResults.add(false);
                }


                Intent intent = new Intent(GameRoundActivity.this, RoundResultsActivity.class);
                intent.putStringArrayListExtra("USED_WORDS",usedWords);
                intent.putExtra("WORD_RESULTS", wordResults);
                intent.putExtra("TEAMS", teams);
                intent.putExtra("CURRENT_TEAM", currentTeamIndex);
                intent.putExtra("POINTS_EARNED", scoreThisRound);
                intent.putExtra("SETTINGS", settings);
                startActivity(intent);
                finish();
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
