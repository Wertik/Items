package space.devport.wertik.items.commands.utility;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import space.devport.utils.commands.MainCommand;
import space.devport.utils.commands.struct.ArgumentRange;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.utils.commands.struct.Preconditions;
import space.devport.utils.item.ItemBuilder;
import space.devport.utils.xseries.XMaterial;
import space.devport.wertik.items.commands.CommandUtils;
import space.devport.wertik.items.utils.Utils;

public class SetType extends MainCommand {

    public SetType(String name) {
        super(name);
        this.preconditions = new Preconditions()
                .playerOnly()
                .permissions("items.utility.settype");
    }

    @Override
    protected CommandResult perform(CommandSender sender, String label, String[] args) {

        Player player = (Player) sender;

        if (CommandUtils.checkAir(player)) return CommandResult.FAILURE;

        XMaterial type = XMaterial.matchXMaterial(args[0].toUpperCase()).orElse(null);

        if (type == null) {
            language.getPrefixed("Invalid-Material")
                    .replace("%param%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        Material material = type.parseMaterial();

        if (material == null) {
            language.getPrefixed("Invalid-Material")
                    .replace("%param%", args[0])
                    .send(sender);
            return CommandResult.FAILURE;
        }

        ItemBuilder builder = Utils.getBuilderInHand(player);

        builder.type(material);

        Utils.setItem(player, EquipmentSlot.HAND, builder.build());
        language.getPrefixed("Material-Changed")
                .replace("%material%", builder.getMaterial())
                .send(sender);
        return CommandResult.SUCCESS;
    }

    @Override
    public String getDefaultUsage() {
        return "/%label% <type>";
    }

    @Override
    public String getDefaultDescription() {
        return "Change the material of an item.";
    }

    @Override
    public ArgumentRange getRange() {
        return new ArgumentRange(1);
    }
}