package com.example.golfingapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> roundScore = new ArrayList<Integer>();
    private Button buttonSaveScore;
    private Button buttonAdd;
    private Button buttonSubtract;
    private TextView textViewCurrentHole;
    private int currentScore = 0;
    private int currentHole = 1;
    private final String TEXTVIEW_HOLE_ID = "textViewHole";

    private TextView textViewHole1;
    private TextView textViewHole2;
    private TextView textViewHole3;
    private TextView textViewHole4;
    private TextView textViewHole5;
    private TextView textViewHole6;
    private TextView textViewHole7;
    private TextView textViewHole8;
    private TextView textViewHole9;
    private TextView textViewHole10;
    private TextView textViewHole11;
    private TextView textViewHole12;
    private TextView textViewHole13;
    private TextView textViewHole14;
    private TextView textViewHole15;
    private TextView textViewHole16;
    private TextView textViewHole17;
    private TextView textViewHole18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSaveScore = findViewById(R.id.buttonSaveScore);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        textViewCurrentHole = findViewById(R.id.textViewCurrentHole);

        textViewHole1 = findViewById(R.id.textViewHole1);
        textViewHole2 = findViewById(R.id.textViewHole2);
        textViewHole3 = findViewById(R.id.textViewHole3);
        textViewHole4 = findViewById(R.id.textViewHole4);
        textViewHole5 = findViewById(R.id.textViewHole5);
        textViewHole6 = findViewById(R.id.textViewHole6);
        textViewHole7 = findViewById(R.id.textViewHole7);
        textViewHole8 = findViewById(R.id.textViewHole8);
        textViewHole9 = findViewById(R.id.textViewHole9);
        textViewHole10 = findViewById(R.id.textViewHole10);
        textViewHole11 = findViewById(R.id.textViewHole11);
        textViewHole12 = findViewById(R.id.textViewHole12);
        textViewHole13 = findViewById(R.id.textViewHole13);
        textViewHole14 = findViewById(R.id.textViewHole14);
        textViewHole15 = findViewById(R.id.textViewHole15);
        textViewHole16 = findViewById(R.id.textViewHole16);
        textViewHole17 = findViewById(R.id.textViewHole17);
        textViewHole18 = findViewById(R.id.textViewHole18);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCurrentScore(view);
            }
        });

        buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCurrentScore(view);
            }
        });

        buttonSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveScore(view);
            }
        });
    }

    public void changeCurrentScore(View view) {
        if (view.getId() == buttonAdd.getId()) {
            currentScore += 1;
        } else {
            if (currentScore > 0) {
                currentScore -= 1;
            }
        }
        updateUiCurrentScore();
    }

    public void saveScore(View view) {
        roundScore.add(currentScore);
        currentScore = 0;
        updateUiCurrentScore();

        updateUiScoreCard();

        Log.d("changeCurrentScore", "Size of array: " + roundScore.size());
        for (int i = 1; i < roundScore.size(); i++) {
            Log.d("saveScore", "Hole " + i + ": " + roundScore.get(i));
        }
    }

    private void updateUiScoreCard() {

    }

    public void updateUiCurrentScore() {
        textViewCurrentHole.setText(String.valueOf(currentScore));
    }
}