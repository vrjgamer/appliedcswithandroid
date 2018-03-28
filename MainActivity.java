package com.v.vrjco.scarnesdice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by VRUSHABH on 06-01-2018.
 */

public class MainActivity extends AppCompatActivity {

    private TextView tvUserScore, tvComputerScore,
            tvStatus;
    private ImageView imDiceFace;
    private Button bRoll, bHold, bReset;

    private int[] diceFaceImages = {
            R.drawable.dice1,
            R.drawable.dice2,
            R.drawable.dice3,
            R.drawable.dice4,
            R.drawable.dice5,
            R.drawable.dice6
    };

    private int userOverallScore = 0, userTurnScore = 0;
    private int computerOverallScore = 0, computerTurnScore = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkAllViews();

        bRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollButtonClick();
            }
        });

        bHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdButtonClick();
            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButtonClick();
            }
        });



    }

    private void linkAllViews() {
        tvUserScore = findViewById(R.id.user_score);
        tvComputerScore = findViewById(R.id.computer_score);
        tvStatus = findViewById(R.id.play_status);
        imDiceFace = findViewById(R.id.dice_face);
        bRoll = findViewById(R.id.roll_button);
        bHold = findViewById(R.id.hold_button);
        bReset = findViewById(R.id.reset_button);
    }


    public void rollButtonClick() {
        int rolledNumber = rollDice();
        imDiceFace.setImageResource(diceFaceImages[rolledNumber]);
        rolledNumber++;

        if (rolledNumber == 1) {
            userTurnScore = 0;
            tvStatus.setText("You rolled a '1'");
            computerTurn();
        } else {
            userTurnScore += rolledNumber;
            tvStatus.setText(String.format("Your turn score is %d", userTurnScore));
        }

    }

    public void holdButtonClick() {
        userOverallScore += userTurnScore;
        userTurnScore = 0;

        tvUserScore.setText(String.format("Your Score: %d", userOverallScore));
        if (!checkWinner())
            computerTurn();
    }


    private void computerTurn() {
        enableButtons(false);

        while (true) {
            int computerRolledNumber = rollDice();
            imDiceFace.setImageResource(diceFaceImages[computerRolledNumber]);
            computerRolledNumber++;

            if (computerRolledNumber == 1) {
                computerTurnScore = 0;
                tvStatus.setText("Computer rolled a '1'");
                tvStatus.setText("Now Player's turn");
                enableButtons(true);
                break;
            } else {
                computerTurnScore += computerRolledNumber;
                tvStatus.setText(String.format("Computer turn score is %d", computerTurnScore));
            }

            if (computerTurnScore > 20) {
                computerOverallScore += computerTurnScore;
                computerTurnScore = 0;
                tvComputerScore.setText(String.format("Computer Score: %d", computerOverallScore));
                tvStatus.setText("Now Player's turn");
                enableButtons(true);
                break;
            }
        }

        checkWinner();
    }


    public boolean checkWinner() {
        if (userOverallScore >= 100) {
            Toast.makeText(getApplicationContext(), "You Win!", Toast.LENGTH_SHORT).show();
            resetButtonClick();
            return true;
        } else if (computerOverallScore >= 100) {
            Toast.makeText(getApplicationContext(), "Computer Wins!", Toast.LENGTH_SHORT).show();
            resetButtonClick();
            return true;
        }
        return false;
    }

    public void resetButtonClick() {
        userOverallScore = 0;
        userTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;

        tvUserScore.setText("Your Score: 0");
        tvComputerScore.setText("Computer Score: 0");
        tvStatus.setText("Status: Game Start");

        enableButtons(true);
    }

    private int rollDice() {
        Random random = new Random();
        return random.nextInt(6);
    }

    private void enableButtons(boolean isEnabled) {
        bRoll.setEnabled(isEnabled);
        bHold.setEnabled(isEnabled);
    }

}
