package com.example.aliasclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RoundResultsActivity extends AppCompatActivity {

    private TextView tvRoundSummary;
    private Button btnContinue;
    private ArrayList<HashMap<String, Object>> teams;
    private int currentTeamIndex;
    private int pointsEarned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_results);

        tvRoundSummary = findViewById(R.id.tvRoundSummary);
        btnContinue = findViewById(R.id.btnContinue);

        teams = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        pointsEarned = getIntent().getIntExtra("POINTS_EARNED", 0);

        updateTeamScore();
        showSummary();

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScoreboardActivity.class);
            intent.putExtra("TEAMS", (Serializable) teams);
            startActivity(intent);
            finish();
        });
    }

    private void updateTeamScore() {
        HashMap<String, Object> team = teams.get(currentTeamIndex);
        int oldScore = (int) team.getOrDefault("score", 0);
        team.put("score", oldScore + pointsEarned);
    }

    private void showSummary() {
        HashMap<String, Object> team = teams.get(currentTeamIndex);
        String name = (String) team.get("name");

        tvRoundSummary.setText(name + " earned " + pointsEarned + " points!");
    }
}
