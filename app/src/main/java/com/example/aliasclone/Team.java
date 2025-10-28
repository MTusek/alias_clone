package com.example.aliasclone;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements Serializable {
    private String name;
    private ArrayList<String> players;
    private int score;
    private int currentPlayerIndex;

    public Team(String name, ArrayList<String> players) {
        this.name = name;
        this.players = players;
        this.score = 0;
        this.currentPlayerIndex = 0;
    }

    public String getName() { return name; }
    public ArrayList<String> getPlayers() { return players; }
    public int getScore() { return score; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }

    public void addScore(int points) { score += points; }

    public void advancePlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
}
