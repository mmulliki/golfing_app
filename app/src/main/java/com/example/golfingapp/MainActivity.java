package com.example.golfingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView recyclerViewScoreCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSaveScore = findViewById(R.id.buttonSaveScore);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        textViewCurrentHole = findViewById(R.id.textViewCurrentHole);
        recyclerViewScoreCard = findViewById(R.id.recyclerViewScoreCard);


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