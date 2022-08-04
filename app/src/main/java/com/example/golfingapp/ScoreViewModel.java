package com.example.golfingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> frontNineScores = new ArrayList<Integer>();
    private ArrayList<Integer> backNineScores = new ArrayList<Integer>();

    public ScoreViewModel() {
        for (int i = 0; i < 18; i++) {
            frontNineScores.add(0);

            if (i > 8) {
                backNineScores.add(0);
            }
        }

    }

    public ArrayList<Integer> getScores() {
        return frontNineScores;
    }

    public ArrayList<Integer> getBackNineScores() {
        return backNineScores;
    }

    public void addScore(int holeScore) {
        frontNineScores.add(holeScore);
    }

    public void updateHole(int holeScore, int index) {
        frontNineScores.add(index, holeScore);
    }
}
