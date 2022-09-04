package com.example.golfingapp;

import android.os.Handler;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> arrayRoundScores = new ArrayList<Integer>();
    private ArrayList<Integer> playerOneScores = new ArrayList<>();
    private MutableLiveData<ArrayList<Integer>> roundScores = new MutableLiveData<>();
    private static final int LOOP_START_VALUE = 0;
    private static final int LOOP_END_VALUE = 21;
    private static final int FRONT_TOTAL_POSITION = 9;
    private static final int BACK_TOTAL_POSITION = 19;
    private static final int ROUND_TOTAL_POSITION = 20;

    public ScoreViewModel() {
//        for (int i = LOOP_START_VALUE; i < LOOP_END_VALUE; i++) {
//            arrayRoundScores.add(0);
//        }
        setRoundScores();
        playerOneScores.addAll(arrayRoundScores);
//        roundScores.setValue(arrayRoundScores);
    }

    public MutableLiveData<ArrayList<Integer>> getAllScores() {
        return roundScores;
    }

    public void updateScore(ScoreAdapter scoreAdapter, int currentHole, boolean isAdd) {
        //Get the score from the current hole. If the Add button has been clicked,
        //increase the score by one. Otherwise, decrease the score.
        int oldScore = arrayRoundScores.get(currentHole);

        if (isAdd) {
            if (oldScore < 99) {
                oldScore++;
            }
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
                if (frontTotalScore < 99) {
                    frontTotalScore++;
                }
            } else {
                if (frontTotalScore > 0) {
                    frontTotalScore--;
                }
            }

            arrayRoundScores.set(FRONT_TOTAL_POSITION, frontTotalScore);
        } else if (currentHole > FRONT_TOTAL_POSITION && currentHole < BACK_TOTAL_POSITION) {
            int backTotalScore = arrayRoundScores.get(BACK_TOTAL_POSITION);
            if (isAdd) {
                if (backTotalScore < 99) {
                    backTotalScore++;
                }
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
        scoreAdapter.setCurrentHole(currentHole);
        roundScores.setValue(arrayRoundScores);
    }

    public int getTotalScore() {
        return arrayRoundScores.get(ROUND_TOTAL_POSITION);
    }

    public int getCurrentHoleScore(int currentHole) {
        return arrayRoundScores.get(currentHole);
    }

    public void resetScore(ScoreAdapter scoreAdapter) {
        for (int i = LOOP_START_VALUE; i < LOOP_END_VALUE; i++) {
            arrayRoundScores.set(i, 0);
        }
        scoreAdapter.setCurrentHole(0);
        roundScores.setValue(arrayRoundScores);

    }

    public void changeDisplayedPlayer(int buttonID) {
        if (buttonID == 1) {
            roundScores.setValue(playerOneScores);
        } else {
            roundScores.setValue(arrayRoundScores);
        }
    }

    public void setRoundScores() {
        Handler responseHandler = new Handler();

        ResetArrayThread resetArrayThread = new ResetArrayThread(arrayRoundScores, roundScores,
                responseHandler);
        resetArrayThread.start();
        resetArrayThread.getLooper();
        while (resetArrayThread.getRequestHandler() == null) {

        }
        resetArrayThread.queueResetHT();
        resetArrayThread.quitSafely();
    }

}
