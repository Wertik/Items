package space.devport.wertik.items.utils;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.messageutil.MessageBuilder;
import space.devport.wertik.items.ItemsPlugin;

import java.util.List;

public enum Language {

    /**
     * GENERAL
     */

    NO_PERMS("No-Permissions", "&cYou don't have permission to do this."),
    ONLY_PLAYERS("Only-Players", "&cAction has to be done in-game."),
    PLAYER_OFFLINE("Player-Offline", "&cThat player is not online."),
    CANNOT_HELP_WITH_AIR("Cannot-Help-With-Air", "&cCannot help you with AIR."),
    RELOAD("Reload-Done", "&7Done... reload took &f%time%&7ms.", "Reload does not manipulate with item storage, to load/save them, do '/items load/save'."),
    TOO_MANY_ARGUMENTS("Too-Many-Arguments", "&cToo many arguments.", "&cUsage: &7%usage%"),
    NOT_ENOUGH_ARGUMENTS("Not-Enough-Arguments", "&cNot enough arguments.", "&cUsage: &7%usage%"),
    NOT_A_NUMBER("Not-A-Number", "&f%param% &cis not a number."),

    /**
     * UTIL COMMANDS
     */

    LINE_ADDED("Line-Added", "&7Line added to item lore."),
    LINE_REMOVED("Line-Removed", "&7Line removed from item lore."),
    NO_LORE("No-Lore", "&cItem doesn't have a lore."),
    FLAGS_LIST("Flags-List", "&7Flags: %flags%"),
    FLAG_ADDED("Flag-Added", "&7Flag added to item."),
    FLAG_REMOVED("Flag-Removed", "&7Flag removed from item."),
    FLAG_INVALID("Flag-Invalid", "&cFlag &f%flag% &cis invalid."),
    NO_FLAG("No-Flag", "&cItem doesn't have this flag."),
    ENCHANTS_LIST("Enchants-List", "&7Enchants: %enchants%"),
    ENCHANT_ADDED("Enchant-Added", "&7Enchant added to item."),
    ENCHANT_REMOVED("Enchant-Removed", "&7Enchantment removed."),
    INVALID_ENCHANT("Invalid-Enchantment", "&cEnchantment is invalid."),
    ITEM_RENAMED("Item-Renamed", "&7Item renamed."),
    SET_UNSTACKABLE("Set-Unstackable", "&7Unstackable property set to &f%state%&7."),

    /**
     * ITEM COMMANDS
     */

    ITEMS_HELP("Items-Help",
            "&8&m        &e Items &8&m        &r",
            "&e/%label% list &8- &7Lists saved items.",
            "&e/%label% save (name) &8- &7Save all loaded items, or save item in hand under a name.",
            "&e/%label% load (name) &8- &7Load all items, or by name.",
            "&e/%label% remove <name> &8- &7Removes item by name.",
            "&e/%label% detail <name> &8- &7Displays info about an item in the db.",
            "&e/%label% drop <name> <worldName;x;y;z> (amount) &8- &7Drops item on a given location.",
            "&e/%label% give <name> (playerName) (amount) &8- &7Give player an item.",
            "&e/att &8- &7Help page regarding attributes.",
            "&e/setname <name> &8- &7Set display name of an item.",
            "&e/lore &8- &7List item lore.",
            "&e/addlore <line> &8- &7Add a line of lore.",
            "&e/remlore <lineINdex> &8- &7Remove a line from item lore.",
            "&e/flags &8- &7Display flags on an item.",
            "&e/addflag <flagName> &8- &7Add itemFlag to an item.",
            "&e/remflag <flagName> &8- &7Remove itemFlag from an item.",
            "&e/enchs &8- &7List enchantments on item.",
            "&e/addench <enchantment> <level> &8- &7Add enchant to item.",
            "&e/remench <enchantment> &8- &7Remove enchantment from item."),
    ITEM_SPAWNED_AT("Spawned-At", "&7Item &f%item%&7x%amount%&7 spawned &8@ &7%location%"),
    ITEMS_LIST("Items-List", "&7Items: %items%"),
    ITEM_ADDED("Item-Added", "&7Item added under name &f%item%"),
    ITEM_UPDATED("Item-Updated", "&7Item &f%item% &7updated."),
    ITEM_REMOVED("Item-Removed", "&7Item &f%item% &7removed."),
    ITEMS_SAVED("Items-Saved", "&7Items saved to storage."),
    ITEMS_LOADED("Items-Loaded", "&7Items loaded from storage."),
    ITEM_SAVED("Item-Saved", "&7Item &f%item% &7saved to storage."),
    ITEM_LOADED("Item-Loaded", "&7Item &f%item% &7loaded from storage."),
    ITEM_NOT_VALID("Item-Not-Valid", "&cItem &f%item% &cdoes not exist."),
    ITEM_GIVEN("Item-Given", "&7Gave player &f%player% &7item &e%item%&7x&f%amount%"),

