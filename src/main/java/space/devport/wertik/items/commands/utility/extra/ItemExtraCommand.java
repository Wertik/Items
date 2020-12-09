package space.devport.wertik.items.commands.utility.extra;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.CommandUtils;

public class ItemExtraCommand extends MainCommand {

    public ItemExtraCommand(ItemsPlugin plugin) {
        super("itemextra");

        this.preconditions = new Preconditions()
                .playerOnly()
                .permissions("items.utility.extra");

        addSubCommand(new UnPlace(plugin));
        addSubCommand(new UnStack(plugin));
        addSubCommand(new UnCraft(plugin));
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        if (CommandUtils.checkAir(sender)) return CommandResult.FAILURE;
        return super.perform(sender, label, args);
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
        return "Manage item extra.";
    }
}