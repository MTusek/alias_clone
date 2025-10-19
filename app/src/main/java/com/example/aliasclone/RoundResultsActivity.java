package com.example.aliasclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RoundResultsActivity extends AppCompatActivity {

    private TextView tvRoundSummary, tvFinalScore;
    private Button btnContinue;

    private LinearLayout wordListContainer;
    private ArrayList<HashMap<String, Object>> teams;

    private ArrayList<String> usedWords;
    private ArrayList<Boolean> wordResults;
    private int currentTeamIndex;
    private int pointsEarned;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_results);

        tvRoundSummary = findViewById(R.id.tvRoundSummary);
        btnContinue = findViewById(R.id.btnContinue);
        wordListContainer = findViewById(R.id.wordListContainer);
        tvFinalScore = findViewById(R.id.tvFinalScore);

        teams = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        usedWords = getIntent().getStringArrayListExtra("USED_WORDS");
        wordResults = (ArrayList<Boolean>) getIntent().getSerializableExtra("WORD_RESULTS");

        populateWordList();
        pointsEarned = calculateScore();
        updateTeamScore();

        currentTeamIndex++;
        if(currentTeamIndex>teams.size())
            currentTeamIndex=0;

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoreboardActivity.class);
            intent.putExtra("TEAMS", (Serializable) teams);
            intent.putExtra("CURRENT_TEAM", currentTeamIndex);
            startActivity(intent);
            finish();
        });
    }

    private void updateTeamScore() {
        HashMap<String, Object> team = teams.get(currentTeamIndex);
        int oldScore = (int) team.getOrDefault("score", 0);
        team.put("score", oldScore+pointsEarned);
    }

    private int calculateScore(){
        int score = 0;
        for (int i = 0; i < wordListContainer.getChildCount(); i++) {
            CheckBox cb = (CheckBox) wordListContainer.getChildAt(i);
            if (cb.isChecked()) score++;
        }
        return score;
    }

    private void populateWordList() {
        if (usedWords == null || wordResults == null) return;

        for (int i = 0; i < usedWords.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(usedWords.get(i));
            checkBox.setChecked(wordResults.get(i)); // prefill from gameplay
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tvFinalScore.setText("Final score: " + calculateScore());
            });
            wordListContainer.addView(checkBox);
        }
        tvFinalScore.setText("Final score: " + calculateScore());
    }
}
