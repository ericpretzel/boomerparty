package game;

import javax.swing.*;

public final class ClientInstructions {

    public static void show() {
        JPanel panel = new JPanel();

        JTextArea instructions = new JTextArea();
        instructions.setText("Welcome to BoomerParty\n\n" +
                "How to play:\n" +
                "When it is your turn, you will be given a prompt of 2-3 letters. You must type a word containing the prompt.\n\n" +
                "Example: type a word containing \"WE\"\n" +
                "You could type \"weed\", \"wet\", or \"mowed\" (or many other words) to satisfy this prompt.\n\n" +
                "You have ten seconds to answer the prompt, or you will lose a life. You can gain a life by answering prompts with words " +
                "containing most letters of the alphabet. If you lose all your lives, you are out of the game.");
        instructions.setEditable(false);
        panel.add(instructions);

        JOptionPane.showMessageDialog(null, panel, "BoomerParty", JOptionPane.PLAIN_MESSAGE);

    }
}
