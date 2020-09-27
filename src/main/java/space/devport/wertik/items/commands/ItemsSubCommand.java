package space.devport.wertik.items.commands;

import org.jetbrains.annotations.Nullable;
import space.devport.utils.commands.SubCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.wertik.items.ItemsPlugin;

public abstract class ItemsSubCommand extends SubCommand {

    protected final ItemsPlugin plugin;

    public ItemsSubCommand(String name, ItemsPlugin plugin) {
        super(name);
        //setPermissions();
        this.plugin = plugin;
    }

    @Override
    public @Nullable
    abstract String getDefaultUsage();

    @Override
    public @Nullable
    abstract String getDefaultDescription();

    @Override
    public @Nullable
    abstract ArgumentRange getRange();
}
