package com.example.golfingapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> arrayRoundScores = new ArrayList<Integer>();
    private MutableLiveData<ArrayList<Integer>> roundScores = new MutableLiveData<>();
    private static final int LOOP_START_VALUE = 0;
    private static final int LOOP_END_VALUE = 20;
    private static final int FRONT_TOTAL_POSITION = 9;
    private static final int BACK_TOTAL_POSITION = 19;

    public ScoreViewModel() {
        for (int i = LOOP_START_VALUE; i < LOOP_END_VALUE; i++) {
            arrayRoundScores.add(0);
        }
        roundScores.setValue(arrayRoundScores);
    }

    public MutableLiveData<ArrayList<Integer>> getAllScores() {
        return roundScores;
    }

    public void addScore(int currentHole, int holeScore) {
        //Set the score for the current hole
        arrayRoundScores.set(currentHole, holeScore);

        //Add score to front nine total
        if (currentHole < FRONT_TOTAL_POSITION) {
            int frontTotalScore = arrayRoundScores.get(FRONT_TOTAL_POSITION);
            frontTotalScore += holeScore;
            arrayRoundScores.set(FRONT_TOTAL_POSITION, frontTotalScore);
        }

        //Add score to round total
        int totalScore = arrayRoundScores.get(BACK_TOTAL_POSITION);
        totalScore += holeScore;
        arrayRoundScores.set(BACK_TOTAL_POSITION, totalScore);

        //Update the UI
        roundScores.setValue(arrayRoundScores);
    }

    public void updateScore(int currentHole, int holeScore) {
        // Get the old score of the current hole from the array,
        // add that to the new score of the current hole, and add
        // that to the 9/18 hole totals.
        int oldScore = arrayRoundScores.get(currentHole);
        int scoreDifferential = oldScore - holeScore;

        if (currentHole < FRONT_TOTAL_POSITION) {
            int frontTotalScore = arrayRoundScores.get(FRONT_TOTAL_POSITION);
            frontTotalScore -= scoreDifferential;
            arrayRoundScores.set(FRONT_TOTAL_POSITION, frontTotalScore);
        }

        int totalScore = arrayRoundScores.get(BACK_TOTAL_POSITION);
        totalScore -= scoreDifferential;
        arrayRoundScores.set(BACK_TOTAL_POSITION, totalScore);

        //Set the score for the current hole
        arrayRoundScores.set(currentHole, holeScore);

        // Update the UI
        roundScores.setValue(arrayRoundScores);
    }
}
