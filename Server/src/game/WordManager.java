package game;

import util.DLList;
import util.HashTable;
import util.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WordManager {

    private static final HashTable<String> dictionary = new HashTable<>(750000);

    static /*loadWords*/ {
        final int num = 450827; //this is the number of lines in words.txt

        JFrame fr = new JFrame();
        JProgressBar bar = new JProgressBar(JProgressBar.CENTER, 0, num);
        bar.setBorder(new TitledBorder("Loading words.txt..."));
        fr.add(bar);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fr.setVisible(true);
        new BufferedReader(new InputStreamReader(WordManager.class.getResourceAsStream("words.txt")))
                .lines()
                .forEach(word -> {
                    dictionary.add(word);
                    bar.setValue(bar.getValue() + 1);
                    bar.repaint();
                });
        fr.dispose();
    }

    private final HashTable<String> usedWords = new HashTable<>();
    private final DLList<String> promptQueue = new DLList<>();
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    {
        generatePrompt();
    }

    private static boolean promptIsValid(String prompt) {
        int counter = 0;
        for (String word : dictionary) {
            if (word.contains(prompt) && ++counter >= 500)
                return true;
        }
        return false;
    }

    public boolean check(String word) {
        if (wordIsValid(word)) {
            Logger.log(word + " is valid");
            usedWords.add(word);
            promptQueue.remove(0);
            return true;
        }
        Logger.log(word + " is not valid");
        return false;
    }

    private boolean wordIsValid(String word) {
        return word.contains(promptQueue.get(0)) &&
                !usedWords.contains(word) &&
                dictionary.contains(word);
    }

    public String getPrompt() {
        if (promptQueue.size() == 0)
            generatePrompt();
        new Thread(() -> {
            while (promptQueue.size() < 3)
                generatePrompt();
        }).start();
        Logger.log("Prompt: " + promptQueue.get(0));
        return promptQueue.get(0);
    }

    private void generatePrompt() {
        StringBuilder prompt;
        int promptLength = (int) (2 + Math.random() * 2);
        do {
            prompt = new StringBuilder();
            for (int i = 1; i <= promptLength; i++) {
                prompt.append(alphabet.charAt((int) (Math.random() * alphabet.length())));
            }
        } while (!promptIsValid(prompt.toString()));
        promptQueue.add(prompt.toString());
    }
}
