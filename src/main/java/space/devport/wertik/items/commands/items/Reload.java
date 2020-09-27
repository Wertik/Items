package space.devport.wertik.items.commands.items;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;
import space.devport.wertik.items.commands.ItemsSubCommand;

public class Reload extends ItemsSubCommand {

    public Reload(ItemsPlugin plugin) {
        super("reload", plugin);
        this.preconditions = new Preconditions().permissions("items.reload");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        plugin.reload(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% reload";
    }

    @Override
    public String getDefaultDescription() {
        return "Reload plugin config and language.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(0);
    }
}
