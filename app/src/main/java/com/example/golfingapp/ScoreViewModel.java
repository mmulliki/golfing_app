package com.example.golfingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> frontNineScores = new ArrayList<Integer>();
    private MutableLiveData<ArrayList<Integer>> roundScores = new MutableLiveData<>();

    public ScoreViewModel() {
        for (int i = 0; i < 18; i++) {
            frontNineScores.add(0);
        }
        roundScores.setValue(frontNineScores);
    }

    public MutableLiveData<ArrayList<Integer>> getAllScores() {
        return roundScores;
    }

    public ArrayList<Integer> getScores() {
        return frontNineScores;
    }

    public void addScore(int currentHole, int holeScore) {
        frontNineScores.set(currentHole, holeScore);
    }

    public void updateHole(int holeScore, int index) {
        frontNineScores.add(index, holeScore);
    }
}
