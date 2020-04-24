package space.devport.wertik.items.commands.attributes;

import org.bukkit.command.CommandSender;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;

public class AttributesCommand extends MainCommand {

    public AttributesCommand(String name) {
        super(name);
        this.aliases = new String[]{"att", "atts"};
        this.preconditions = new Preconditions()
                .permissions("items.control")
                .playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {
        return super.perform(sender, label, args);
    }

    @Override
    public boolean registerTabCompleter() {
        return true;
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
        return "Displays this.";
    }
}