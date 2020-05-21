package game;

import java.util.Objects;

public class Player implements java.io.Serializable {

    public final String username;
    public int health;
    public String playedWord;

    public Player(String username) {
        this.username = username;
        this.health = 2;
        this.playedWord = "";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(player.username, this.username);
    }

    public int hashCode() {
        return username.hashCode();
    }

    public String toString() {
        return this.username;
    }
}
