package com.example.golfingapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ScoreViewModel extends ViewModel {
    private ArrayList<Integer> arrayRoundScores = new ArrayList<Integer>();
    private MutableLiveData<ArrayList<Integer>> roundScores = new MutableLiveData<>();

    public ScoreViewModel() {
        for (int i = 0; i < 18; i++) {
            arrayRoundScores.add(0);
        }
        roundScores.setValue(arrayRoundScores);
    }

    public MutableLiveData<ArrayList<Integer>> getAllScores() {
        return roundScores;
    }

    public void addScore(int currentHole, int holeScore) {
        arrayRoundScores.set(currentHole, holeScore);
        roundScores.setValue(arrayRoundScores);
    }
}
