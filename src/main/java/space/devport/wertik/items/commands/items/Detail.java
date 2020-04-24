package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.system.ItemManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Detail extends SubCommand {

    private final ItemManager itemManager;

    public Detail(String name) {
        super(name);
        this.preconditions = new Preconditions().permissions("items.manage.detail");
        itemManager = ItemsPlugin.getInstance().getItemManager();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkItemStored(sender, args[0])) return CommandResult.FAILURE;

        ItemBuilder builder = itemManager.getBuilder(args[0]);

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
                if (!ItemBuilder.getFilteredNBT().contains(key))
                    sender.sendMessage(StringUtil.color(" &8- &7" + key + "&f:&7" + builder.getNBT().get(key)));
            }
        }
        return CommandResult.SUCCESS;
    }

    @Override
    public List<String> requestTabComplete(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 0) {
            suggestions.addAll(itemManager.getItems().keySet());
        }

        Collections.sort(suggestions);
        return suggestions;
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