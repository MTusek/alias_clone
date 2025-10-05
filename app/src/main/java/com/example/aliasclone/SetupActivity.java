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

import com.example.aliasclone.GameActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private LinearLayout teamContainer;
    private Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        teamContainer = findViewById(R.id.teamContainer);

        addAddTeamButton();
        //addGameSettingsSection();
        ensureSettingsAtBottom();
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
                addStartButton();
                updateStartButtonState();
                ensureSettingsAtBottom();
            }
        });

        teamContainer.addView(btnAddTeam);
    }

    /*private void addStartButton() {

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
                HashMap<String, Object> settings = collectSettings();
                intent.putExtra("SETTINGS", settings);
                startActivity(intent);
            }
        });

        teamContainer.addView(btnStartGame);
    }*/

    private void ensureSettingsAtBottom() {
        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View child = teamContainer.getChildAt(i);
            if (child.getTag() != null && child.getTag().equals("settingsSection")) {
                teamContainer.removeView(child);
                break;
            }
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View settingsView = inflater.inflate(R.layout.item_game_settings, teamContainer, false);
        settingsView.setTag("settingsSection");

        teamContainer.addView(settingsView);
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
                    // sigurnosna provjera (ne bi se smjelo dogoditi ako UI radi)
                    btnStartGame.setEnabled(false);
                    return;
                }

                ArrayList<HashMap<String, Object>> allTeams = collectTeams();
                HashMap<String, Object> settings = collectSettings();

                Intent intent = new Intent(SetupActivity.this, GameActivity.class);
                intent.putExtra("TEAMS", allTeams);
                intent.putExtra("SETTINGS", settings);
                startActivity(intent);
            }
        });

        teamContainer.addView(btnStartGame);
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

        return validTeams >= 2; // treba barem 2 ispravna tima
    }
    private void updateStartButtonState() {
        if (btnStartGame != null) {
            btnStartGame.setEnabled(isGameReady());
        }
    }


    private void addGameSettingsSection() {
        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View child = teamContainer.getChildAt(i);
            if (child.getTag() != null && child.getTag().equals("settingsSection")) {
                teamContainer.removeView(child);
                break;
            }
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        View settingsView = inflater.inflate(R.layout.item_game_settings, teamContainer, false);
        settingsView.setTag("settingsSection");

        teamContainer.addView(settingsView);
    }


    private void addTeam() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View teamView = inflater.inflate(R.layout.item_team, teamContainer, false);

        Button btnAddPlayer = teamView.findViewById(R.id.btnAddPlayer);
        LinearLayout playerContainer = teamView.findViewById(R.id.playerContainer);
        Button btnDeleteTeam = teamView.findViewById(R.id.btnDeleteTeam);

        addPlayer(playerContainer);
        addPlayer(playerContainer);

        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayer(playerContainer);
                updateStartButtonState();
            }
        });

        btnDeleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamContainer.removeView(teamView);
                updateStartButtonState();
            }
        });

        teamContainer.addView(teamView);
    }

    private void addPlayer(LinearLayout playerContainer) {
        LinearLayout playerLayout = new LinearLayout(this);
        playerLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText et = new EditText(this);
        et.setHint("Player name");
        et.setLayoutParams(new LinearLayout.LayoutParams(0,
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

        playerLayout.addView(et);
        playerLayout.addView(btnDeletePlayer);

        playerContainer.addView(playerLayout);
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

    private HashMap<String, Object> collectSettings() {
        HashMap<String, Object> settings = new HashMap<>();

        View settingsView = null;
        for (int i = 0; i < teamContainer.getChildCount(); i++) {
            View child = teamContainer.getChildAt(i);
            if (child.getTag() != null && child.getTag().equals("settingsSection")) {
                settingsView = child;
                break;
            }
        }

        if (settingsView != null) {
            EditText etRoundTime = settingsView.findViewById(R.id.etRoundTime);
            EditText etMaxScore = settingsView.findViewById(R.id.etMaxScore);
            CheckBox cbNoNegative = settingsView.findViewById(R.id.cbNoNegative);
            CheckBox cbDoubleBonus = settingsView.findViewById(R.id.cbLastWord);
            CheckBox cbRandomTopic = settingsView.findViewById(R.id.cbRandomTopic);

            settings.put("roundTime", etRoundTime.getText().toString());
            settings.put("maxScore", etMaxScore.getText().toString());
            settings.put("noNegative", cbNoNegative.isChecked());
            settings.put("doubleBonus", cbDoubleBonus.isChecked());
            settings.put("randomTopic", cbRandomTopic.isChecked());
        }

        return settings;
    }

}
