package com.example.golfingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdd;
    private Button buttonSubtract;
    private Button buttonArrowLeft;
    private Button buttonArrowRight;
    private TextView textViewCurrentHole;
    private TextView textViewTotalScore;
    private int currentScore;
    private int currentHole;
    private boolean isEdit = false;
    private int holeToUpdate;
    private RecyclerView recyclerViewScoreCard;
    private static final String KEY_CURRENT_SCORE = "key_current_score";
    private static final String KEY_CURRENT_HOLE = "key_current_hole";
    private static final String FRONT_TOTAL_LABEL = "Ot";
    private static final String BACK_TOTAL_LABEL = "In";
    private static final String ROUND_TOTAL_LABEL = "Total";
    private static final int GRID_LAYOUT_SPAN= 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonArrowLeft = findViewById(R.id.buttonArrowLeft);
        buttonArrowRight = findViewById(R.id.buttonArrowRight);
        textViewCurrentHole = findViewById(R.id.textViewCurrentHole);
        textViewTotalScore = findViewById(R.id.textViewTotalScore);

        if (savedInstanceState != null) {
            currentScore = savedInstanceState.getInt(KEY_CURRENT_SCORE);
            currentHole = savedInstanceState.getInt(KEY_CURRENT_HOLE);
            textViewCurrentHole.setText(String.valueOf(currentScore));
        }

        //Set up ViewModel
        ScoreViewModel scoreViewModel = new ViewModelProvider(this)
                .get(ScoreViewModel.class);

        //Set up RecyclerView
        recyclerViewScoreCard = findViewById(R.id.recyclerViewScoreCard);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(GRID_LAYOUT_SPAN,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerViewScoreCard.setLayoutManager(layoutManager);
        ScoreAdapter scoreAdapter = new ScoreAdapter(this, currentHole);
        recyclerViewScoreCard.setAdapter(scoreAdapter);

        final Observer<ArrayList<Integer>> scoreObserver = new Observer<ArrayList<Integer>>() {
            @Override
            public void onChanged(ArrayList<Integer> integers) {
                scoreAdapter.setScoreList(integers);
                textViewTotalScore.setText(String.valueOf(scoreViewModel.getTotalScore()));
            }
        };

        scoreViewModel.getAllScores().observe(this, scoreObserver);

        //Set up listeners
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                changeCurrentScore(view, scoreViewModel);
                scoreViewModel.updateScore(currentHole, true);
            }
        });

        buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreViewModel.updateScore(currentHole, false);
//                changeCurrentScore(view, scoreViewModel);
            }
        });

        buttonArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentHole > 0) {
                    currentHole--;
                }
                Log.d("ArrowButtons", "Current hole: " + currentHole);
            }
        });

        buttonArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set previous hole background to white
                View oldView = Objects.requireNonNull(recyclerViewScoreCard.getLayoutManager())
                        .findViewByPosition(currentHole);
                assert oldView != null;
                TextView oldScore = oldView.findViewById(R.id.textViewScore);
                oldScore.setBackground(getResources().getDrawable(R.drawable.border));

                if (currentHole < 17) {
                    currentHole++;
                }

                //Set current hole background to red
                View v = Objects.requireNonNull(recyclerViewScoreCard.getLayoutManager())
                        .findViewByPosition(currentHole);
                assert v != null;
                TextView score = v.findViewById(R.id.textViewScore);
                score.setBackground(getResources().getDrawable(R.drawable.on_edit_background));
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_SCORE, currentScore);
        outState.putInt(KEY_CURRENT_HOLE, currentHole);

    }

    private static class ScoreHolder extends RecyclerView.ViewHolder {
        private TextView label;
        private TextView score;
        private ConstraintLayout constraintLayoutHole;
        private int currentHole;
        private Drawable onEditBackground;

        public ScoreHolder(Drawable onEditBackground, @NonNull View itemView, int currentHole) {
            super(itemView);

            label = itemView.findViewById(R.id.textViewLabel);
            score = itemView.findViewById(R.id.textViewScore);
            constraintLayoutHole = itemView.findViewById(R.id.constraintLayoutHolder);
            this.currentHole = currentHole;
            this.onEditBackground = onEditBackground;
        }

        public void bindHoleScore(Integer score, int position, ClickListener clickListener) {
            position++;

            if (position < 10) {
                String tempPosition = " " + position;
                label.setText(tempPosition);

                String tempScore = " " + score;
                this.score.setText(tempScore);
            } else {
                switch (position) {
                    case 10: label.setText(FRONT_TOTAL_LABEL); break;
                    case 20: label.setText(BACK_TOTAL_LABEL); break;
                    case 21: label.setText(ROUND_TOTAL_LABEL); break;
                    default: label.setText(String.valueOf(position - 1));

                }
                if (score < 10) {
                    String tempString = " " + score;
                    this.score.setText(tempString);
                } else {
                    this.score.setText(String.valueOf(score));
                }
            }

            constraintLayoutHole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

            if (position == currentHole + 1) {
                this.score.setBackground(onEditBackground);
            }
        }
    }

    private static class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private ArrayList<Integer> arrayScores;
        private static ClickListener clickListener;
        private int currentHole;
        private Drawable onEditBackground;

        public ScoreAdapter(Context context, int currentHole) {
            this.currentHole = currentHole;
            onEditBackground = context.getResources().getDrawable(R.drawable.on_edit_background);
        }

        public void setScoreList(ArrayList<Integer> arrayScores) {
            this.arrayScores = arrayScores;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View object = inflater.inflate(R.layout.hole_layout, parent, false);

            return new ScoreHolder(onEditBackground, object, currentHole);
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
            holder.bindHoleScore(arrayScores.get(position), position, clickListener);
        }

        @Override
        public int getItemCount() {
            return arrayScores.size() - 1;
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            ScoreAdapter.clickListener = clickListener;
        }

        public void setCurrentHole(int currentHole) {
            this.currentHole = currentHole;
        }
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}