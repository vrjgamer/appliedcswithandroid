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

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final String TAG = AnagramDictionary.class.getSimpleName();

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;

	//Milestone 1
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
	//Milestone 2
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
	//extension
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
			//Milestone 1
            wordList.add(word);
            wordSet.add(word);
			//Milestone 2
            String str = sortLetters(word);
            if (lettersToWord.containsKey(str)) {
                lettersToWord.get(str).add(word);
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                lettersToWord.put(str, list);
            }
			//extension
            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                ArrayList<String> list = new ArrayList<>();
                list.add(word);
                sizeToWords.put(word.length(), list);
            }
        }

    }

    public boolean isGoodWord(String word, String base) {
        boolean valid = ((wordSet.contains(word)) && (!word.contains(base)));
        Log.d(TAG, base + " " + word + " valid: " + !valid);
        return valid;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
		for (char c = 'a'; c <= 'z'; c++) {
            String str = sortLetters(targetWord + c);
            if (lettersToWord.containsKey(str)) {
                result.addAll(lettersToWord.get(str));
            }
        }
        Log.d(TAG, result.toString());
        return result;
    }

    private String sortLetters(String str) {
        char[] chary = str.toCharArray();
        char temp;
        for (int i = 0; i < str.length(); i++) {
            for (int j = i; j < str.length(); j++) {
                if (chary[i] > chary[j]) {
                    temp = chary[i];
                    chary[i] = chary[j];
                    chary[j] = temp;
                }
            }
        }
//        Log.d(TAG, new String(chary));
        return new String(chary);
    }


    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        word = word.toLowerCase();
        for (char c = 'a'; c <= 'z'; c++) {
            String str = sortLetters(word + c);
            if (lettersToWord.containsKey(str)) {
                result.addAll(lettersToWord.get(str));
            }
        }
        Log.d(TAG, result.toString());
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> list = sizeToWords.get(wordLength);
        for (int i = random.nextInt(list.size()); i < list.size(); i++) {
            String word = list.get(i);
            String sort = sortLetters(word);
            if (lettersToWord.get(sort).size() >= MIN_NUM_ANAGRAMS) {
                Log.d(TAG, word);
                if (wordLength < MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return word;
            }
        }
       return "POST";
    }
}
