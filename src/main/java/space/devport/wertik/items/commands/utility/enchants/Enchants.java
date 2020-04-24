package space.devport.wertik.items.commands.utility.enchants;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

public class Enchants extends MainCommand {

    public Enchants(String name) {
        super(name);
        this.preconditions = new Preconditions().playerOnly().permissions("items.utility.enchants");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        if (CommandUtils.checkAir((Player) sender)) return CommandResult.FAILURE;

        if (args.length > 0) return super.perform(sender, label, args);

        ItemBuilder builder = Utils.getBuilderInHand((Player) sender);

        language.getPrefixed("Enchants-List")
                .replace("%enchants%", Utils.mapToString(builder.getEnchants(), "&7, &f", "&f:", "&cNo enchants."))
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
        return "Manage enchants.";
    }
}