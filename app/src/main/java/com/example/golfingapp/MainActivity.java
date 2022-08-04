package com.example.golfingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonSaveScore;
    private Button buttonAdd;
    private Button buttonSubtract;
    private TextView textViewCurrentHole;
    private int currentScore = 0;
    private int currentHole = 1;
    private RecyclerView recyclerViewScoreCard;
    private RecyclerView recyclerViewBackNine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSaveScore = findViewById(R.id.buttonSaveScore);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        textViewCurrentHole = findViewById(R.id.textViewCurrentHole);

        //Set up ViewModel
        ScoreViewModel scoreViewModel = new ViewModelProvider(this)
                .get(ScoreViewModel.class);

        //Set up RecyclerView
        recyclerViewScoreCard = findViewById(R.id.recyclerViewScoreCard);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(9,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerViewScoreCard.setLayoutManager(layoutManager);
        ScoreAdapter scoreAdapter = new ScoreAdapter();
        recyclerViewScoreCard.setAdapter(scoreAdapter);
        scoreAdapter.setScoreList(scoreViewModel.getScores());

        //Set up listeners
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
        currentScore = 0;
        updateUiCurrentScore();

        updateUiScoreCard();
    }

    private static class ScoreHolder extends RecyclerView.ViewHolder {

        TextView label;
        TextView score;

        public ScoreHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.textViewLabel);
            score = itemView.findViewById(R.id.textViewScore);
        }

        public void bindHoleScore(Integer score, int position) {
            position++;
            if (position < 10) {
                String tempPosition = " " + position;
                label.setText(tempPosition);

                String tempScore = " " + score;
                this.score.setText(tempScore);
            } else {
                label.setText(String.valueOf(position));
                if (score < 10) {
                    String tempString = " " + score;
                    this.score.setText(tempString);
                } else {
                    this.score.setText(String.valueOf(score));
                }
            }
        }
    }

    private static class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {

        private ArrayList<Integer> arrayScores;

        public void setScoreList(ArrayList<Integer> arrayScores) {
            this.arrayScores = arrayScores;
        }

        @NonNull
        @Override
        public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View object = inflater.inflate(R.layout.hole_layout, parent, false);

            return new ScoreHolder(object);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
            holder.bindHoleScore(arrayScores.get(position), position);
        }

        @Override
        public int getItemCount() {
            return arrayScores.size();
        }
    }

    private void updateUiScoreCard() {

    }

    public void updateUiCurrentScore() {
        textViewCurrentHole.setText(String.valueOf(currentScore));
    }
}