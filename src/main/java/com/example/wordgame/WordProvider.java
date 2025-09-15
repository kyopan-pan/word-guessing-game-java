package com.example.wordgame;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WordProvider {
    private final List<String> words = new ArrayList<>();
    private final Random random = new SecureRandom();

    public WordProvider() {
        // 単語リストの初期化
        String[] seed = {
                "apple", "bridge", "cat", "dolphin", "elephant",
                "forest", "guitar", "harbor", "island", "jacket",
                "kitchen", "library", "mountain", "notebook", "ocean",
                "planet", "quarter", "rainbow", "station", "thunder"
        };
        for (String s : seed) words.add(s.toLowerCase(Locale.ROOT));
    }

    // 難易度に応じた単語をランダムに選択する
    public String pickRandomWord(Difficulty difficulty) {
        List<String> pool = words.stream()
                .filter(w -> w.length() >= difficulty.getMinLen() && w.length() <= difficulty.getMaxLen())
                .toList();
        if (pool.isEmpty()) pool = words; // fallback
        return pool.get(random.nextInt(pool.size()));
    }

    public List<String> getAllWords() {
        return List.copyOf(words);
    }
}
