package com.example.golfingapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreHolder> {
    private ArrayList<Integer> arrayScores;
    private static MainActivity.ClickListener clickListener;
    private int currentHole;
    private Drawable onEditBackground;
    private Drawable border;

    public ScoreAdapter(Context context, int currentHole) {
        this.currentHole = currentHole;
//        onEditBackground = context.getResources().getDrawable(R.drawable.on_edit_background);
        onEditBackground = ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.on_edit_background, context.getTheme());
        border = ResourcesCompat.getDrawable(context.getResources(),
                R.drawable.border, context.getTheme());
    }

    public void setScoreList(ArrayList<Integer> arrayScores) {
        this.arrayScores = arrayScores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdapterTest", "Called onCreateViewHolder" +
                " currentHole: " + currentHole);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View object = inflater.inflate(R.layout.hole_layout, parent, false);

        return new ScoreHolder(border, onEditBackground, object);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder holder, int position) {
        Log.d("AdapterTest", "Called onBindViewHolder");
        holder.bindHoleScore(arrayScores.get(position), position, currentHole, clickListener);
    }

    @Override
    public int getItemCount() {
        return arrayScores.size() - 1;
    }

    public void setOnItemClickListener(MainActivity.ClickListener clickListener) {
        ScoreAdapter.clickListener = clickListener;
    }

    public void setCurrentHole(int currentHole) {
        this.currentHole = currentHole;
    }
}

