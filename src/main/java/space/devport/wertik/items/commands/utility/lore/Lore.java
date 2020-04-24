package space.devport.wertik.items.commands.utility.lore;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

public class Lore extends MainCommand {

    public Lore(String name) {
        super(name);
        this.preconditions = new Preconditions()
                .playerOnly()
                .permissions("items.control");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkAir((Player) sender)) return CommandResult.FAILURE;

        if (args.length > 0) return super.perform(sender, label, args);

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        if (builder.getLore().getMessage().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eLore: &cNo lore."));
            return CommandResult.FAILURE;
        }

        sender.sendMessage(StringUtil.color("&eLore:"));
        int i = 0;
        for (String line : builder.getLore().getMessage()) {
            sender.sendMessage(StringUtil.color("&f " + i + " &8- &r" + line));
            i++;
        }

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
        return "Manage lore.";
    }
}