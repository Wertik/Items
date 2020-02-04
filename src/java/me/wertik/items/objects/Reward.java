package me.wertik.items.objects;

import java.util.ArrayList;
import java.util.List;

public class Reward {

    private List<String> consoleCommands = new ArrayList<>();
    private List<String> playerCommands = new ArrayList<>();
    private List<String> playerOPCommands = new ArrayList<>();

    public List<String> getConsoleCommands() {
        return consoleCommands;
    }

    public void setConsoleCommands(List<String> consoleCommands) {
        this.consoleCommands = consoleCommands;
    }

    public List<String> getPlayerCommands() {
        return playerCommands;
    }

    public void setPlayerCommands(List<String> playerCommands) {
        this.playerCommands = playerCommands;
    }

    public List<String> getPlayerOPCommands() {
        return playerOPCommands;
    }

    public void setPlayerOPCommands(List<String> playerOPCommands) {
        this.playerOPCommands = playerOPCommands;
    }
}
