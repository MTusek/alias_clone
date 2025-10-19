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
    private ArrayList<HashMap<String, Object>> teams;
    private int currentTeamIndex=0;
    private int currentPlayerIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_info);

        tvTurnInfo = findViewById(R.id.tvTurnInfo);
        btnStartRound = findViewById(R.id.btnStartRound);

        teams = (ArrayList<HashMap<String, Object>>) getIntent().getSerializableExtra("TEAMS");

       /* int lastTeamIndex = getIntent().getIntExtra("LAST_TEAM_INDEX", -1);
        if (lastTeamIndex != -1) {
            currentTeamIndex = (lastTeamIndex + 1) % teams.size();
        }
        */

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
        HashMap<String, Object> team = teams.get(currentTeamIndex);
        ArrayList<String> players = (ArrayList<String>) team.get("players");

        String teamName = (String) team.get("name");
        String playerName = players.get(currentPlayerIndex);

        tvTurnInfo.setText(teamName + " – " + playerName + " explains!");
    }
}
