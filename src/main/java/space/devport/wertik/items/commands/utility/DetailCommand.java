package space.devport.wertik.items.commands.utility;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.wertik.items.commands.CommandUtils;

public class DetailCommand extends MainCommand {

    public DetailCommand() {
        super("detail");
        this.preconditions = new Preconditions()
                .permissions("items.utility.detail")
                .playerOnly();
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkAir(sender))
            return CommandResult.FAILURE;

        CommandUtils.sendDetail(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public @NotNull ArgumentRange getRange() {
        return new ArgumentRange(0);
    }

    @Override
    public String getDefaultUsage() {
        return "/%label%";
    }

    @Override
    public String getDefaultDescription() {
        return "Display details about item in hand.";
    }
}