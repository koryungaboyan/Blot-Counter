package com.unnamed.blotcounter;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.activity.EdgeToEdge;


import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    TextView textView1, textView4;
    TextView roundNumber, roundScore, roundScoreRed, terz1, terz2, we, you, quansh;
    TextView roundEdit;
    int roundTerz1, roundTerz2, roundNumberInt;
    String result;
    TextView roundTextView;
    int totalTeam1, totalTeam2;
    boolean quanshed, sharped;
    boolean said;
    boolean scoreSet;
    private LinearLayout scoresDisplayContainer;
    private boolean updateTerz1, kp;

    private int secondsPassed = 0;
    private static Timer timer = new Timer();
    public int round_terz1, round_terz2, plus_score, minus_score, roundScoreWritten;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        scoresDisplayContainer = findViewById(R.id.scoresDisplayContainer);
        textView1 = findViewById(R.id.textView1);
        textView4 = findViewById(R.id.textView4);
        roundNumber = findViewById(R.id.roundNumber);
        roundScore = findViewById(R.id.roundScore);
        roundScoreRed = findViewById(R.id.roundScoreRed);
        quansh = findViewById(R.id.quansh);
        roundEdit = findViewById(R.id.roundEdit);
        textView1.setEnabled(false);
        roundScore.setEnabled(false);
        textView4.setEnabled(false);
        roundScore.setZ(1000);
        roundScoreRed.setZ(1001);
        roundTerz1 = 0;
        roundTerz2 = 0;
        totalTeam2 = 0;
        totalTeam1 = 0;
        quanshed = false;
        sharped = false;
        scoreSet = false;
        kp = false;
        said = false;
        quansh.setEnabled(said);
        roundNumber.setTextColor(RED);

        startTimer();


        textView1.setOnClickListener(v -> {
            updateTerz1 = true; // Update terz1
            showAlertDialog();
            if (roundTerz1 <= 0) {
                roundTerz1 = 0;
            }
        });

        textView4.setOnClickListener(v -> {
            updateTerz1 = false; // Update terz2
            showAlertDialog();
        });

        roundNumber.setOnClickListener(v -> {
            setRoundNumber();
        });

        roundScoreRed.setOnClickListener(v -> {
            if (roundScore.isEnabled() && !kp) {
                setRoundScore(result);
            } else if (roundScore.isEnabled() && kp) {
                showKPdialog(result);
            }
        });

        quansh.setOnClickListener(v -> {
            if (!quanshed) {
                setQuansh(quansh);
            } else if (!sharped) {
                setQuansh(quansh);
            }
        });

        roundEdit.setOnClickListener(v -> {
            showRoundEditDialog();
        });

    }

    @SuppressLint("ResourceAsColor")
    private void showAlertDialog() {
        // Create an EditText for the alert dialog
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED); // Allow negative numbers
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER); // Center alignment
        editText.setTextColor(R.color.black);

        // Create the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(editText);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String enteredText = editText.getText().toString().trim();

            // Validate if enteredText is a valid number
            if (isValidNumber(enteredText)) {
                // Calculate sum only if input is a valid number
                int sum = calculateSum(enteredText);

                // Update UI based on sum
                if (updateTerz1) {
                    roundTerz1 += sum;
                    if (roundTerz1 < 0) {
                        roundTerz1 = 0;
                    }
                    if (terz1 != null) {
                        terz1.setText(String.valueOf(roundTerz1));
                    }
                } else {
                    roundTerz2 += sum;
                    if (roundTerz2 < 0) {
                        roundTerz2 = 0;
                    }
                    if (terz2 != null) {
                        terz2.setText(String.valueOf(roundTerz2));
                    }
                }
            } else {
                // Handle invalid input (optional: show error message)
                Toast.makeText(getApplicationContext(), "Invalid input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.show();
    }

    private boolean isValidNumber(String input) {
        // Check if input is a valid number (including negative numbers)
        return input.matches("-?\\d+");
    }

    private int calculateSum(String input) {
        int sum = 0;

        // Replace all non-digit characters except '-' with spaces
        input = input.replaceAll("[^\\d-]", " ");

        // Split the cleaned text by spaces to get individual number strings
        String[] numberStrings = input.trim().split("\\s+");

        // Calculate sum of valid numbers
        for (String numberStr : numberStrings) {
            if (!TextUtils.isEmpty(numberStr)) {
                int number = Integer.parseInt(numberStr.trim());
                sum += number;
            }
        }

        return sum;
    }


    @SuppressLint("ResourceAsColor")
    public void setRoundNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create a ScrollView to hold the layout
        ScrollView scrollView = new ScrollView(this);

        // Create a linear layout to hold the buttons and edit text
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER); // Center the contents vertically
        scrollView.addView(layout);

        // Create buttons for the first group
        LinearLayout.LayoutParams buttonParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Adjust width as needed
                LinearLayout.LayoutParams.WRAP_CONTENT // Adjust height as needed
        );
        buttonParams1.setMargins(0, 20, 0, 20); // Set margins (left, top, right, bottom)

        LinearLayout buttonGroup1 = new LinearLayout(this);
        buttonGroup1.setOrientation(LinearLayout.HORIZONTAL);
        buttonGroup1.setGravity(Gravity.CENTER);

        Button buttonPlus = new Button(this);
        buttonPlus.setText("+");
        buttonPlus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        buttonPlus.setTextColor(BLACK);
        buttonPlus.setLayoutParams(buttonParams1);

        Button buttonMinus = new Button(this);
        buttonMinus.setText("-");
        buttonMinus.setTextColor(BLACK);
        buttonMinus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        buttonMinus.setLayoutParams(buttonParams1);

        buttonGroup1.addView(buttonPlus);
        buttonGroup1.addView(buttonMinus);

        CheckBox kp_checkbox = new CheckBox(this);
        kp_checkbox.setText("Կապույտ");
        kp_checkbox.setTextColor(BLACK);
        kp_checkbox.setTextSize(18);
        kp_checkbox.setGravity(View.TEXT_ALIGNMENT_CENTER);
        kp_checkbox.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Create buttons for the second group
        LinearLayout buttonGroup2 = new LinearLayout(this);
        buttonGroup2.setOrientation(LinearLayout.HORIZONTAL);
        buttonGroup2.setGravity(Gravity.CENTER);

        // Define button properties for all options
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        buttonParams.setMargins(0, 20, 0, 20);

        Button option1 = new Button(this);
        option1.setText("♥️");
        option1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        option1.setLayoutParams(buttonParams);
        Button option2 = new Button(this);
        option2.setText("♦️");
        option2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        option2.setLayoutParams(buttonParams);
        Button option3 = new Button(this);
        option3.setText("♣️");
        option3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        option3.setLayoutParams(buttonParams);
        Button option4 = new Button(this);
        option4.setText("♠️");
        option4.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        option4.setLayoutParams(buttonParams);
        Button option5 = new Button(this);
        option5.setText("X");
        option5.setTextColor(BLACK);
        option5.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        option5.setLayoutParams(buttonParams);

        buttonGroup2.addView(option1);
        buttonGroup2.addView(option2);
        buttonGroup2.addView(option3);
        buttonGroup2.addView(option4);
        buttonGroup2.addView(option5);

        option1.setBackgroundResource(R.drawable.backgroundstatesecond);
        buttonPlus.setBackgroundResource(R.drawable.backgroundstatesecond);
        buttonMinus.setBackgroundResource(R.drawable.backgroundstatesecond);
        option2.setBackgroundResource(R.drawable.backgroundstatesecond);
        option3.setBackgroundResource(R.drawable.backgroundstatesecond);
        option4.setBackgroundResource(R.drawable.backgroundstatesecond);
        option5.setBackgroundResource(R.drawable.backgroundstatesecond);

        LinearLayout.LayoutParams checkboxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Adjust width as needed
                LinearLayout.LayoutParams.WRAP_CONTENT  // Adjust height as needed
        );
        checkboxParams.gravity = Gravity.CENTER;  // Center the checkbox

        kp_checkbox.setLayoutParams(checkboxParams); // Apply layout parameters

        // Create an EditText
        TextView textView = new TextView(this);
        textView.setText("Խոսացած");
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24); // Set text size to 24dp
        textView.setTextColor(BLACK);

        EditText editText = new EditText(this);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setGravity(Gravity.CENTER);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHintTextColor(Color.GRAY);
        editText.setTextColor(BLACK);

        kp_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editText.setText("25");
            } else {
                editText.setText(""); // Clear or set to a default value if unchecked
            }
        });

        layout.setGravity(Gravity.CENTER);

        // Add the button groups and edit text to the layout
        layout.addView(buttonGroup1);
        layout.addView(buttonGroup2);
        layout.addView(textView);
        layout.addView(editText);
        layout.addView(kp_checkbox);

        builder.setView(scrollView);

        builder.setPositiveButton("OK", (dialog, which) -> {
            roundNumber.setTextColor(BLACK);
            roundNumber.setEnabled(false);
            roundNumber.setClickable(false);
            roundScoreRed.setVisibility(VISIBLE);
            textView1.setTextColor(RED);
            textView1.setEnabled(true);
            textView1.setClickable(true);
            textView4.setTextColor(RED);
            textView4.setEnabled(true);
            textView4.setClickable(true);
            quansh.setEnabled(true);
            quansh.setClickable(true);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> {
                Button selectedButton1 = buttonPlus.isSelected() ? buttonPlus : buttonMinus;
                Button selectedButton2 = option1.isSelected() ? option1 :
                        option2.isSelected() ? option2 :
                                option3.isSelected() ? option3 :
                                        option4.isSelected() ? option4 :
                                                option5;

                String selectedOption1 = selectedButton1.getText().toString();
                String selectedOption2 = selectedButton2.getText().toString();
                String inputText = editText.getText().toString();

                if (inputText.isEmpty() || !selectedButton1.isSelected() || !selectedButton2.isSelected()) {
                    Toast.makeText(this, "Please enter a valid inputs", Toast.LENGTH_SHORT).show();
                } else {
                    int inputValue = Integer.parseInt(inputText);

                    if (kp_checkbox.isChecked() && inputValue < 25) {
                        Toast.makeText(this, "Կապույտի համար 25 կամ ավել", Toast.LENGTH_LONG).show();
                        kp_checkbox.setChecked(false);
                        kp = false;
                    } else if (inputValue < 8) {
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                    } else {
                        roundNumberInt = inputValue;

                        result = selectedOption1 + " " + selectedOption2 + " " + inputText;
                        roundNumber.setEnabled(false);

                        quansh.setEnabled(true);
                        roundScoreRed.setVisibility(VISIBLE);
                        textView1.setTextColor(RED);
                        textView4.setTextColor(RED);
                        roundNumber.setTextColor(BLACK);
                        roundTerz1 = 0;
                        roundTerz2 = 0;
                        textView1.setEnabled(true);
                        textView4.setEnabled(true);
                        System.out.println(roundScore.isEnabled());
                        kp = kp_checkbox.isChecked();

                        quansh.setEnabled(true);

                        addScorePair(result);
                        dialog.dismiss();
                    }
                }
            });

            // Set the text color of the dialog buttons
            positiveButton.setTextColor(getResources().getColor(R.color.black));
            positiveButton.setBackgroundResource(R.drawable.button_border);
            positiveButton.setBackgroundColor(R.drawable.button_border);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negativeButton.setTextColor(getResources().getColor(R.color.black));
            negativeButton.setBackgroundResource(R.drawable.button_border);

            positiveButton.setBackgroundResource(R.drawable.button_border);
            negativeButton.setBackgroundResource(R.drawable.button_border);
        });

        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.show();

