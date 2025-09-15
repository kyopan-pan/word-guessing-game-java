package com.example.wordgame;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== 英単語推測ゲーム！！ ===");
        Difficulty difficulty = askDifficulty(scanner);

        WordProvider provider = new WordProvider();
        String secret = provider.pickRandomWord(difficulty);
        Game game = new Game(secret, difficulty.getMaxAttempts());

        while (!game.isOver()) {
            System.out.println(game.getMaskedWord());
            System.out.println("残り試行可能回数: " + game.getRemainingAttempts());
            System.out.println("使用済みのアルファベット: " + String.join(", ", game.getUsedLettersSorted()));

            String guess = promptLetter(scanner);
            Game.Result result = game.guess(guess.charAt(0));
            switch (result) {
                case ALREADY_USED -> System.out.println("すでに利用した単語です");
                case HIT -> System.out.println("当たり");
                case MISS -> System.out.println("不正解");
                case INVALID -> System.out.println("無効な入力です");
            }
        }

        System.out.println();
        // 勝敗判定のフラグが立っていればゲームを終了
        if (game.isWin()) {
            System.out.println("正解です! Answer: " + game.getSecretWord());
        } else {
            System.out.println("ゲームオーバー! Answer: " + game.getSecretWord());
        }
    }

    private static Difficulty askDifficulty(Scanner scanner) {
        //  難易度選択を行わせる
        System.out.println("難易度選択: [E]asy / [N]ormal / [H]ard (デフォルト: Normal)");
        String line = scanner.nextLine().trim();
        line = line.toLowerCase(Locale.ROOT);
        return switch (line) {
            case "e", "easy" -> Difficulty.EASY;
            case "h", "hard" -> Difficulty.HARD;
            default -> Difficulty.NORMAL;
        };
    }

    private static String promptLetter(Scanner scanner) {
        // 問題回答の入力を行う部分
        while (true) {
            System.out.print("アルファベットを入力してください: ");
            String line = scanner.nextLine().trim();
            // 入力規約を満たしている場合に
            if (line.length() == 1 && Character.isLetter(line.charAt(0))) {
                return line.toLowerCase(Locale.ROOT);
            }
            System.out.println("Warn:英語のアルファベット(A-Z)を1文字だけ入力してください(大文字小文字は関係なし)");
        }
    }
}
