package com.example.wordgame;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Game {
    public enum Result {HIT, MISS, ALREADY_USED, INVALID}

    private final String secretWord;
    private final Set<Character> usedLetters = new HashSet<>();
    private final Set<Character> hits = new HashSet<>();
    private int remainingAttempts;
    private boolean over;
    private boolean win;

    public Game(String secretWord, int maxAttempts) {
        if (secretWord == null || secretWord.isBlank()) throw new IllegalArgumentException("問題の単語が必要です");
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts must be >= 1");

        this.secretWord = secretWord.toLowerCase(Locale.ROOT);
        this.remainingAttempts = maxAttempts;

        for (char c : this.secretWord.toCharArray()) {
            if (!Character.isLetter(c)) hits.add(c);
        }
        updateOverState();
    }

    // 一致を判定する処理
    public Result guess(char raw) {
        char c = Character.toLowerCase(raw);
        if (!Character.isLetter(c)) return Result.INVALID;
        if (over) return Result.INVALID;

        if (!usedLetters.add(c)) {
            return Result.ALREADY_USED;
        }

        if (secretWord.indexOf(c) >= 0) {
            hits.add(c);
            updateOverState();
            return Result.HIT;
        } else {
            remainingAttempts--;
            updateOverState();
            return Result.MISS;
        }
    }

    // ゲームの終了状態を判定する処理
    private void updateOverState() {
        if (remainingAttempts <= 0) {
            over = true;
            win = false;
            return;
        }
        boolean allRevealed = true;
        for (char c : secretWord.toCharArray()) {
            if (Character.isLetter(c) && !hits.contains(c)) {
                allRevealed = false;
                break;
            }
        }
        if (allRevealed) {
            over = true;
            win = true;
        }
    }

    // マスクした単語を返す処理(問題文)
    public String getMaskedWord() {
        StringBuilder sb = new StringBuilder();
        for (char c : secretWord.toCharArray()) {
            if (!Character.isLetter(c)) {
                sb.append(c);
            } else if (hits.contains(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
        }
        return sb.toString();
    }

    // すでに利用した単語のリスト
    public List<String> getUsedLettersSorted() {
        return usedLetters.stream()
                .map(String::valueOf)
                .sorted()
                .toList();
    }

    public boolean isOver() {
        return over;
    }

    public boolean isWin() {
        return win;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public String getSecretWord() {
        return secretWord;
    }
}