// Add click listeners to toggle the button states
        buttonPlus.setOnClickListener(view -> {
            buttonPlus.setSelected(true);
            buttonPlus.setBackgroundResource(R.drawable.backgroundstatefirst);
            buttonMinus.setSelected(false);
            buttonMinus.setBackgroundResource(R.drawable.backgroundstatesecond);
        });

        buttonMinus.setOnClickListener(view -> {
            buttonPlus.setSelected(false);
            buttonPlus.setBackgroundResource(R.drawable.backgroundstatesecond);
            buttonMinus.setSelected(true);
            buttonMinus.setBackgroundResource(R.drawable.backgroundstatefirst);
        });

        option1.setOnClickListener(view -> {
            option1.setSelected(true);
            option1.setBackgroundResource(R.drawable.backgroundstatefirst);
            option2.setSelected(false);
            option2.setBackgroundResource(R.drawable.backgroundstatesecond);
            option3.setSelected(false);
            option3.setBackgroundResource(R.drawable.backgroundstatesecond);
            option4.setSelected(false);
            option4.setBackgroundResource(R.drawable.backgroundstatesecond);
            option5.setSelected(false);
            option5.setBackgroundResource(R.drawable.backgroundstatesecond);
        });

        option2.setOnClickListener(view -> {
            option1.setSelected(false);
            option1.setBackgroundResource(R.drawable.backgroundstatesecond);
            option2.setSelected(true);
            option2.setBackgroundResource(R.drawable.backgroundstatefirst);
            option3.setSelected(false);
            option3.setBackgroundResource(R.drawable.backgroundstatesecond);
            option4.setSelected(false);
            option4.setBackgroundResource(R.drawable.backgroundstatesecond);
            option5.setSelected(false);
            option5.setBackgroundResource(R.drawable.backgroundstatesecond);
        });

        option3.setOnClickListener(view -> {
            option1.setSelected(false);
            option1.setBackgroundResource(R.drawable.backgroundstatesecond);
            option2.setSelected(false);
            option2.setBackgroundResource(R.drawable.backgroundstatesecond);
            option3.setSelected(true);
            option3.setBackgroundResource(R.drawable.backgroundstatefirst);
            option4.setSelected(false);
            option4.setBackgroundResource(R.drawable.backgroundstatesecond);
            option5.setSelected(false);
            option5.setBackgroundResource(R.drawable.backgroundstatesecond);
        });

        option4.setOnClickListener(view -> {
            option1.setSelected(false);
            option1.setBackgroundResource(R.drawable.backgroundstatesecond);
            option2.setSelected(false);
            option2.setBackgroundResource(R.drawable.backgroundstatesecond);
            option3.setSelected(false);
            option3.setBackgroundResource(R.drawable.backgroundstatesecond);
            option4.setSelected(true);
            option4.setBackgroundResource(R.drawable.backgroundstatefirst);
            option5.setSelected(false);
            option5.setBackgroundResource(R.drawable.backgroundstatesecond);
        });

        option5.setOnClickListener(view -> {
            option1.setSelected(false);
            option1.setBackgroundResource(R.drawable.backgroundstatesecond);
            option2.setSelected(false);
            option2.setBackgroundResource(R.drawable.backgroundstatesecond);
            option3.setSelected(false);
            option3.setBackgroundResource(R.drawable.backgroundstatesecond);
            option4.setSelected(false);
            option4.setBackgroundResource(R.drawable.backgroundstatesecond);
            option5.setSelected(true);
            option5.setBackgroundResource(R.drawable.backgroundstatefirst);
        });
    }


    private void addScorePair(String roundNumber) {
        LinearLayout scorePairLayout = new LinearLayout(this);
        scorePairLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 16);
        scorePairLayout.setLayoutParams(layoutParams);

        // Create TextViews for displaying Team 1, Round Number, Team 2 scores, and who said the number
        terz1 = new TextView(this);
        terz1.setText(String.valueOf(0));
        terz1.setTextSize(18); // Smaller text size for terz1
        terz1.setTextColor(BLACK);
        terz1.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.5f // Less weight to make it less prominent
        ));
        terz1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        we = new TextView(this);
        we.setText(String.valueOf(0));
        we.setTextSize(24);
        we.setTextColor(BLACK);
        we.setTypeface(null, Typeface.BOLD);
        we.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        we.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        you = new TextView(this);
        you.setText(String.valueOf(0));
        you.setTextSize(24);
        you.setTextColor(BLACK);
        you.setTypeface(null, Typeface.BOLD);
        you.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        you.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        terz2 = new TextView(this);
        terz2.setText(String.valueOf(0));
        terz2.setTextSize(18);
        terz2.setTextColor(BLACK);
        terz2.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.5f // Less weight to make it less prominent
        ));
        terz2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        roundTextView = new TextView(this);
        roundTextView.setText(roundNumber);
        roundTextView.setTextSize(18);
        roundTextView.setTextColor(BLACK);
        roundTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        roundTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        roundScore = new TextView(this);
        roundScore.setText(String.valueOf(0));
        roundScore.setTextSize(24);
        roundScore.setVisibility(VISIBLE);
        roundScore.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        roundScore.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Add TextViews to the score pair layout
        scorePairLayout.addView(terz1);
        scorePairLayout.addView(we);
        scorePairLayout.addView(you);
        scorePairLayout.addView(terz2);
        scorePairLayout.addView(roundTextView);
        scorePairLayout.addView(roundScore);

        // Add the score pair layout to the main scoresDisplayContainer
        scoresDisplayContainer.addView(scorePairLayout);
    }


    public void setRoundScore(String result) {
        final char firstChar = result.charAt(0);
        final EditText editText = new EditText(MainActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(editText);

        builder.setPositiveButton("OK", null); // override later
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // 🔹 Do NOT disable anything here, just dismiss
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener( v -> {
                String enteredText = editText.getText().toString().trim();
                if (enteredText.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int enteredNumber = evaluateExpression(enteredText);

                    if (enteredNumber == 162) {
                        enteredNumber = 250;
                    }

                    roundScore.setText(String.valueOf(enteredNumber));
                    int roundedNumber = roundUp(enteredNumber);
                    if (roundedNumber < 0) roundedNumber = 0;

                    int z = roundTerz1 + roundTerz2;

                    // Apply scoring rules
                    if (firstChar == '+') {
                        if (enteredNumber + (roundTerz1 * 10) < roundNumberInt * 10) {
                            if (quanshed) {
                                totalTeam2 += (2 * roundNumberInt) + 16 + z;
                            } else if (sharped) {
                                totalTeam2 += (4 * roundNumberInt) + 16 + z;
                            } else {
                                totalTeam2 += 16 + roundNumberInt + z;
                            }
                        } else {
                            if (quanshed) {
                                totalTeam1 += (2 * roundNumberInt) + 16 + z;
                            } else if (sharped) {
                                totalTeam1 += (4 * roundNumberInt) + 16 + z;
                            } else {
                                totalTeam1 += (enteredNumber == 250 ? 25 : roundedNumber) + roundNumberInt + roundTerz1;
                                totalTeam2 += roundTerz2 + Math.max(0, 16 - roundedNumber);
                            }
                        }
                    } else {
                        if (enteredNumber + (roundTerz2 * 10) < roundNumberInt * 10) {
                            if (quanshed) {
                                totalTeam1 += (2 * roundNumberInt) + 16 + z;
                            } else if (sharped) {
                                totalTeam1 += (4 * roundNumberInt) + 16 + z;
                            } else {
                                totalTeam1 += 16 + roundNumberInt + z;
                            }
                        } else {
                            if (quanshed) {
                                totalTeam2 += (2 * roundNumberInt) + 16 + z;
                            } else if (sharped) {
                                totalTeam2 += (4 * roundNumberInt) + 16 + z;
                            } else {
                                totalTeam2 += (enteredNumber == 250 ? 25 : roundedNumber) + roundNumberInt + roundTerz2;
                                totalTeam1 += roundTerz1 + Math.max(0, 16 - roundedNumber);
                            }
                        }
                    }

                    // Update team scores
                    you.setText(String.valueOf(totalTeam2));
                    we.setText(String.valueOf(totalTeam1));

                    quanshed = false;
                    sharped = false;

                    if (totalTeam1 >= 301) gameOver(1);
                    if (totalTeam2 >= 301) gameOver(2);

                    roundScore.setVisibility(VISIBLE);
                    roundScoreRed.setVisibility(INVISIBLE);

                    roundNumber.setTextColor(Color.RED);
                    roundNumber.setEnabled(true);
                    roundNumber.setClickable(true);

                    textView1.setTextColor(Color.BLACK);
                    textView1.setEnabled(false);
                    textView1.setClickable(false);

                    textView4.setTextColor(Color.BLACK);
                    textView4.setEnabled(false);
                    textView4.setClickable(false);

                    quansh.setEnabled(false);
                    quansh.setClickable(false);

                    editText.setEnabled(false);

                    dialog.dismiss();
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Please enter a valid number or expression", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.setOnDismissListener(d -> {
            // Only enforce disabled state after OK, not Cancel
            // (handled above)
        });

        dialog.show();
    }


    private int roundUp(double number) {
        double numberAfter = number / 10.0 - 0.6;
        return (int) Math.ceil(numberAfter);
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                secondsPassed++;
            }
        };
        // Start the timer and schedule the task to run every 1000 milliseconds (1 second)
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    // Call this method to stop the timer
    private void stopTimer() {
        timer.cancel();
    }

    // Modify gameOver method to display time
    private void gameOver(int team) {
        stopTimer();  // Stop the timer when the game is over

        int hours = secondsPassed / 3600;
        int minutes = (secondsPassed % 3600) / 60;
        int seconds = (secondsPassed % 60);

        String timeElapsed = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team " + team + " wins!");
        builder.setMessage("Time Elapsed: " + timeElapsed + "Start over?");
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Reset timer and go to StartActivity
            Intent intent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize TextView if needed
        TextView textView = dialog.findViewById(android.R.id.message);
        if (textView != null) {
            textView.setTextSize(18);  // Example of customizing text size
        }
    }

    private void setQuansh(TextView quansh) {
        String roundNumberText = roundTextView.getText().toString();
        if (!quanshed) {
            quanshed = true;
            roundTextView.setText(roundNumberText + " ք");
            quansh.setBackgroundResource(R.drawable.button_bggrey);
            quansh.setText("սրել");
        } else {
            sharped = true;
            quanshed = false;
            if (roundNumberText.endsWith("ք")) {
                roundNumberText = roundNumberText.substring(0, roundNumberText.length() - 1) + "✓";
            } else {
                roundNumberText += "✓";
            }
            roundTextView.setText(roundNumberText);
            quansh.setBackgroundResource(R.drawable.button_bgyellow);
            quansh.setText("✓");
        }
    }

    private int evaluateExpression(String expression) {
        // Trim any whitespace from the expression
        expression = expression.replaceAll("\\s+", "");

        Stack<Integer> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        int currentNumber = 0;
        boolean buildingNumber = false;

        for (int i = 0; i < expression.length(); i++) {
            char currentChar = expression.charAt(i);

            if (Character.isDigit(currentChar)) {
                currentNumber = currentNumber * 10 + (currentChar - '0');
                buildingNumber = true;
            } else {
                if (buildingNumber) {
                    numbers.push(currentNumber);
                    currentNumber = 0;
                    buildingNumber = false;
                }

                if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                    while (!operations.isEmpty() && precedence(operations.peek()) >= precedence(currentChar)) {
                        numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
                    }
                    operations.push(currentChar);
                }
            }
        }

        if (buildingNumber) {
            numbers.push(currentNumber);
        }

        while (!operations.isEmpty()) {
            numbers.push(applyOperation(operations.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private int precedence(char operation) {
        if (operation == '+' || operation == '-') {
            return 1;
        } else if (operation == '*' || operation == '/') {
            return 2;
        }
        return -1;
    }

    private int applyOperation(char operation, int b, int a) {
        switch (operation) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    private void showRoundEditDialog() {
        // Check if there's a current round to edit
        if (roundTextView == null || roundTextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "No round to edit", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a ScrollView to hold the layout
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(40, 20, 40, 20);
        scrollView.addView(layout);

        // Title
        TextView titleText = new TextView(this);
        titleText.setText("Խմբագրել խաղափուլը");
        titleText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        titleText.setTextColor(BLACK);
        titleText.setTypeface(null, Typeface.BOLD);
        titleText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, 30);
        titleText.setLayoutParams(titleParams);
        layout.addView(titleText);

        // Terz1 (Team 1 declarations)
        TextView terz1Label = new TextView(this);
        terz1Label.setText("Թերզ +:");
        terz1Label.setTextSize(18);
        terz1Label.setTextColor(BLACK);
        layout.addView(terz1Label);

        EditText terz1Edit = new EditText(this);
        terz1Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        terz1Edit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        terz1Edit.setText(String.valueOf(roundTerz1));
        terz1Edit.setTextColor(BLACK);
        layout.addView(terz1Edit);

        // Terz2 (Team 2 declarations)
        TextView terz2Label = new TextView(this);
        terz2Label.setText("Թերզ -:");
        terz2Label.setTextSize(18);
        terz2Label.setTextColor(BLACK);
        LinearLayout.LayoutParams terz2LabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        terz2LabelParams.setMargins(0, 20, 0, 0);
        terz2Label.setLayoutParams(terz2LabelParams);
        layout.addView(terz2Label);

        EditText terz2Edit = new EditText(this);
        terz2Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        terz2Edit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        terz2Edit.setText(String.valueOf(roundTerz2));
        terz2Edit.setTextColor(BLACK);
        layout.addView(terz2Edit);

        // Round Score
        TextView scoreLabel = new TextView(this);
        scoreLabel.setText("Խոսացողը տարել է:");
        scoreLabel.setTextSize(18);
        scoreLabel.setTextColor(BLACK);
        LinearLayout.LayoutParams scoreLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        scoreLabelParams.setMargins(0, 20, 0, 0);
        scoreLabel.setLayoutParams(scoreLabelParams);
        layout.addView(scoreLabel);

        EditText scoreEdit = new EditText(this);
        scoreEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT);
        scoreEdit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        scoreEdit.setText(roundScore.getText().toString());
        scoreEdit.setTextColor(BLACK);
        layout.addView(scoreEdit);

        // Team 1 Total Score
        TextView team1Label = new TextView(this);
        team1Label.setText("Հաշիվ +:");
        team1Label.setTextSize(18);
        team1Label.setTextColor(BLACK);
        LinearLayout.LayoutParams team1LabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        team1LabelParams.setMargins(0, 20, 0, 0);
        team1Label.setLayoutParams(team1LabelParams);
        layout.addView(team1Label);

        EditText team1Edit = new EditText(this);
        team1Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        team1Edit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        team1Edit.setText(we.getText().toString());
        team1Edit.setTextColor(BLACK);
        layout.addView(team1Edit);

        // Team 2 Total Score
        TextView team2Label = new TextView(this);
        team2Label.setText("Հաշիվ -:");
        team2Label.setTextSize(18);
        team2Label.setTextColor(BLACK);
        LinearLayout.LayoutParams team2LabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        team2LabelParams.setMargins(0, 20, 0, 0);
        team2Label.setLayoutParams(team2LabelParams);
        layout.addView(team2Label);

        EditText team2Edit = new EditText(this);
        team2Edit.setInputType(InputType.TYPE_CLASS_NUMBER);
        team2Edit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        team2Edit.setText(you.getText().toString());
        team2Edit.setTextColor(BLACK);
        layout.addView(team2Edit);

        // Round Number Info (display only)
        TextView roundInfoLabel = new TextView(this);
        roundInfoLabel.setText("Խոսացած:");
        roundInfoLabel.setTextSize(18);
        roundInfoLabel.setTextColor(BLACK);
        LinearLayout.LayoutParams roundInfoLabelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        roundInfoLabelParams.setMargins(0, 20, 0, 0);
        roundInfoLabel.setLayoutParams(roundInfoLabelParams);
        layout.addView(roundInfoLabel);

        TextView roundInfoText = new TextView(this);
        roundInfoText.setText(roundTextView.getText().toString());
        roundInfoText.setTextSize(16);
        roundInfoText.setTextColor(BLACK);
        roundInfoText.setGravity(Gravity.CENTER);
        roundInfoText.setPadding(10, 10, 10, 10);
        roundInfoText.setBackgroundResource(R.drawable.button_border);
        layout.addView(roundInfoText);

        // Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(scrollView);

        builder.setPositiveButton("Պահպանել", (dialog, which) -> {
            try {
                // Update terz values
                int newTerz1 = Integer.parseInt(terz1Edit.getText().toString());
                int newTerz2 = Integer.parseInt(terz2Edit.getText().toString());

                // Update round score
                String scoreText = scoreEdit.getText().toString().trim();
                int newRoundScore = scoreText.isEmpty() ? 0 : evaluateExpression(scoreText);

                // Update team totals
                int newTeam1Total = Integer.parseInt(team1Edit.getText().toString());
                int newTeam2Total = Integer.parseInt(team2Edit.getText().toString());

                // Apply changes
                roundTerz1 = newTerz1;
                roundTerz2 = newTerz2;
                totalTeam1 = newTeam1Total;
                totalTeam2 = newTeam2Total;

                // Update UI
                terz1.setText(String.valueOf(roundTerz1));
                terz2.setText(String.valueOf(roundTerz2));
                roundScore.setText(String.valueOf(newRoundScore));
                we.setText(String.valueOf(totalTeam1));
                you.setText(String.valueOf(totalTeam2));

                Toast.makeText(this, "Տվյալները պահպանվեցին", Toast.LENGTH_SHORT).show();

                // Check for game over
                if (totalTeam1 >= 301) gameOver(1);
                if (totalTeam2 >= 301) gameOver(2);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Չեղարկել", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.show();

        // Style buttons
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(getResources().getColor(R.color.black));
        }
        if (negativeButton != null) {
            negativeButton.setTextColor(getResources().getColor(R.color.black));
        }
    }

    @SuppressLint("ResourceAsColor")
    private void showKPdialog(String result) {
        // Create a linear layout to hold the checkbox
        final char firstChar = result.charAt(0);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER); // Center the contents horizontally and vertically

        // Create and configure the checkbox
        CheckBox kp_bool = new CheckBox(this);
        kp_bool.setText("Կապույտ");
        kp_bool.setTextColor(BLACK);
        kp_bool.setTextSize(24);

        LinearLayout.LayoutParams checkboxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Adjust width as needed
                LinearLayout.LayoutParams.WRAP_CONTENT  // Adjust height as needed
        );
        checkboxParams.gravity = Gravity.CENTER;  // Center the checkbox

        kp_bool.setLayoutParams(checkboxParams);
        // Add the checkbox to the layout
        layout.addView(kp_bool);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            if (firstChar == '+') {
                if (kp && kp_bool.isChecked()) {
                    if (quanshed) {
                        totalTeam1 += 16 + (roundNumberInt * 2) + roundTerz1;
                    } else if (sharped) {
                        totalTeam1 += 16 + (roundNumberInt * 4) + roundTerz1;
                    } else {
                        totalTeam1 += 25 + roundNumberInt + roundTerz1;
                        totalTeam2 += roundTerz2;
                    }
                } else if (kp && !kp_bool.isChecked()) {
                    if (quanshed) {
                        totalTeam2 += 16 + (roundNumberInt * 2) + roundTerz2 + roundTerz1;
                    } else if (sharped) {
                        totalTeam2 += 16 + (roundNumberInt * 4) + roundTerz2 + roundTerz1;
                    } else {
                        totalTeam2 += roundNumberInt + 16 + roundTerz1 + roundTerz2;
                    }
                }
            } else {
                if (kp && kp_bool.isChecked()) {
                    if (quanshed) {
                        totalTeam2 += 25 + (roundNumberInt * 2) + roundTerz2;
                    } else if (sharped) {
                        totalTeam2 += 25 + (roundNumberInt * 4) + roundTerz2;
                    } else {
                        totalTeam2 += 25 + roundNumberInt + roundTerz2;
                        totalTeam1 += roundTerz1;
                    }
                } else if (kp && !kp_bool.isChecked()) {
                    if (quanshed) {
                        totalTeam1 += 16 + (roundNumberInt * 2) + roundTerz2 + roundTerz1;
                    } else if (sharped) {
                        totalTeam1 += 16 + (roundNumberInt * 4) + roundTerz2 + roundTerz1;
                    } else {
                        totalTeam1 += roundNumberInt + 16 + roundTerz1 + roundTerz2;
                    }
                }
            }

            you.setText(String.valueOf(totalTeam2));
            we.setText(String.valueOf(totalTeam1));

            quanshed = false;
            sharped = false;
            roundNumber.setTextColor(RED);
            roundNumber.setEnabled(true);
            roundNumber.setClickable(true);
            roundScore.setVisibility(VISIBLE);
            textView1.setTextColor(BLACK);
            textView1.setEnabled(false);
            textView1.setClickable(false);
            textView4.setTextColor(BLACK);
            textView4.setEnabled(false);
            textView4.setClickable(false);
            quansh.setEnabled(false);
            quansh.setClickable(false);
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.show();
    }
}
