package com.example.golfingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView textViewTotalScore;
    private int currentScore;
    private int currentHole;
    private RecyclerView recyclerViewScoreCard;
    private ScoreViewModel scoreViewModel;
    private ScoreAdapter scoreAdapter;
    private static final String KEY_CURRENT_SCORE = "key_current_score";
    private static final String KEY_CURRENT_HOLE = "key_current_hole";
    private static final int GRID_LAYOUT_SPAN= 10;
    private static final int FRONT_NINE_LABEL_POSITION = 9;
    private static final int BACK_NINE_LABEL_POSITION = 19;
    private static Drawable border;
    private static Drawable onEditBackground;
    private Button buttonPlayerOne;
    private Button buttonPlayerTwo;
    private Button buttonPlayerThree;
    private Button buttonPlayerFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_mail);
        buttonPlayerOne = findViewById(R.id.buttonPlayerOne);
        buttonPlayerTwo = findViewById(R.id.buttonPlayerTwo);
        buttonPlayerThree = findViewById(R.id.buttonPlayerThree);
        buttonPlayerFour = findViewById(R.id.buttonPlayerFour);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSubtract = findViewById(R.id.buttonSubtract);
        buttonArrowLeft = findViewById(R.id.buttonArrowLeft);
        buttonArrowRight = findViewById(R.id.buttonArrowRight);
        textViewTotalScore = findViewById(R.id.textViewTotalScore);
        border = AppCompatResources.getDrawable(this, R.drawable.border);
        onEditBackground = AppCompatResources
                .getDrawable(this, R.drawable.on_edit_background);

        if (savedInstanceState != null) {
            currentScore = savedInstanceState.getInt(KEY_CURRENT_SCORE);
            currentHole = savedInstanceState.getInt(KEY_CURRENT_HOLE);
        }

        //Set up ViewModel
        scoreViewModel = new ViewModelProvider(this)
                .get(ScoreViewModel.class);

        //Set up RecyclerView
        recyclerViewScoreCard = findViewById(R.id.recyclerViewScoreCard);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(GRID_LAYOUT_SPAN,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerViewScoreCard.setLayoutManager(layoutManager);
        scoreAdapter = new ScoreAdapter(this, currentHole);
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
                scoreViewModel.updateScore(scoreAdapter, currentHole, true);
            }
        });

        buttonSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scoreViewModel.getCurrentHoleScore(currentHole) != 0) {
                    scoreViewModel.updateScore(scoreAdapter, currentHole, false);
                }
            }
        });

        buttonArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCurrentHoleBackground(view);
            }
        });

        buttonArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentHole < BACK_NINE_LABEL_POSITION - 1) {
                    changeCurrentHoleBackground(view);
                }
            }
        });

        buttonPlayerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreViewModel.changeDisplayedPlayer(1);
            }
        });

        buttonPlayerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scoreViewModel.changeDisplayedPlayer(2);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_SCORE, currentScore);
        outState.putInt(KEY_CURRENT_HOLE, currentHole);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            resetScore();
        }
        return true;
    }

    private void resetScore() {
        currentHole = 0;
        scoreViewModel.resetScore(scoreAdapter);
    }

    public void changeCurrentHoleBackground(View view) {
        //Set previous hole background to white
        View oldView = Objects.requireNonNull(recyclerViewScoreCard.getLayoutManager())
                .findViewByPosition(currentHole);
        assert oldView != null;
        TextView oldScore = oldView.findViewById(R.id.textViewScore);
        oldScore.setBackground(border);

        //Increase or decrease the current hole, according to button pressed
        if (view.getId() == R.id.buttonArrowRight) {
            if (currentHole < BACK_NINE_LABEL_POSITION - 1) {
                currentHole++;

                if (currentHole == FRONT_NINE_LABEL_POSITION) {
                    currentHole++;
                }
            }
        } else {
            if (currentHole > 0) {
                currentHole--;

                if (currentHole == FRONT_NINE_LABEL_POSITION) {
                    currentHole--;
                }
            }
        }

        //Set current hole background to red
        if (currentHole != BACK_NINE_LABEL_POSITION) {
            View v = Objects.requireNonNull(recyclerViewScoreCard.getLayoutManager())
                    .findViewByPosition(currentHole);
            assert v != null;
            TextView score = v.findViewById(R.id.textViewScore);

            score.setBackground(onEditBackground);
        }
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}