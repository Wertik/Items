package space.devport.wertik.items.commands.utility.nbt;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.text.message.Message;
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
        if (builder.getNBT().isEmpty()) {
            language.sendPrefixed(sender, "No-NBT");
            return CommandResult.FAILURE;
        }

        Message nbt = new Message(language.getPrefixed("NBT-List"));

        for (Map.Entry<String, String> entry : builder.getNBT().entrySet()) {
            nbt.append(language.get("NBT-List-Line")
                    .replace("%key%", entry.getKey())
                    .replace("%value%", entry.getValue()));
        }

        nbt.send(sender);
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