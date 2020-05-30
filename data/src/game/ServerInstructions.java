package game;

import javax.swing.*;

public final class ServerInstructions {
    public static void show() {
        JPanel panel = new JPanel();

        JTextArea instructions = new JTextArea();
        instructions.setText("Welcome to BoomerParty\n\n" +
                "This is the server, you cannot play but you host the game for clients to connect to.\n\n" +
                "You can start the game when there are enough players and there is a log.\n\n" +
                "Finally, you can also get a hint for the current prompt in the game if nobody can figure out a word for it."
        );
        instructions.setEditable(false);
        panel.add(instructions);

        JOptionPane.showMessageDialog(null, panel, "BoomerParty", JOptionPane.PLAIN_MESSAGE);

    }
}
