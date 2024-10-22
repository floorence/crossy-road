package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

//A list of high scores with a set length of 5 (lower scores are removed as higher scores are
//added).
public class HighScoresList implements Writable {
    private int[] highScores;

    //EFFECTS: Initializes a new HighScoresList with length 5
    public HighScoresList() {
        highScores = new int[5];
    }

    //MODIFIES: this
    //EFFECTS: Sets the score at given index to the given score
    public void setScore(int i, int score) {
        highScores[i] = score;
    }

    //EFFECTS: Returns the score at the given index
    public int getScore(int i) {
        return highScores[i];
    }

    //EFFECTS: Returns the list of high scores
    public int[] getScores() {
        return highScores;
    }

    //REQUIRES: score >= 0
    //MODIFIES: this
    //EFFECTS: If score is greater than the last score in highScores, then it is a highScore
    //and adds score in its correct spot in highScores such that highScores is from high to low.
    //Otherwise, do nothing
    public boolean isHighScore(int score) {
        if (score > highScores[4]) {
            highScores[4] = score;
            for (int i = highScores.length - 2; i >= 0; i--) {
                if (highScores[i + 1] > highScores[i]) {
                    int num = highScores[i + 1];
                    highScores[i + 1] = highScores[i];
                    highScores[i] = num;
                } else {
                    break;
                }
            }
            return true;
        }
        return false;
    }

    //EFFECTS: Returns a string representation of the scores in highScores
    @Override
    public String toString() {
        String s = "\nHigh Scores: \n";
        for (int i = 0; i < highScores.length; i++) {
            s = s + highScores[i] + "\n";
        }
        return s;
    }

    //EFFECTS: Returns a JSON representation of list of high scores
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int score : highScores) {
            JSONObject json = new JSONObject();
            json.put("score", score);
            jsonArray.put(json);
        }
        jsonObject.put("high scores", jsonArray);
        return jsonObject;
    }

    //EFFECTS: Returns true if all the scores in both lists are equal
    @Override
    public boolean equals(Object o) {
        HighScoresList list = (HighScoresList) o;
        for (int i = 0; i < highScores.length; i++) {
            if (!(list.getScores()[i] == highScores[i])) {
                return false;
            }
        }
        return true;
    }
}
