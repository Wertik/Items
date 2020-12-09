package space.devport.wertik.items.system.attribute.struct;

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
        Bukkit.getScheduler().runTaskAsynchronously(ItemsPlugin.getInstance(), () -> {
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
        });
    }

    // Parses a command
    private void parseCommand(Player player, String cmd) {

        placeholders.add("%player%", player.getName());

        // Parse placeholders
        cmd = placeholders.parse(cmd).trim();

        if (cmd.startsWith("op!"))
            executeOp(player, strip(cmd));
        else if (cmd.startsWith("p!"))
            executePlayer(player, strip(cmd));
        else
            executeConsole(strip(cmd));
    }

    private String strip(String command) {
        return command.replace("op!", "")
                .replace("p!", "");
    }

    private void executeConsole(String command) {
        ItemsPlugin.executeConsoleCommand(command);
    }

    private void executePlayer(Player player, String command) {
        ItemsPlugin.executePlayerCommand(player, command);
    }

    private void executeOp(Player player, String command) {

        if (player.isOp()) {
            executePlayer(player, command);
            return;
        }

        Bukkit.getScheduler().runTask(ItemsPlugin.getInstance(), () -> {
            player.setOp(true);
            player.performCommand(command);
            player.setOp(false);
        });
    }
}