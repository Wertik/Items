package space.devport.wertik.items.objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import space.devport.utils.text.Placeholders;
import space.devport.utils.text.message.CachedMessage;
import space.devport.wertik.items.ItemsPlugin;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Reward {

    private List<String> commands = new ArrayList<>();

    private CachedMessage broadcast = new CachedMessage();

    private CachedMessage inform = new CachedMessage();

    private Placeholders placeholders = new Placeholders();

    public void give(Player player) {

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
                int random = ItemsPlugin.getInstance().getRandom().nextInt(randomCommands.size());
                parseCommand(player, randomCommands.get(random));
            }
        }

        // Inform
        inform.parseWith(placeholders).send(player);
        inform.pull();

        // Broadcast
        broadcast.parseWith(placeholders);
        for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
            broadcast.send(loopPlayer);
            broadcast.pull();
        }
    }

    // Parses a command
    private void parseCommand(Player player, String cmd) {

        placeholders.add("%player%", player.getName());

        // Parse placeholders
        cmd = placeholders.parse(cmd).trim();

        // Execute only once
        if (cmd.startsWith("op!"))
            executeOp(cmd.replace("op!", "").trim(), player);
        else if (cmd.startsWith("p!"))
            executePlayer(cmd.replace("p!", "").trim(), player);
        else
            executeConsole(cmd);
    }

    // Execute command as console
    private void executeConsole(String cmd) {
        ItemsPlugin.getInstance().getServer().dispatchCommand(ItemsPlugin.getInstance().getServer().getConsoleSender(), cmd);
    }

    // Execute command as player
    private void executePlayer(String cmd, Player player) {
        player.performCommand(cmd);
    }

    // Execute as player with op
    private void executeOp(String cmd, Player player) {

        // If player is already op, we don't have to risk it
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