package com.example.aliasclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ScoreboardActivity extends AppCompatActivity {

    private LinearLayout scoreContainer;
    private ArrayList<Team> teams;
    private Button btnNextRound;

    private int currentTeamIndex;

    @Override
    protected void onResume() {
        super.onResume();
        if (teams != null) {
            Collections.sort(teams, (a, b) ->
                    Integer.compare((b.getScore()),
                            (a.getScore())));
            displayScores();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreContainer = findViewById(R.id.scoreContainer);
        btnNextRound = findViewById(R.id.btnNextRound);

        teams = (ArrayList<Team>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        Settings settings = (Settings) getIntent().getSerializableExtra("SETTINGS");

        Collections.sort(teams, (a, b) -> {
            int scoreA = (int) a.getScore();
            int scoreB = (int) b.getScore();
            return Integer.compare(scoreB, scoreA);
        });

        displayScores();

        btnNextRound.setOnClickListener(v -> {
            Intent intent = new Intent(this, TurnInfoActivity.class);
            int nextTeamIndex = (currentTeamIndex + 1) % teams.size();
            intent.putExtra("TEAMS", teams);
            intent.putExtra("CURRENT_TEAM", nextTeamIndex);
            intent.putExtra("SETTINGS", settings);
            startActivity(intent);
        });
    }

    private void displayScores() {
        scoreContainer.removeAllViews();
        for (Team team : teams) {
            String name = team.getName();
            int score = (int) team.getScore();

            TextView tv = new TextView(this);
            tv.setText(name + ": " + score + " pts");
            tv.setTextSize(18);
            scoreContainer.addView(tv);
        }
    }
}
