package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.ItemsPlugin;

public class Reload extends SubCommand {

    public Reload(String name) {
        super(name);
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        ItemsPlugin.getInstance().reload(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return null;
    }

    @Override
    public String getDefaultDescription() {
        return null;
    }

    @Override
    public ArgumentRange getRange() {
        return null;
    }
}
