package com.example.wordgame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordProviderTest {
    @Test
    void 英単語が10以上登録されているか() {
        WordProvider wp = new WordProvider();
        assertTrue(wp.getAllWords().size() >= 10);
    }

    @Test
    void 難易度に応じた長さの文字列を返せるか() {
        WordProvider wp = new WordProvider();
        String w = wp.pickRandomWord(Difficulty.HARD);
        assertTrue(w.length() >= Difficulty.HARD.getMinLen() && w.length() <= Difficulty.HARD.getMaxLen());
    }
}
