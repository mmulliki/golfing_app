package com.example.golfingapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ScoreHolder extends RecyclerView.ViewHolder {
    private final TextView label;
    private final TextView score;
    private final ConstraintLayout constraintLayoutHole;
    private int currentHole;
    private final Drawable onEditBackground;
    private final Drawable border;
    private static final String FRONT_TOTAL_LABEL = "Ot";
    private static final String BACK_TOTAL_LABEL = "In";
    private static final String ROUND_TOTAL_LABEL = "Total";

    public ScoreHolder(Drawable border, Drawable onEditBackground, @NonNull View itemView) {
        super(itemView);

        label = itemView.findViewById(R.id.textViewLabel);
        score = itemView.findViewById(R.id.textViewScore);
        constraintLayoutHole = itemView.findViewById(R.id.constraintLayoutHolder);
        this.onEditBackground = onEditBackground;
        this.border = border;
    }

    public void bindHoleScore(Integer score, int position, int currentHole,
                              MainActivity.ClickListener clickListener) {
        int thisPosition = position;
        thisPosition++;
        this.currentHole = currentHole;

        if (thisPosition < 10) {
            String tempPosition = " " + thisPosition;
            label.setText(tempPosition);

            String tempScore = " " + score;
            this.score.setText(tempScore);
        } else {
            switch (thisPosition) {
                case 10: label.setText(FRONT_TOTAL_LABEL); break;
                case 20: label.setText(BACK_TOTAL_LABEL); break;
                case 21: label.setText(ROUND_TOTAL_LABEL); break;
                default: label.setText(String.valueOf(thisPosition - 1));

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

        if (thisPosition == currentHole + 1) {
            this.score.setBackground(onEditBackground);
        } else {
            this.score.setBackground(border);
        }
    }
}
