package com.unnamed.blotcounter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        LinearLayout historyContainer = findViewById(R.id.historyContainer);

        loadHistory(historyContainer);
    }

    private void loadHistory(LinearLayout container) {
        SharedPreferences sharedPreferences = getSharedPreferences("GameHistory", MODE_PRIVATE);
        String historyJson = sharedPreferences.getString("history_list", "[]");

        try {
            JSONArray historyArray = new JSONArray(historyJson);

            // Iterate backwards to show newest first
            for (int i = historyArray.length() - 1; i >= 0; i--) {
                JSONObject gameObj = historyArray.getJSONObject(i);

                String date = gameObj.optString("date", "Unknown Date");
                String winner = gameObj.optString("winner", "Unknown Winner");
                String score = gameObj.optString("score", "0 - 0");

                addHistoryItem(container, date, winner, score);
            }

            if (historyArray.length() == 0) {
                TextView emptyView = new TextView(this);
                emptyView.setText("Դեռ խաղեր չկան");
                emptyView.setTextSize(18);
                emptyView.setTextColor(Color.GRAY);
                emptyView.setGravity(Gravity.CENTER);
                container.addView(emptyView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addHistoryItem(LinearLayout container, String date, String winner, String score) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(30, 20, 30, 20);
        itemLayout.setBackgroundResource(R.drawable.button_border); // Reusing existing drawable if suitable, or just a
                                                                    // simple border

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 20);
        itemLayout.setLayoutParams(params);

        TextView dateView = new TextView(this);
        dateView.setText(date);
        dateView.setTextSize(14);
        dateView.setTextColor(Color.GRAY);
        itemLayout.addView(dateView);

        TextView winnerView = new TextView(this);
        winnerView.setText(winner);
        winnerView.setTextSize(18);
        winnerView.setTextColor(Color.BLACK);
        winnerView.setTypeface(null, android.graphics.Typeface.BOLD);
        itemLayout.addView(winnerView);

        TextView scoreView = new TextView(this);
        scoreView.setText(score);
        scoreView.setTextSize(16);
        scoreView.setTextColor(Color.DKGRAY);
        itemLayout.addView(scoreView);

        container.addView(itemLayout);
    }
}