    /**
     * ATTRIBUTES
     */

    ATTRIBUTES_HELP("Attributes-Help",
            "&8&m        &r &eAttributes &8&m        &r",
            "&e/att add <name> <action> &8- &7Add attribute to an item.",
            "&e/att rem <name/action> &8- &7Remove attribute based on clickType/Name.",
            "&e/att clear &8- &7Clears attributes.",
            "&e/att list (hand/h) &8- &7Lists attributes from config or on item in hand."),
    ATTRIBUTES_LIST("Attributes", "&7Attributes: %attributes%"),
    ATTRIBUTE_ADDED("Attribute-Added", "&7Attribute &f%attribute% &7added to item."),
    ATTRIBUTE_REMOVED("Attribute-Removed", "&7Attribute &f%attribute% &7removed from item."),
    ATTRIBUTE_COULD_NOT_REMOVE("Could-Not-Remove-Attribute", "&cCould not remove attribute."),
    ATTRIBUTE_INVALID_PARAM("Attribute-Invalid-Param", "&cItem doesn't have action or attribute &f%param%&c.", "&cUsage: &7%usage%"),
    ATTRIBUTE_INVALID("Attribute-Invalid", "&cAttribute &f%attribute% &cis not valid.", "&7Valid attributes: %valid%"),
    ATTRIBUTES_CLEARED("Attributes-Cleared", "&7Attributes cleared."),
    CLICK_TYPE_INVALID("Click-Type-Invalid", "&cAction &f%action% &cis not valid.", "&cValid actions: &7%valid%"),

    /**
     * COOLDOWNS
     */

    COOLDOWN_EXPIRED("Cooldown-Expired", "&7Cooldown on attribute %attribute% has expired."),
    ITEM_ON_COOLDOWN("Item-Cooldown", "&7Item is still on cooldown, wait &f%time%&7s."),

    /**
     * LIMIT USE
     */

    ITEM_USE_LIMIT("Item-Use-Limit", "&7You have reached this items maximum amount of uses.");

    @Getter
    private final String path;

    @Getter
    private MessageBuilder value;

    Language(String path, String... value) {
        this.path = path;
        this.value = new MessageBuilder(value);
    }

    public void setValue(MessageBuilder value) {
        this.value = value;
    }

    public static void load() {
        Configuration lang = new Configuration(ItemsPlugin.getInstance(), "language");

        boolean save = false;

        for (Language message : values()) {
            if (!lang.getFileConfiguration().contains(message.getPath())) {
                lang.setMessageBuilder(message.getPath(), message.getValue());
                save = true;
            } else {
                message.setValue(lang.loadMessageBuilder(message.getPath(), message.getValue()));
            }
        }

        if (save)
            lang.save();
    }

    public void send(CommandSender sender) {
        if (!get().isEmptyAbsolute())
            get().send(sender);
    }

    public void sendPrefixed(CommandSender sender) {
        if (!getPrefixed().isEmpty())
            getPrefixed().send(sender);
    }

    public MessageBuilder get() {
        return new MessageBuilder(this.value);
    }

    public MessageBuilder getPrefixed() {
        if (get().isEmptyAbsolute())
            return new MessageBuilder();

        MessageBuilder message = new MessageBuilder(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + get().getWorkingMessage().get(0));
        List<String> subList = get().getWorkingMessage().subList(1, get().getWorkingMessage().size());

        if (!subList.isEmpty())
            subList.forEach(message::addLine);

        return message;
    }
}