package game;

import server.ClientHandler;
import util.Globals;
import util.HashMap;
import util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameManager implements Runnable {
    public final HashMap<Player, ClientHandler> clients = new HashMap<>();
    public final Game game = new Game();
    private final WordManager wm = new WordManager();
    private Player currentPlayer;

    public void addPlayer(ClientHandler client) {
        Player player = new Player(client.username);
        game.players.add(player);
        clients.put(player, client);
        broadcast();
    }

    public void removePlayer(ClientHandler client) {
        Player player = new Player(client.username);
        if (player.equals(currentPlayer)) {
            currentPlayer.health = 0;
            nextPlayer();
            return;
        } else {
            game.players.remove(player);
        }
        clients.put(player, null);
        broadcast();
    }

    public void run() {
        //add existing clients from previous game if applicable
        for (Player player : clients.getKeys()) {
            if (!game.players.contains(player) && clients.get(player).connected()) {
                game.players.add(player);
            }
        }

        //starting values of player
        for (Player player : game.players) {
            player.health = 2;
            player.playedWord = "";
            player.myTurn = false;
            player.bonusLetters.clear();
        }

        //starting values of game
        game.isRunning = true;
        game.prompt = wm.getPrompt();
        currentPlayer = game.players.get(0);
        currentPlayer.myTurn = true;

        startNewTimer();
    }

    /**
     * starts a new timer, defaulting to 10 seconds
     */

    private synchronized void startNewTimer() {
        game.timer = 10;

        ScheduledExecutorService timerThread = Executors.newSingleThreadScheduledExecutor();
        ExecutorService inputThread = Executors.newSingleThreadExecutor();

        timerThread.scheduleAtFixedRate(() -> {
            game.timer--;
            if (game.timer < 0) {
                currentPlayer.health--;
                game.soundToPlay = Globals.LOSE_SOUND;
                nextPlayer();
                inputThread.shutdown();
                timerThread.shutdown();
            } else {
                broadcast();
            }
        }, 0, 1, TimeUnit.SECONDS);

        inputThread.execute(() -> {
            final ClientHandler client = clients.get(currentPlayer);
            while (!timerThread.isShutdown()) {
                if (!client.ready()) {
                    continue;
                }

                String word = client.receive();
                Logger.log(word);
                currentPlayer.playedWord = word;

                //a word marked as ready for checking will be marked with an exclamation point at the end.
                if (word.length() > 0 && word.charAt(word.length() - 1) == '!') {

                    word = word.substring(0, word.length() - 1);
                    currentPlayer.playedWord = word;

                    if (wm.check(word)) {
                        game.soundToPlay = Globals.VALID_WORD_SOUND;

                        word.chars().forEach(c -> {
                            String letter = String.valueOf((char)c);
                            if (Globals.BONUS_LETTERS.contains(letter)) {
                                currentPlayer.bonusLetters.add(letter);
                            }
                        });
                        if (currentPlayer.bonusLetters.size() == Globals.BONUS_LETTERS.length()) {
                            currentPlayer.health = Math.min(3, currentPlayer.health + 1);
                            currentPlayer.bonusLetters.clear();
                        }

                        inputThread.shutdown();
                        timerThread.shutdown();
                        nextPlayer();
                        break;
                    } else {
                        game.soundToPlay = Globals.INVALID_WORD_SOUND;
                    }
                }
                broadcast();
            }
        });
    }

    private synchronized void nextPlayer() {
        game.players.remove(currentPlayer);
        currentPlayer.myTurn = false;
        if (currentPlayer.health > 0)
            game.players.add(currentPlayer);

        game.isRunning = game.players.size() >= 2;
        if (game.isRunning) {
            currentPlayer = game.players.get(0); //new currentPlayer
            currentPlayer.myTurn = true;
            currentPlayer.playedWord = "";
            game.prompt = wm.getPrompt();

            startNewTimer();
        } else {
            broadcast();
        }
    }

    /**
     * sends the game object to each player
     */
    private synchronized void broadcast() {
        for (Player p : clients.getKeys()) {
            ClientHandler client = clients.get(p);
            if (client.connected()) {
                client.send(game);
            } else {
                removePlayer(client);
            }
            game.soundToPlay = "";
        }
    }

    public String getHint() {
        return wm.getHint();
    }
}
