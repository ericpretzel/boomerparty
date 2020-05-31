package game;

import util.DLList;
import util.HashTable;
import util.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordManager {

    private static final int numLines = 273278;
    private static final HashTable<String> dictionary = new HashTable<>((int) (numLines * 1.25));

    static /*loadWords*/ {
        JFrame fr = new JFrame();
        JProgressBar bar = new JProgressBar(JProgressBar.CENTER, 0, numLines);
        bar.setBorder(new TitledBorder("Loading words.txt..."));
        fr.add(bar);
        fr.pack();
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        fr.setVisible(true);
        new BufferedReader(new InputStreamReader(WordManager.class.getResourceAsStream("words.txt")))
                .lines()
                .forEach(word -> {
                    dictionary.add(word.toUpperCase());
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
        if (word.contains(promptQueue.get(0)) &&
                !usedWords.contains(word) &&
                dictionary.contains(word)) {
            Logger.log(word + " is valid");
            usedWords.add(word);
            promptQueue.remove(0);
            return true;
        }
        Logger.log(word + " is not valid");
        return false;
    }

    private final ExecutorService promptThreadPool = Executors.newFixedThreadPool(3);
    public String getPrompt() {
        if (promptQueue.size() == 0)
            generatePrompt();
        for (int i = promptQueue.size(); i < 3; i++)
            promptThreadPool.execute(this::generatePrompt);
        return promptQueue.get(0);
    }

    public String getHint() {
        for (String word : dictionary) {
            if (word.contains(promptQueue.get(0)) && !usedWords.contains(word))
                return word;
        }
        return "";
    }

    private void generatePrompt() {
        String prompt;
        int promptLength = (int) (2 + Math.random() * 2);
        do {
            prompt = "";
            for (int i = 1; i <= promptLength; i++) {
                prompt = prompt.concat(String.valueOf(alphabet.charAt((int) (Math.random() * alphabet.length()))));
            }
        } while (!promptIsValid(prompt));
        synchronized(promptQueue) {
            promptQueue.add(prompt);
        }
    }
}
