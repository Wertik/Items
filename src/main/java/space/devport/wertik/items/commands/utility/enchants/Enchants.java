package space.devport.wertik.items.commands.utility.enchants;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.message.Message;
import space.devport.utils.xseries.XEnchantment;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

import java.util.Map;

public class Enchants extends MainCommand {

    public Enchants(String name) {
        super(name);
        this.preconditions = new Preconditions().playerOnly().permissions("items.utility.enchants");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkAir((Player) sender)) return CommandResult.FAILURE;

        if (args.length > 0) return super.perform(sender, label, args);

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        if (builder.getEnchants().isEmpty()) {
            language.sendPrefixed(sender, "No-Enchants");
            return CommandResult.FAILURE;
        }

        Message enchants = new Message(language.getPrefixed("Enchants-List"));

        for (Map.Entry<XEnchantment, Integer> entry : builder.getEnchants().entrySet()) {
            enchants.append(language.get("Enchants-List-Line")
                    .replace("%enchantment%", entry.getKey().name())
                    .replace("%level%", entry.getKey()));
        }

        enchants.send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public boolean checkRange() {
        return false;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public String getDefaultDescription() {
        return "Manage enchants.";
    }
}