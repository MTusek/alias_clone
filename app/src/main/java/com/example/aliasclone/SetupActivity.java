package com.example.aliasclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private LinearLayout teamContainer;
    private LinearLayout bottomContainer;

    private Button btnStartGame;

    private int teamCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        teamContainer = findViewById(R.id.teamContainer);
        bottomContainer = findViewById(R.id.bottomContainer);

        addAddTeamButton();
        addGameSettingsSection();
        addStartButton();
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
                updateStartButtonState();
            }
        });

        teamContainer.addView(btnAddTeam);
    }

    private void addStartButton() {
        if (btnStartGame != null) {
            teamContainer.removeView(btnStartGame);
        }

        btnStartGame = new Button(this);
        btnStartGame.setText("START THE MATCH");
        btnStartGame.setEnabled(false);

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGameReady()) {
                    btnStartGame.setEnabled(false);
                    return;
                }

                ArrayList<Team> allTeams = collectTeams();
                Settings settings = collectSettings();

                Intent intent = new Intent(SetupActivity.this, TurnInfoActivity.class);
                intent.putExtra("TEAMS", allTeams);
                intent.putExtra("SETTINGS",  settings);
                startActivity(intent);
            }
        });
        bottomContainer.addView(btnStartGame);
    }

    private void addTeam() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View teamView = inflater.inflate(R.layout.item_team, teamContainer, false);

        teamCount++;
        String defaultTeamName = "Team " + teamCount;

        EditText etTeamName = teamView.findViewById(R.id.etTeamName);
        LinearLayout playerContainer = teamView.findViewById(R.id.playerContainer);
        Button btnAddPlayer = teamView.findViewById(R.id.btnAddPlayer);
        Button btnDeleteTeam = teamView.findViewById(R.id.btnDeleteTeam);

        etTeamName.setText(defaultTeamName);
        addPlayer(playerContainer, 1);
        addPlayer(playerContainer, 2);

        btnAddPlayer.setOnClickListener(v -> {
            int playerCount = playerContainer.getChildCount();
            addPlayer(playerContainer, playerCount + 1);
            updateStartButtonState();
        });

        btnDeleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamContainer.removeView(teamView);
                updateStartButtonState();
            }
        });

        teamContainer.addView(teamView, teamContainer.getChildCount() - 1);
        updateStartButtonState();
    }


    private boolean isGameReady() {
        int validTeams = 0;

        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View teamView = teamContainer.getChildAt(i);
            EditText etTeamName = teamView.findViewById(R.id.etTeamName);
            LinearLayout playerContainer = teamView.findViewById(R.id.playerContainer);

            if (etTeamName != null && playerContainer != null) {
                String teamName = etTeamName.getText().toString().trim();
                int playerCount = 0;

                for (int j = 0; j < playerContainer.getChildCount(); j++) {
                    View playerLayout = playerContainer.getChildAt(j);
                    if (playerLayout instanceof LinearLayout) {
                        EditText etPlayer = (EditText) ((LinearLayout) playerLayout).getChildAt(0);
                        String playerName = etPlayer.getText().toString().trim();
                        if (!playerName.isEmpty()) {
                            playerCount++;
                        }
                    }
                }

                // uvjet: tim mora imati ime i barem 2 igrača
                if (!teamName.isEmpty() && playerCount >= 2) {
                    validTeams++;
                }
            }
        }

        return validTeams >= 2;
    }
    private void updateStartButtonState() {
        if (btnStartGame != null) {
            btnStartGame.setEnabled(isGameReady());
        }
    }


        private void addGameSettingsSection() {
            LayoutInflater inflater = LayoutInflater.from(this);
            View settingsView = inflater.inflate(R.layout.item_game_settings, bottomContainer, false);
            settingsView.setTag("settingsSection");

            bottomContainer.addView(settingsView, 0);
        }

    private void addPlayer(LinearLayout playerContainer, int playerNumber) {
        LinearLayout playerLayout = new LinearLayout(this);
        playerLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText etPlayerName = new EditText(this);
        etPlayerName.setHint("Player name");
        etPlayerName.setText("Player"+playerNumber);
        etPlayerName.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button btnDeletePlayer = new Button(this);
        btnDeletePlayer.setText("X");

        btnDeletePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerContainer.removeView(playerLayout);
                updateStartButtonState();
            }
        });

        playerLayout.addView(etPlayerName);
        playerLayout.addView(btnDeletePlayer);

        playerContainer.addView(playerLayout);
    }

    private ArrayList<Team> collectTeams() {
        ArrayList<Team> teams = new ArrayList<>();

        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View teamView = teamContainer.getChildAt(i);

            EditText etTeamName = teamView.findViewById(R.id.etTeamName);
            if (etTeamName == null) continue;

            LinearLayout playerContainer = teamView.findViewById(R.id.playerContainer);

            String teamName = etTeamName.getText().toString().trim();
            if (teamName.isEmpty()) teamName = "Team " + (i + 1);

            ArrayList<String> players = new ArrayList<>();

            for (int j = 0; j < playerContainer.getChildCount(); j++) {
                View playerView = playerContainer.getChildAt(j);

                if (playerView instanceof LinearLayout) {
                    LinearLayout playerLayout = (LinearLayout) playerView;

                    for (int k = 0; k < playerLayout.getChildCount(); k++) {
                        View child = playerLayout.getChildAt(k);
                        if (child instanceof EditText) {
                            EditText etPlayer = (EditText) child;
                            String name = etPlayer.getText().toString().trim();
                            if (name.isEmpty()) name = "Player " + (j + 1);
                            players.add(name);
                            break;
                        }
                    }
                }
            }

            Team team = new Team(teamName, players);
            teams.add(team);
        }
        return teams;
    }


    private Settings collectSettings() {
        View settingsView = null;
        for (int i = 0; i < bottomContainer.getChildCount(); i++) {
            View child = bottomContainer.getChildAt(i);
            if (child.getTag() != null && child.getTag().equals("settingsSection")) {
                settingsView = child;
                break;
            }
        }

        if (settingsView == null) {
            return new Settings();
        }

        EditText etRoundTime = settingsView.findViewById(R.id.etRoundTime);
        EditText etMaxScore = settingsView.findViewById(R.id.etMaxScore);
        CheckBox cbNoNegative = settingsView.findViewById(R.id.cbNoNegative);
        CheckBox cbLastWord = settingsView.findViewById(R.id.cbLastWord);
        CheckBox cbRandomTopic = settingsView.findViewById(R.id.cbRandomTopic);

        int roundTime = 60;
        int maxScore = 10;

        try {
            String roundTimeStr = etRoundTime.getText().toString().trim();
            if (!roundTimeStr.isEmpty()) roundTime = Integer.parseInt(roundTimeStr);
        } catch (NumberFormatException ignored) {}

        try {
            String maxScoreStr = etMaxScore.getText().toString().trim();
            if (!maxScoreStr.isEmpty()) maxScore = Integer.parseInt(maxScoreStr);
        } catch (NumberFormatException ignored) {}

        boolean noNegative = cbNoNegative.isChecked();
        boolean lastWord = cbLastWord.isChecked();
        boolean randomTopic = cbRandomTopic.isChecked();

        return new Settings(roundTime, maxScore, noNegative, lastWord, randomTopic);
    }


}
