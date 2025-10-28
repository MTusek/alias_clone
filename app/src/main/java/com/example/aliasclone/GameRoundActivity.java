package com.example.aliasclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameRoundActivity extends AppCompatActivity {

    private TextView tvWord, tvTimer, tvScore;
    private Button btnCorrect, btnSkip;

    private CountDownTimer timer;
    private String[] words = {"Apple", "Sunshine", "Guitar", "Mountain", "Tree", "Computer", "River", "Music", "Dog", "Sun", "Love", "Book", "Space"};
    private int index;
    private int currentTeamIndex;
    private int roundDuration;

    private ArrayList<String> usedWords = new ArrayList<>();
    private ArrayList<Boolean> wordResults = new ArrayList<>();

    private String currentWord;

    private int scoreThisRound = 0;

    private ArrayList<Team> teams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);

        tvWord = findViewById(R.id.tvWord);
        tvTimer = findViewById(R.id.tvTimer);
        tvScore = findViewById(R.id.tvScore);
        btnCorrect = findViewById(R.id.btnCorrect);
        btnSkip = findViewById(R.id.btnSkip);

        teams = (ArrayList<Team>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        HashMap<String, Object> settings = (HashMap<String, Object>) getIntent().getSerializableExtra("SETTINGS");


        if (settings != null && settings.containsKey("roundTime")) {
            Object rt = settings.get("roundTime");
            roundDuration = (Integer) rt;
        }else
            roundDuration = 5;

        showNextWord();
        startRoundTimer(roundDuration);

        btnCorrect.setOnClickListener(v -> {
            scoreThisRound++;
            tvScore.setText("Score: " + scoreThisRound);
            wordResults.add(true);
            usedWords.add(currentWord);
            showNextWord();
        });

        btnSkip.setOnClickListener(v -> {
            scoreThisRound--;
            if (scoreThisRound <= 0) scoreThisRound = 0;
            tvScore.setText("Score: " + scoreThisRound);
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
        timer = new CountDownTimer(seconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Time remaining: " + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                tvTimer.setText("Time's up!");
                btnCorrect.setEnabled(false);
                btnSkip.setEnabled(false);

                Intent intent = new Intent(GameRoundActivity.this, RoundResultsActivity.class);
                intent.putStringArrayListExtra("USED_WORDS",usedWords);
                intent.putExtra("WORD_RESULTS", (Serializable)wordResults);
                intent.putExtra("TEAMS",(Serializable) teams);
                intent.putExtra("CURRENT_TEAM", currentTeamIndex);
                intent.putExtra("POINTS_EARNED", scoreThisRound);
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
