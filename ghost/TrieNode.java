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

import java.util.HashMap;


public class TrieNode {
    private HashMap<Character, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<Character, TrieNode> temp = children;
        for (int i = 0; i < s.length(); i++){
            if (!temp.containsKey(s.charAt(i))){
                temp.put(s.charAt(i), new TrieNode());
            }
            if (i == s.length() - 1){
                temp.get(s.charAt(i)).isWord = true;
            }
            temp = temp.get(s.charAt(i)).children;
        }
    }

    private TrieNode searchTries(String s){
        TrieNode temp = this;
        for (int i = 0; i < s.length(); i++){
            if (!temp.children.containsKey(s.charAt(i))){
                return null;
            }
            temp = temp.children.get(s.charAt(i));
        }
        return temp;
    }


    public boolean isWord(String s) {
        TrieNode temp = searchTries(s);
        if (temp == null)
            return false;
        else
            return temp.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        TrieNode temp = searchTries(s);
        if (temp == null){
            return null;
        }
        while (!temp.isWord){
            for (Character c: temp.children.keySet()){
                temp = temp.children.get(c);
                s += c;
                break;
            }
        }
        return s;
    }


    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
