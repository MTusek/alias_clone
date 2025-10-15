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
    private int index = 0;
    private int currentTeamIndex;

    private int roundDuration;

    private int scoreThisRound = 0;

    private ArrayList<HashMap<String, Object>> teams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);

        tvWord = findViewById(R.id.tvWord);
        tvTimer = findViewById(R.id.tvTimer);
        tvScore = findViewById(R.id.tvScore);
        btnCorrect = findViewById(R.id.btnCorrect);
        btnSkip = findViewById(R.id.btnSkip);

        teams = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        Serializable serializable = getIntent().getSerializableExtra("SETTINGS");
        HashMap<String, Object> settings = null;

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
            showNextWord();
        });

        btnSkip.setOnClickListener(v -> {
            scoreThisRound--;
            if (scoreThisRound <= 0) scoreThisRound = 0;
            tvScore.setText("Score: " + scoreThisRound);
            showNextWord();
        });

    }

    private void showNextWord() {
        tvWord.setText(words[index % words.length]);
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
                //btnNextWord.setEnabled(false);
                btnCorrect.setEnabled(false);
                btnSkip.setEnabled(false);

                //int pointsEarned = (int) (Math.random() * 5 + 1); // Temporary random points...

                Intent intent = new Intent(GameRoundActivity.this, RoundResultsActivity.class);
                intent.putExtra("TEAMS",(Serializable) teams);
                intent.putExtra("CURRENT_TEAM", getIntent().getIntExtra("CURRENT_TEAM", 0));
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
