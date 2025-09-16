package com.example.wordgame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 難易度選択で適切な出力と戻り値が得られることを検証する単体テスト。
 */
public class DifficultySelectionTest {
    private PrintStream originalOut;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private Difficulty invokeAskDifficultyWithInput(String input) throws Exception {
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        Method method = Main.class.getDeclaredMethod("askDifficulty", Scanner.class);
        method.setAccessible(true);
        return (Difficulty) method.invoke(null, sc);
    }

    private String capturedOut() {
        return baos.toString(StandardCharsets.UTF_8);
    }

    private void assertPromptWasPrinted() {
        String out = capturedOut();
        assertTrue(out.contains("難易度選択: [E]asy / [N]ormal / [H]ard (デフォルト: Normal)"),
                "難易度選択のプロンプトが出力されているはず");
    }

    @Test
    void 入力がEでEASYが選択される() throws Exception {
        Difficulty d = invokeAskDifficultyWithInput("E\n");
        assertEquals(Difficulty.EASY, d);
        assertPromptWasPrinted();
    }

    @Test
    void 入力がeasyでEASYが選択される() throws Exception {
        Difficulty d = invokeAskDifficultyWithInput("easy\n");
        assertEquals(Difficulty.EASY, d);
        assertPromptWasPrinted();
    }

    @Test
    void 入力がHでHARDが選択される() throws Exception {
        Difficulty d = invokeAskDifficultyWithInput("H\n");
        assertEquals(Difficulty.HARD, d);
        assertPromptWasPrinted();
    }

    @Test
    void 入力がhardでHARDが選択される() throws Exception {
        Difficulty d = invokeAskDifficultyWithInput("hard\n");
        assertEquals(Difficulty.HARD, d);
        assertPromptWasPrinted();
    }

    @Test
    void 入力が空ならデフォルトでNORMALが選択される() throws Exception {
        Difficulty d = invokeAskDifficultyWithInput("\n");
        assertEquals(Difficulty.NORMAL, d);
        assertPromptWasPrinted();
    }

    @Test
    void 不明な入力ならデフォルトでNORMALが選択される() throws Exception {
        Difficulty d = invokeAskDifficultyWithInput("x\n");
        assertEquals(Difficulty.NORMAL, d);
        assertPromptWasPrinted();
    }
}
