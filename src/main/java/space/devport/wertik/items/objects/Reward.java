package space.devport.wertik.items.objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import space.devport.utils.messageutil.MessageBuilder;
import space.devport.utils.messageutil.ParseFormat;
import space.devport.wertik.items.ItemsPlugin;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Reward {

    // Commands executed
    private final List<String> commands = new ArrayList<>();

    // Message brodcasted to server
    private MessageBuilder broadcast = new MessageBuilder();

    // Message sent to player
    private MessageBuilder inform = new MessageBuilder();

    // ParseFormat containing placeholders
    private ParseFormat format = new ParseFormat();

    // Reward a player
    public void give(Player player) {

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
                int random = ItemsPlugin.getInstance().getRandom().nextInt(randomCommands.size());
                parseCommand(player, randomCommands.get(random));
            }
        }

        // Inform
        inform.send(player);

        // Broadcast
        Bukkit.getOnlinePlayers().forEach(broadcast::send);
    }

    // Parses a command
    private void parseCommand(Player player, String cmd) {

        format.fill("%player%", player.getName());

        // Parse placeholders
        cmd = format.parse(cmd);

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
        ItemsPlugin.getInstance().getServer().dispatchCommand(ItemsPlugin.getInstance().getServer().getConsoleSender(), cmd.trim());
    }

    // Execute command as player
    private void executePlayer(String cmd, Player player) {
        player.performCommand(cmd.trim());
    }

    // Execute as player with op
    private void executeOp(String cmd, Player player) {

        // If player is already op, we don't have to risk it
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