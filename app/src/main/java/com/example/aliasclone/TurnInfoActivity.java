package com.example.aliasclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnInfoActivity extends AppCompatActivity {

    private TextView tvTurnInfo;
    private Button btnStartRound;
    private ArrayList<Team> teams;
    private int currentTeamIndex;
    private int currentPlayerIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_info);

        tvTurnInfo = findViewById(R.id.tvTurnInfo);
        btnStartRound = findViewById(R.id.btnStartRound);

        teams = (ArrayList<Team>) getIntent().getSerializableExtra("TEAMS");
        currentTeamIndex = getIntent().getIntExtra("CURRENT_TEAM", 0);
        showCurrentTurn();

        btnStartRound.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameRoundActivity.class);
            intent.putExtra("TEAMS", teams);
            intent.putExtra("CURRENT_TEAM", currentTeamIndex);
            intent.putExtra("CURRENT_PLAYER", currentPlayerIndex);
            startActivity(intent);
        });
    }

    private void showCurrentTurn() {
        if (teams.isEmpty()) return;
       Team team = teams.get(currentTeamIndex);
        ArrayList<String> players = team.getPlayers();
        int currentPlayerIndex = team.getCurrentPlayerIndex();

        String teamName =  team.getName();
        String playerName = players.get(currentPlayerIndex);

        tvTurnInfo.setText(teamName + " – " + playerName + " explains!");
    }
}
