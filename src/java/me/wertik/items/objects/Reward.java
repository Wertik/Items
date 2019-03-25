package me.wertik.items.objects;

import java.util.List;

public class Reward {

    private List<String> consoleCommands;
    private List<String> playerCommands;

    public Reward(List<String> consoleCommands, List<String> playerCommands) {
        this.consoleCommands = consoleCommands;
        this.playerCommands = playerCommands;
    }

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
}
