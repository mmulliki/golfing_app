package com.example.golfingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
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
    private int currentScore;
    private int currentHole;
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
//        scoreAdapter.setScoreList(scoreViewModel.getScores());
        LiveData<ArrayList<Integer>> roundScores = scoreViewModel.getAllScores();
        roundScores.observe(this, new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                Log.d("Observer", "Entered observer");
                scoreAdapter.setScoreList(integers);
            }
        });

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
                saveScore(view, scoreViewModel, scoreAdapter);
            }
        });
    }

    public void changeCurrentScore(View view) {
        if (view.getId() == buttonAdd.getId()) {
            currentScore++;
        } else {
            if (currentScore > 0) {
                currentScore--;
            }
        }
        updateUiCurrentScore();
    }

    public void saveScore(View view, ScoreViewModel scoreViewModel,
                          ScoreAdapter scoreAdapter) {
        Log.d("CurrentHole", "Before addScore: " + currentHole);
        scoreViewModel.addScore(currentHole, currentScore);

        currentScore = 0;
        currentHole++;
        Log.d("CurrentHole", "After addScore: " + currentHole);
        updateUiCurrentScore();

//        updateUiScoreCard(scoreViewModel, scoreAdapter);
    }

    private void updateUiScoreCard(ScoreViewModel scoreViewModel,
                                   ScoreAdapter scoreAdapter) {
        scoreAdapter.setScoreList(scoreViewModel.getScores());

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
            notifyDataSetChanged();
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


    public void updateUiCurrentScore() {
        textViewCurrentHole.setText(String.valueOf(currentScore));
    }
}