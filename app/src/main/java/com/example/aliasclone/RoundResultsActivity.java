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
    private ArrayList<Team> teams;

    private ArrayList<String> usedWords;
    private ArrayList<Boolean> wordResults;
    private int currentTeamIndex;
    private int pointsEarned;

    private Settings settings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_results);

        tvRoundSummary = findViewById(R.id.tvRoundSummary);
        btnContinue = findViewById(R.id.btnContinue);
        wordListContainer = findViewById(R.id.wordListContainer);
        tvFinalScore = findViewById(R.id.tvFinalScore);

        teams = (ArrayList<Team>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        usedWords = getIntent().getStringArrayListExtra("USED_WORDS");
        wordResults = (ArrayList<Boolean>) getIntent().getSerializableExtra("WORD_RESULTS");
        settings = (Settings) getIntent().getSerializableExtra("SETTINGS");

        populateWordList();
        pointsEarned = calculateScore();
        updateTeamScore();
        Team currentTeam = teams.get(currentTeamIndex);
        currentTeam.advancePlayer();

        currentTeamIndex = (currentTeamIndex + 1) % teams.size();

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoreboardActivity.class);
            intent.putExtra("TEAMS", teams);
            intent.putExtra("CURRENT_TEAM", currentTeamIndex);
            intent.putExtra("SETTINGS", settings);
            startActivity(intent);
            finish();
        });
    }

    private void updateTeamScore() {
        Team team = teams.get(currentTeamIndex);
        int oldScore = team.getScore();
        team.addScore( oldScore+pointsEarned);
    }

    private int calculateScore() {
        int score = 0;
        if (settings.isNoNegative()) {
            for (int i = 0; i < wordListContainer.getChildCount(); i++) {
                CheckBox cb = (CheckBox) wordListContainer.getChildAt(i);
                if (cb.isChecked()) score++;
            }
        } else {
            for (int i = 0; i < wordListContainer.getChildCount(); i++) {
                CheckBox cb = (CheckBox) wordListContainer.getChildAt(i);
                if (cb.isChecked()) score++;
                if (!cb.isChecked()) score--;
            }
        }
        if(score<=0) score=0;
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
