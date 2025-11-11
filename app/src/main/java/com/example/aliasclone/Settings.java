package com.example.aliasclone;

import java.io.Serializable;

public class Settings implements Serializable {
    private int roundTime;
    private int maxScore;
    private boolean noNegative;
    private boolean lastWord;
    private boolean randomTopic;

    public Settings(int roundTime, int maxScore, boolean noNegative, boolean lastWord, boolean randomTopic) {
        this.roundTime = roundTime;
        this.maxScore = maxScore;
        this.noNegative = noNegative;
        this.lastWord = lastWord;
        this.randomTopic = randomTopic;
    }
    public Settings() {
        this.roundTime = 60;
        this.maxScore = 10;
        this.noNegative = false;
        this.lastWord = false;
        this.randomTopic = false;
    }

    public int getRoundTime() { return roundTime; }
    public int getMaxScore() { return maxScore; }
    public boolean isNoNegative() { return noNegative; }
    public boolean isLastWordKept() { return lastWord; }
    public boolean isRandomTopic() { return randomTopic; }

    public void setRoundTime(int roundTime) { this.roundTime = roundTime; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public void setNoNegative(boolean noNegative) { this.noNegative = noNegative; }
    public void setLastWord(boolean lastWord) { this.lastWord = lastWord; }
    public void setRandomTopic(boolean randomTopic) { this.randomTopic = randomTopic; }
}
