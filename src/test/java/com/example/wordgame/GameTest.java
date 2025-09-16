package com.example.wordgame;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    void ゲームで正解するケース() {
        Game g = new Game("apple", 5);
        assertEquals("_____", g.getMaskedWord());
        assertEquals(Game.Result.HIT, g.guess('p'));
        assertEquals("_pp__", g.getMaskedWord());
        assertEquals(Game.Result.MISS, g.guess('u'));
        assertEquals(4, g.getRemainingAttempts());
        assertEquals(Game.Result.HIT, g.guess('a'));
        assertEquals("app__", g.getMaskedWord());
        assertEquals(Game.Result.HIT, g.guess('l'));
        assertEquals(Game.Result.HIT, g.guess('e'));
        assertTrue(g.isOver());
        assertTrue(g.isWin());
    }

    @Test
    void ゲームで負けるケース() {
        Game g = new Game("cat", 2);
        assertEquals(Game.Result.MISS, g.guess('z'));
        assertEquals(Game.Result.MISS, g.guess('q'));
        assertTrue(g.isOver());
        assertFalse(g.isWin());
    }

    @Test
    void 入力済みのアルファベットを再度入力するケース() {
        Game g = new Game("cat", 5);
        assertEquals(Game.Result.HIT, g.guess('c'));
        assertEquals(Game.Result.ALREADY_USED, g.guess('c'));
    }

    @Test
    void 二文字以上を与えるテストケース() throws Exception {
        // Main.promptLetter は 1 文字以外の入力を受け付けず再入力を促す仕様
        String input = "ab\nZ\n"; // 最初は2文字、次に有効な1文字
        Scanner sc = new Scanner(new ByteArrayInputStream(
                input.getBytes(StandardCharsets.UTF_8)));

        Method method = Main.class.getDeclaredMethod("promptLetter", Scanner.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, sc);

        // 無効な2文字入力は拒否され、その後の有効入力(Z)が小文字化されて返ることを検証
        assertEquals("z", result);
    }

    @Test
    void 無効入力で警告メッセージが出力されること() throws Exception {
        // 標準出力をキャプチャ
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        try {
            System.setOut(ps);
            // 入力: 無効「ab」の後に有効「Z」
            String input = "ab\nZ\n";
            Scanner sc = new Scanner(new ByteArrayInputStream(
                    input.getBytes(StandardCharsets.UTF_8)));

            Method method = Main.class.getDeclaredMethod("promptLetter", Scanner.class);
            method.setAccessible(true);
            String result = (String) method.invoke(null, sc);

            // 出力に警告が含まれることを確認（プロンプトと警告が連結されて出力される）
            String out = baos.toString(StandardCharsets.UTF_8);
            assertTrue(out.contains("アルファベットを入力してください: Warn:英語のアルファベット(A-Z)を1文字だけ入力してください(大文字小文字は関係なし)"));
            // 返却値も確認
            assertEquals("z", result);
        } finally {
            System.setOut(originalOut);
        }
    }
}
