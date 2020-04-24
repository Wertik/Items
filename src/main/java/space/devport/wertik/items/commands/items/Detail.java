package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.system.ItemManager;
import space.devport.wertik.items.utils.Utils;

public class Detail extends SubCommand {

    private final ItemManager itemManager;

    public Detail(String name) {
        super(name);
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[1])) return CommandResult.FAILURE;

        Player player = (Player) sender;
        ItemBuilder builder = Utils.getBuilderInHand(player);

        // TODO: Somehow add to language and move to Command Utils

        sender.sendMessage(StringUtil.color("&eName: &f" + (builder.getDisplayName().isEmpty() ? builder.getMaterial().toString() : builder.getDisplayName().toString())));
        sender.sendMessage(StringUtil.color("&eMaterial: &f" + builder.getMaterial().name()));
        sender.sendMessage(StringUtil.color("&eAmount: &f" + builder.getAmount()));

        // Lore
        if (!builder.getLore().getMessage().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eLore:"));
            int i = 0;
            for (String line : builder.getLore().getMessage()) {
                sender.sendMessage(StringUtil.color("&f " + i + " &8- &r" + line));
                i++;
            }
        }

        // Enchants
        if (!builder.getEnchants().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eEnchants:"));
            builder.getEnchants().forEach((enchantment, level) -> sender.sendMessage(StringUtil.color(" &8- &7" + enchantment.toString() + "&f;&7" + level)));
        }

        // Flags
        if (!builder.getFlags().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eFlags:"));
            builder.getFlags().forEach(flag -> sender.sendMessage(StringUtil.color(" &8- &7" + flag.toString())));
        }

        // NBT
        if (!builder.getNBT().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eNBT:"));

            for (String key : builder.getNBT().keySet()) {
                if (!ItemsPlugin.getInstance().getFilteredNBT().contains(key))
                    sender.sendMessage(StringUtil.color(" &8- &7" + key + "&f:&7" + builder.getNBT().get(key)));
            }
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% detail <name>";
    }

    @Override
    public String getDefaultDescription() {
        return "Displays details about an item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}