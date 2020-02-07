package space.devport.wertik.items.objects;

import lombok.Getter;
import lombok.Setter;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reward {

    // Commands executed by console
    @Getter
    @Setter
    private List<String> commands = new ArrayList<>();

    // Message broadcasted to the whole server
    @Getter
    @Setter
    private List<String> broadcastMessage = new ArrayList<>();

    // Message sent to the player
    @Getter
    @Setter
    private List<String> informMessage = new ArrayList<>();

    // Reward a player
    public void reward(Player player) {

        // Loop through commands and find prefixes
        for (String cmd : commands) {

            // Parse command to fill in placeholders
            cmd = Utils.parse(cmd, player);

            // Execute based on prefix
            if (cmd.contains("!")) {
                switch (cmd.split("!")[0].toLowerCase().trim()) {
                    case "op":
                        executeOp(cmd, player);
                    case "player":
                        executePlayer(cmd, player);
                        break;
                    default:
                        executeConsole(cmd);
                }
            } else executeConsole(cmd);
        }

        // Messages
        List<String> msg = informMessage.stream().map(line -> Utils.parse(line, player)).collect(Collectors.toList());
    }

    // Execute command as console
    private void executeConsole(String cmd) {
        Main.inst.getServer().dispatchCommand(Main.inst.getServer().getConsoleSender(), cmd);
    }

    // Execute command as player
    private void executePlayer(String cmd, Player player) {
        player.performCommand(cmd);
    }

    // Execute as player with op
    private void executeOp(String cmd, Player player) {

        // If player is already op, we don't have to set it again
        if (player.isOp()) {
            executePlayer(cmd, player);
            return;
        }

        // Give op and take after command is executed
        player.setOp(true);
        player.performCommand(cmd);
        player.setOp(false);
    }
}
