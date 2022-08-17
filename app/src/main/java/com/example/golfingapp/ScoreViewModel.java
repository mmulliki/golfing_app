package com.example.golfingapp;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> arrayRoundScores = new ArrayList<Integer>();
    private MutableLiveData<ArrayList<Integer>> roundScores = new MutableLiveData<>();
    private static final int LOOP_START_VALUE = 0;
    private static final int LOOP_END_VALUE = 21;
    private static final int FRONT_TOTAL_POSITION = 9;
    private static final int BACK_TOTAL_POSITION = 19;
    private static final int ROUND_TOTAL_POSITION = 20;

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
        } else if (currentHole > FRONT_TOTAL_POSITION && currentHole < BACK_TOTAL_POSITION) {
            int backTotalScore = arrayRoundScores.get(BACK_TOTAL_POSITION);
            backTotalScore += holeScore;
            arrayRoundScores.set(BACK_TOTAL_POSITION, backTotalScore);
        }

        //Add score to round total
        int totalScore = arrayRoundScores.get(ROUND_TOTAL_POSITION);
        totalScore += holeScore;
        arrayRoundScores.set(ROUND_TOTAL_POSITION, totalScore);

        //Update the UI
        roundScores.setValue(arrayRoundScores);
    }

    public void updateScore(int currentHole, boolean isAdd) {
        // Get the old score of the current hole from the array,
        // add that to the new score of the current hole, and add
        // that to the 9/18 hole totals.

        //Get the score from the current hole. If the Add button has been clicked,
        //increase the score by one. Otherwise, decrease the score.
        int oldScore = arrayRoundScores.get(currentHole);

        if (isAdd) {
            oldScore++;
        } else {
            if (oldScore > 0) {
                oldScore--;
            }
        }

        //Set the score for the current hole
        arrayRoundScores.set(currentHole, oldScore);

        //Increase or decrease the score for the front or back nine, according the button
        //pressed, and add the new score to the array.
        if (currentHole < FRONT_TOTAL_POSITION) {
            int frontTotalScore = arrayRoundScores.get(FRONT_TOTAL_POSITION);
            if (isAdd) {
                frontTotalScore++;
            } else {
                if (frontTotalScore > 0) {
                    frontTotalScore--;
                }
            }

            arrayRoundScores.set(FRONT_TOTAL_POSITION, frontTotalScore);
        } else if (currentHole > FRONT_TOTAL_POSITION && currentHole < BACK_TOTAL_POSITION) {
            int backTotalScore = arrayRoundScores.get(BACK_TOTAL_POSITION);
            if (isAdd) {
                backTotalScore++;
            } else {
                if (backTotalScore > 0) {
                    backTotalScore--;
                }
            }

            arrayRoundScores.set(BACK_TOTAL_POSITION, backTotalScore);
        }

        //Increase or decrease the total score of the round, depending on the button selected,
        //and add the new total score to the array.
        int totalScore = arrayRoundScores.get(ROUND_TOTAL_POSITION);

        if (isAdd) {
            totalScore++;
        } else {
            if (totalScore > 0) {
                totalScore--;
            }
        }

        arrayRoundScores.set(ROUND_TOTAL_POSITION, totalScore);

        // Update the UI
        roundScores.setValue(arrayRoundScores);
    }

    public int getTotalScore() {
        return arrayRoundScores.get(ROUND_TOTAL_POSITION);
    }
}
