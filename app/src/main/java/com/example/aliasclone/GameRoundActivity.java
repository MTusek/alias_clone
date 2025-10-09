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

    private TextView tvWord, tvTimer;
    private Button btnNextWord;
    private CountDownTimer timer;
    private String[] words = {"Apple", "Car", "Sunshine", "Guitar", "Mountain"};
    private int index = 0;

    private ArrayList<HashMap<String, Object>> teams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_round);

        tvWord = findViewById(R.id.tvWord);
        tvTimer = findViewById(R.id.tvTimer);
        btnNextWord = findViewById(R.id.btnNextWord);

        showNextWord();
        startRoundTimer(10);

        btnNextWord.setOnClickListener(v -> showNextWord());

        teams = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("TEAMS");
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
                btnNextWord.setEnabled(false);

                int pointsEarned = (int) (Math.random() * 5 + 1); // Temporary random points...

                Intent intent = new Intent(GameRoundActivity.this, RoundResultsActivity.class);
                intent.putExtra("TEAMS",(Serializable) teams);
                intent.putExtra("CURRENT_TEAM", getIntent().getIntExtra("CURRENT_TEAM", 0));
                intent.putExtra("POINTS_EARNED", pointsEarned);
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
