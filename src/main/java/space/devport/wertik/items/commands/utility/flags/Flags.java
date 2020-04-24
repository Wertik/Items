package space.devport.wertik.items.commands.utility.flags;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

import java.util.List;
import java.util.stream.Collectors;

public class Flags extends MainCommand {

    public Flags(String name) {
        super(name);
        this.aliases = new String[]{"flag"};
        this.preconditions = new Preconditions().playerOnly().permissions("items.control");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkAir(sender)) return CommandResult.FAILURE;

        if (args.length > 0) return super.perform(sender, label, args);

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        List<String> flags = builder.getFlags().stream().map(ItemFlag::name).collect(Collectors.toList());
        language.getPrefixed("Flags-List")
                .replace("%flags%", Utils.listToString(flags, "&7, &f", "&cNo flags."))
                .send(sender);
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
        return "Manage flags.";
    }
}