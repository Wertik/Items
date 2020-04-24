package space.devport.wertik.items.commands.utility.nbt;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.StringUtil;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

import java.util.Map;

public class NBT extends MainCommand {

    public NBT(String name) {
        super(name);
        this.preconditions = new Preconditions().playerOnly().permissions("items.utility.nbt");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        if (CommandUtils.checkAir(sender)) return CommandResult.FAILURE;

        if (args.length > 0)
            return super.perform(sender, label, args);

        Player player = (Player) sender;

        ItemBuilder builder = Utils.getBuilderInHand(player);

        // NBT
        if (!builder.getNBT().isEmpty()) {
            sender.sendMessage(StringUtil.color("&eNBT:"));

            for (Map.Entry<String, String> entry : builder.getNBT().entrySet()) {
                if (!ItemBuilder.getFilteredNBT().contains(entry.getKey()))
                    sender.sendMessage(StringUtil.color(" &8- &7" + entry.getKey() + " &f=&7 " + builder.getNBT().get(entry.getKey())));
            }
        } else
            sender.sendMessage(ItemsPlugin.getInstance().getPrefix() + StringUtil.color("&cNo NBT."));

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
        return "Manage NBT.";
    }
}