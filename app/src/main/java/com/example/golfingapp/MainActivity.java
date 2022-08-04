package com.example.golfingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Integer> roundScore = new ArrayList<Integer>();
    private ArrayList<Integer> frontNine = new ArrayList<Integer>();
    private ArrayList<Integer> backNine = new ArrayList<Integer>();
    private Button buttonSaveScore;
    private Button buttonAdd;
    private Button buttonSubtract;
    private TextView textViewCurrentHole;
    private int currentScore = 0;
    private int currentHole = 1;
    private final String TEXTVIEW_HOLE_ID = "textViewHole";
    private RecyclerView recyclerViewScoreCard;
    private RecyclerView recyclerViewBackNine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 18; i++) {
            roundScore.add(0);
            if (i < 9) {
                frontNine.add(0);
            } else {
                backNine.add(0);
            }
        }

        buttonSaveScore = findViewById(R.id.buttonSaveScore);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        textViewCurrentHole = findViewById(R.id.textViewCurrentHole);

        //Front nine RecyclerView
        recyclerViewScoreCard = findViewById(R.id.recyclerViewScoreCard);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 9);
        recyclerViewScoreCard.setLayoutManager(layoutManager);
        ScoreAdapter scoreAdapter = new ScoreAdapter();
        recyclerViewScoreCard.setAdapter(scoreAdapter);
        scoreAdapter.setScoreList(frontNine, backNine);
//
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
//                layoutManager.getOrientation());
//        recyclerViewScoreCard.addItemDecoration(dividerItemDecoration);

        //Back nine RecyclerView
        GridLayoutManager layoutManagerBackNine = new GridLayoutManager(this, 9);
        BackNineAdapter backNineAdapter = new BackNineAdapter();
        recyclerViewBackNine = findViewById(R.id.recyclerViewBackNine);
        recyclerViewBackNine.setLayoutManager(layoutManagerBackNine);
        recyclerViewBackNine.setAdapter(backNineAdapter);
        backNineAdapter.setScoreList(backNine);

//        recyclerViewBackNine.addItemDecoration(dividerItemDecoration);

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

        private ArrayList<Integer> frontNine;

        public void setScoreList(ArrayList<Integer> frontNine, ArrayList<Integer> backNine) {
            this.frontNine = frontNine;
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
            holder.bindHoleScore(frontNine.get(position), position);
        }

        @Override
        public int getItemCount() {
            return frontNine.size();
        }
    }

    private static class BackNineAdapter extends RecyclerView.Adapter<ScoreHolder> {

        private ArrayList<Integer> backNine;

        public void setScoreList(ArrayList<Integer> backNine) {
            this.backNine = backNine;
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
            int holeNumber = position + 9;
            holder.bindHoleScore(backNine.get(position), holeNumber);
        }

        @Override
        public int getItemCount() {
            return backNine.size();
        }
    }
    private void updateUiScoreCard() {

    }

    public void updateUiCurrentScore() {
        textViewCurrentHole.setText(String.valueOf(currentScore));
    }
}