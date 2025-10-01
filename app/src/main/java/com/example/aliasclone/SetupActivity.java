package com.example.aliasclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aliasclone.GameActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private LinearLayout teamContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        teamContainer = findViewById(R.id.teamContainer);

        // Dodaj odmah gumb na početku
        addAddTeamButton();
    }

    private void addAddTeamButton() {
        Button btnAddTeam = new Button(this);
        btnAddTeam.setText("+ Add team");

        btnAddTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamContainer.removeView(v);
                addTeam();
                addAddTeamButton();
                addStartButton();
            }
        });

        teamContainer.addView(btnAddTeam);
    }

    private void addStartButton() {

        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View child = teamContainer.getChildAt(i);
            if (child.getTag() != null && child.getTag().equals("startButton")) {
                teamContainer.removeView(child);
                break;
            }
        }

        Button btnStartGame = new Button(this);
        btnStartGame.setText("Start the game");
        btnStartGame.setTag("startButton");

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<HashMap<String, Object>> allTeams = collectTeams();
                Intent intent = new Intent(SetupActivity.this, GameActivity.class);
                intent.putExtra("TEAMS", allTeams);
                startActivity(intent);
            }
        });

        teamContainer.addView(btnStartGame);
    }

    private void addTeam() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View teamView = inflater.inflate(R.layout.item_team, teamContainer, false);

        Button btnAddPlayer = teamView.findViewById(R.id.btnAddPlayer);
        LinearLayout playerContainer = teamView.findViewById(R.id.playerContainer);

        addPlayer(playerContainer);

        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer(playerContainer);
            }
        });

        teamContainer.addView(teamView);
    }

    private void addPlayer(LinearLayout playerContainer) {
        EditText et = new EditText(this);
        et.setHint("Player name");
        playerContainer.addView(et);
    }

    private ArrayList<HashMap<String, Object>> collectTeams() {
        ArrayList<HashMap<String, Object>> teams = new ArrayList<>();

        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View teamView = teamContainer.getChildAt(i);

            EditText etTeamName = teamView.findViewById(R.id.etTeamName);
            LinearLayout playerContainer = teamView.findViewById(R.id.playerContainer);

            if (etTeamName != null && playerContainer != null) {
                String teamName = etTeamName.getText().toString();

                ArrayList<String> players = new ArrayList<>();
                for (int j = 0; j < playerContainer.getChildCount(); j++) {
                    View child = playerContainer.getChildAt(j);
                    if (child instanceof EditText) {
                        String playerName = ((EditText) child).getText().toString();
                        if (!playerName.isEmpty()) {
                            players.add(playerName);
                        }
                    }
                }

                HashMap<String, Object> teamData = new HashMap<>();
                teamData.put("teamName", teamName);
                teamData.put("players", players);
                teams.add(teamData);
            }
        }
        return teams;
    }
}
