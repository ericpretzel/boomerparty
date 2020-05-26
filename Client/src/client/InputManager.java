package client;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager extends KeyAdapter {

    private final JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final ClientManager client;
    private String word = "";

    public InputManager(ClientManager client) {
        textPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        textPanel.setPreferredSize(textPanel.getSize());
        this.client = client;
    }

    public void keyPressed(KeyEvent e) {
        //check if you should be able to type...
        if (!client.game.isRunning || !client.username.equals(client.game.currentPlayer().username)) return;

        //on letter press: add to the current play.
        if (Character.isAlphabetic(e.getKeyChar())) {
            String letter = String.valueOf(e.getKeyChar()).toUpperCase();
            word = word.concat(letter);
        }

        //on enter: sends the word for the game to check if it is valid or not.
        else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            client.send(word);
            this.word = "";
        }

        //on backspace: remove letter
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (word.length() > 0)
                word = word.substring(0, word.length() - 1);
        }

        textPanel.removeAll();

        //add the letters to the display panel, and make the letters containing the prompt turn green.
        int promptIndex = word.indexOf(client.game.prompt);
        for (int i = 0; i < word.length(); i++) {
            JLabel label = new JLabel(word.substring(i, i + 1));
            label.setFont(new Font(Font.DIALOG, Font.BOLD, 15));
            label.setHorizontalAlignment(JLabel.CENTER);
            if (promptIndex >= 0 && i >= promptIndex && i < promptIndex + client.game.prompt.length()) {
                label.setForeground(Color.green);
            }
            textPanel.add(label);
        }
        textPanel.revalidate();
        textPanel.repaint();
    }

    public JPanel getTextPanel() {
        return textPanel;
    }
}
