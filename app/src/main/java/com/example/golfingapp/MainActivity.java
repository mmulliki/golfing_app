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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonSaveScore;
    private Button buttonAdd;
    private Button buttonSubtract;
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
        buttonSaveScore = findViewById(R.id.buttonSaveScore);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
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
        ScoreAdapter scoreAdapter = new ScoreAdapter();
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
        scoreAdapter.setOnItemClickListener(new ClickListener() {
            private Drawable[] drawableArray = new Drawable[3];


            @Override
            public void onItemClick(View v, int position) {
                TextView label = v.findViewById(R.id.textViewLabel);
                TextView score = v.findViewById(R.id.textViewScore);

                drawableArray[0] = ResourcesCompat.getDrawable(v.getResources(),
                        R.drawable.on_transition_background, v.getContext().getTheme());
                drawableArray[1] = ResourcesCompat.getDrawable(v.getResources(),
                        R.drawable.on_edit_background, v.getContext().getTheme());

                // If scorecard is not in edit mode, change button to "Edit" mode and turn on
                // edit mode, set the current position to update. Otherwise, turn off edit mode,
                // and set the button text to Save.
                // Restrict clicks to holes, not total.
                if (position != 9 && position < 19) {
                    if (isEdit) {
                        if (position == holeToUpdate) {
                            isEdit = false;
                            buttonSaveScore.setText(getResources().getString(R.string.buttonSaveName));
                            buttonSaveScore.setBackgroundColor(getResources().getColor(R.color.purple_700));
                            label.setBackgroundResource(R.drawable.border);
                            score.setBackgroundResource(R.drawable.border);
                        }
                    } else {
                        isEdit = true;
                        buttonSaveScore.setText(getResources().getString(R.string.buttonEditName));
                        buttonSaveScore.setBackgroundColor(getResources()
                                .getColor(R.color.red_alpha));
                        TransitionDrawable transitionDrawable =
                                new TransitionDrawable(drawableArray);
                        label.setBackground(transitionDrawable);
                        transitionDrawable.startTransition(125);

//                        label.setBackgroundResource(R.drawable.on_edit_background);
//                        score.setBackgroundResource(R.drawable.on_edit_background);

                        holeToUpdate = position;
                    }
                }
//                updateScore(scoreViewModel, position);
            }
        });

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
                if (isEdit) {
                    updateScore(scoreViewModel, holeToUpdate);
                } else {
                    saveScore(scoreViewModel);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_SCORE, currentScore);
        outState.putInt(KEY_CURRENT_HOLE, currentHole);

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

    public void saveScore(ScoreViewModel scoreViewModel) {
        if (currentScore == 0) {
            String message = getResources().getString(R.string.toastValidScoreMessage);
            Toast.makeText(this, message,
                    Toast.LENGTH_SHORT).show();
        } else if (currentHole < 18) {
            scoreViewModel.addScore(currentHole, currentScore);
            currentScore = 0;
            currentHole++;

            updateUiCurrentScore();
        }

    }

    public void updateScore(ScoreViewModel scoreViewModel, int position) {
        scoreViewModel.updateScore(position, currentScore);
        currentScore = 0;
        isEdit = false;
        buttonSaveScore.setText(R.string.buttonSaveName);
        buttonSaveScore.setBackgroundColor(getResources().getColor(R.color.purple_700));
        updateUiCurrentScore();
    }

    public void updateUiCurrentScore() {
        textViewCurrentHole.setText(String.valueOf(currentScore));
    }

    private static class ScoreHolder extends RecyclerView.ViewHolder {
        private TextView label;
        private TextView score;
        private ConstraintLayout constraintLayoutHole;
        private Drawable[] drawableArray = new Drawable[2];

        public ScoreHolder(@NonNull View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.textViewLabel);
            score = itemView.findViewById(R.id.textViewScore);
            constraintLayoutHole = itemView.findViewById(R.id.constraintLayoutHolder);
            ResourcesCompat.getDrawable(itemView.getResources(),
                    R.drawable.on_transition_background, itemView.getContext().getTheme());
            drawableArray[0] = ResourcesCompat.getDrawable(itemView.getResources(),
                    R.drawable.on_transition_background, itemView.getContext().getTheme());
            drawableArray[1] = ResourcesCompat.getDrawable(itemView.getResources(),
                    R.drawable.on_edit_background, itemView.getContext().getTheme());
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
                    ColorDrawable[] arrayColorDrawable = {new ColorDrawable(Color.RED),
                            new ColorDrawable(Color.GREEN)};
//                    TransitionDrawable transitionDrawable =
//                            new TransitionDrawable(drawableArray);
//                    label.setBackground(transitionDrawable);
//                    transitionDrawable.startTransition(125);
                }
            });

        }
    }

    private static class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
        private ArrayList<Integer> arrayScores;
        private static ClickListener clickListener;

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
            holder.bindHoleScore(arrayScores.get(position), position, clickListener);
        }

        @Override
        public int getItemCount() {
            return arrayScores.size() - 1;
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            ScoreAdapter.clickListener = clickListener;
        }
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}