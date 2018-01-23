/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";

    private static final String USER_SCORE = "User Score: ";
    private static final String COMP_SCORE = "Computer Score: ";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private TextView txtWord, txtLabel, txtUserScore, txtComputerScore;
    private Button btnChallenge, btnRestart;
    private int scoreUser = 0, scoreComputer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        initializeViews();
        onStart(null);
        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengeHandler();
            }
        });

        // restart button listener
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart(null);
            }
        });
    }

//    private void challengeHandler() {
//        /**
//         * Your code goes here
//         * **/
//    }


    private void challengeHandler() {
        // handles when user presses challenge button
        String word = txtWord.getText().toString();
        String nextWord;
        if (word.length() >= 4 && dictionary.isWord(word)) {
            endGame(true, word + " is a valid word");
        } else {
            nextWord = dictionary.getAnyWordStartingWith(word);
            if (nextWord != null) {
                endGame(false, word + " is a prefix of word \"" + nextWord + "\"");
            } else {
                endGame(true, word + " is not a prefix of any word");
            }
        }
    }

    private void computerTurn() {
        String text = txtWord.getText().toString().trim();
        if (text.length() >= 4 && dictionary.isWord(text)) {
            endGame(false, text + " is a valid word");
            return;
        } else {
            String nextWord = dictionary.getAnyWordStartingWith(text);
            if (nextWord == null) {
                endGame(false, text + " is not a prefix of any word");
                return;
            } else {
                addTextToGame(nextWord.charAt(text.length()));
            }
        }
        userTurn = true;
        txtLabel.setText(USER_TURN);
    }

//
//    private void computerTurn() {
//        TextView label = (TextView) findViewById(R.id.gameStatus);
//        // Do computer turn stuff then make it the user's turn again
//        userTurn = true;
//        label.setText(USER_TURN);
//    }

    private void initializeViews() {
        btnChallenge = findViewById(R.id.bChallenge);
        btnRestart = findViewById(R.id.bRestart);
        txtWord = findViewById(R.id.ghostText);
        txtLabel = findViewById(R.id.gameStatus);
        txtUserScore = findViewById(R.id.scoreUser);
        txtComputerScore = findViewById(R.id.scoreComputer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     *
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = findViewById(R.id.ghostText);
        text.setText("");
        TextView label = findViewById(R.id.gameStatus);
        text.setTextColor(Color.BLACK);
        btnChallenge.setEnabled(true);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORD", txtWord.getText().toString());
        outState.putString("LABEL", txtLabel.getText().toString());
        outState.putInt("SCORE_USER", scoreUser);
        outState.putInt("SCORE_COMPUTER", scoreComputer);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            txtWord.setText(savedInstanceState.getString("WORD"));
            txtLabel.setText(savedInstanceState.getString("LABEL"));
            userTurn = true;  // a saved state is always user turn
            scoreUser = savedInstanceState.getInt("SCORE_USER");
            scoreComputer = savedInstanceState.getInt("SCORE_COMPUTER");
            updateScoresOnBoard();
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char c = (char) event.getUnicodeChar();
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            addTextToGame(c);
            txtLabel.setText(COMPUTER_TURN);
            userTurn = false;
            computerTurn();
        } else {
            (Toast.makeText(this, "Please enter a valid character", Toast.LENGTH_LONG)).show();
        }
        return super.onKeyUp(keyCode, event);
    }

    private void addTextToGame(char character) {
        txtWord.setText(txtWord.getText().toString() + character);
    }

    private void updateScoresOnBoard() {
        txtUserScore.setText(USER_SCORE + scoreUser);
        txtComputerScore.setText(COMP_SCORE + scoreComputer);
    }

    private void endGame(boolean win, String suffix) {
        if (win) {
            txtLabel.setText("User Wins. " + suffix);
            scoreUser += 1;
        } else {
            txtLabel.setText("Computer Wins. " + suffix);
            scoreComputer += 1;
        }
        txtWord.setTextColor(Color.GRAY);
        btnChallenge.setEnabled(false);
        updateScoresOnBoard();
    }

}
