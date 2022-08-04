package com.example.golfingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> frontNineScores = new ArrayList<Integer>();

    public ScoreViewModel() {
        for (int i = 0; i < 18; i++) {
            frontNineScores.add(0);
        }

    }

    public ArrayList<Integer> getScores() {
        return frontNineScores;
    }

    public void addScore(int holeScore, int currentHole) {
        frontNineScores.add(holeScore);
    }

    public void updateHole(int holeScore, int index) {
        frontNineScores.add(index, holeScore);
    }
}
