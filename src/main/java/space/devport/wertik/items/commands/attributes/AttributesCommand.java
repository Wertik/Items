package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.ItemsPlugin;

public class AttributesCommand extends MainCommand {

    public AttributesCommand(ItemsPlugin plugin) {
        super("attributes");

        this.preconditions = new Preconditions()
                .permissions("items.attributes")
                .playerOnly();

        addSubCommand(new AddAttribute(plugin));
        addSubCommand(new RemoveAttribute(plugin));
        addSubCommand(new ListAttributes(plugin));
        addSubCommand(new ClearAttributes(plugin));
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        return super.perform(sender, label, args);
    }

    @Override
    public String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public String getDefaultDescription() {
        return "Displays this.";
    }
}