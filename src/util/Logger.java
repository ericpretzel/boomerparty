package util;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final StringBuilder log = new StringBuilder();

    private static final JTextArea display = new JTextArea(15, 75);

    private Logger() {
    }

    public static JScrollPane scrollPane() {
        display.setEditable(false);
        display.setBorder(new EtchedBorder(EtchedBorder.RAISED));
        display.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        display.setForeground(Color.WHITE);
        display.setBackground(Color.BLACK);
        display.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setHorizontalScrollBar(null);
        return scrollPane;
    }

    public static void log(String toLog) {
        String timestamp = new SimpleDateFormat("[HH:mm:ss] ").format(new Date());
        System.out.println(timestamp + toLog);
        log.append(timestamp).append(toLog).append("\n");
        display.append(timestamp + toLog + "\n");
    }

    public static void log(Object toLog) {
        log(toLog.toString());
    }

    public static String getLog() {
        return log.toString();
    }
}
