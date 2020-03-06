package space.devport.wertik.items.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import space.devport.wertik.items.Main;
import space.devport.wertik.items.utils.Utils;

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

        // Parse commands
        if (!commands.isEmpty()) {
            List<String> randomCommands = new ArrayList<>();

            for (String cmd : commands) {
                cmd = cmd.trim();
                if (cmd.startsWith("random!"))
                    randomCommands.add(cmd.replace("random!", ""));
                else
                    parseCommand(player, cmd);
            }

            // Pick one command
            if (!randomCommands.isEmpty()) {
                int random = Main.inst.getRandom().nextInt(randomCommands.size());
                parseCommand(player, randomCommands.get(random));
            }
        }

        // Messages
        if (!informMessage.isEmpty()) {
            String inform = Utils.listToMessage(informMessage.stream().map(line -> Utils.parse(line, player)).collect(Collectors.toList()));
            player.sendMessage(Utils.color(inform));
        }

        if (!broadcastMessage.isEmpty()) {
            String broadcast = Utils.listToMessage(broadcastMessage.stream().map(line -> Utils.parse(line, player)).collect(Collectors.toList()));
            player.sendMessage(Utils.color(broadcast));
        }
    }

    // Parses a command
    private void parseCommand(Player player, String cmd) {
        // Parse placeholders
        cmd = Utils.parse(cmd, player).trim();

        // Execute only once
        if (cmd.startsWith("op!"))
            executeOp(cmd.replace("op!", ""), player);
        else if (cmd.startsWith("p!"))
            executePlayer(cmd.replace("p!", ""), player);
        else
            executeConsole(cmd);
    }

    // Execute command as console
    private void executeConsole(String cmd) {
        Main.inst.cO.debug("Executing for console: " + cmd.trim());
        Main.inst.getServer().dispatchCommand(Main.inst.getServer().getConsoleSender(), cmd.trim());
    }

    // Execute command as player
    private void executePlayer(String cmd, Player player) {
        Main.inst.cO.debug("Executing for player: " + cmd.trim());
        player.performCommand(cmd.trim());
    }

    // Execute as player with op
    private void executeOp(String cmd, Player player) {

        Main.inst.cO.debug("Executing for OP player: " + cmd.trim());

        // If player is already op, we don't have to set it again
        if (player.isOp()) {
            executePlayer(cmd.trim(), player);
            return;
        }

        // Give op and take after command is executed
        player.setOp(true);
        player.performCommand(cmd.trim());
        player.setOp(false);
    }
}