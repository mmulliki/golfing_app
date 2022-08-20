package com.example.golfingapp;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreHolder extends RecyclerView.ViewHolder {
    private TextView label;
    private TextView score;
    private ConstraintLayout constraintLayoutHole;
    private int currentHole;
    private Drawable onEditBackground;
    private static final String FRONT_TOTAL_LABEL = "Ot";
    private static final String BACK_TOTAL_LABEL = "In";
    private static final String ROUND_TOTAL_LABEL = "Total";

    public ScoreHolder(Drawable onEditBackground, @NonNull View itemView, int currentHole) {
        super(itemView);

        label = itemView.findViewById(R.id.textViewLabel);
        score = itemView.findViewById(R.id.textViewScore);
        constraintLayoutHole = itemView.findViewById(R.id.constraintLayoutHolder);
        this.currentHole = currentHole;
        this.onEditBackground = onEditBackground;
    }

    public void bindHoleScore(Integer score, int position, MainActivity.ClickListener clickListener) {
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
