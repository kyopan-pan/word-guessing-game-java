package com.example.wordgame;

public enum Difficulty {
    //  それぞれの難易度の設定
    // 最大試行回数, 最小単語長, 最大単語長
    EASY(7, 3, 5),
    NORMAL(5, 4, 8),
    HARD(4, 6, 12);

    private final int maxAttempts;
    private final int minLen;
    private final int maxLen;

    Difficulty(int maxAttempts, int minLen, int maxLen) {
        this.maxAttempts = maxAttempts;
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getMinLen() {
        return minLen;
    }

    public int getMaxLen() {
        return maxLen;
    }
}
