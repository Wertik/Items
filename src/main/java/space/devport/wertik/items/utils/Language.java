package space.devport.wertik.items.utils;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.messageutil.MessageBuilder;
import space.devport.wertik.items.ItemsPlugin;

public enum Language {

    /**
     * GENERAL
     */

    NO_PERMS("No-Permissions", "&cYou don't have permission to do this."),
    ONLY_PLAYERS("Only-Players", "&cAction has to be done in-game."),
    PLAYER_OFFLINE("Player-Offline", "&cThat player is not online."),
    CANNOT_HELP_WITH_AIR("Cannot-Help-With-Air", "&cCannot help you with AIR."),

    /**
     * UTIL COMMANDS
     */

    LINE_ADDED("Line-Added", "&7Line added to item lore."),
    LINE_REMOVED("Line-Removed", "&7Line removed from item lore."),
    FLAG_ADDED("Flag-Added", "&7Flag added to item."),
    FLAG_REMOVED("Flag-Removed", "&7Flag removed from item."),
    NO_FLAG("No-Flag", "&cItem doesn't have this flag."),
    ENCHANT_ADDED("Enchant-Added", "&7Enchant added to item."),
    ENCHANT_REMOVED("Enchant-Removed", "&7Enchantment removed."),
    INVALID_ENCHANT("Invalid-Enchantment", "&cEnchantment is invalid."),
    ITEM_RENAMED("Item-Renamed", "&7Item renamed."),

    /**
     * ITEM COMMANDS
     */

    SPAWNED_AT("Spawned-At", "&7Item &f%item%&7x%amount%&7 spawned &8@ &7%location%"),
    ITEMS_SAVED("Items-Saved", "&7Items saved to storage."),
    ITEMS_LOADED("Items-Loaded", "&7Items loaded from storage."),
    ITEM_SAVED("Item-Saved", "&7Item &f%item% &7saved to storage."),
    ITEM_LOADED("Item-Loaded", "&7Item &f%item% &7loaded from storage."),
    ITEM_NOT_VALID("Item-Not-Valid", "&7Item &f%item% &7does not exist."),
    ITEM_GIVEN("Item-Given", "&7Gave player &f%player% &7item &e%item%&7x&f%amount%"),

    /**
     * ATTRIBUTES
     */

    ATTRIBUTE_ADDED("Attribute-Added", "&7Attribute &f%attribute% &7added to item."),
    ATTRIBUTE_REMOVED("Attribute-Removed", "&7Attribute &f%attribute% &7removed from item."),

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
    @Setter
    private String value;

    Language(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public static void load() {
        Configuration lang = new Configuration(ItemsPlugin.getInstance(), "language");

        for (Language message : values()) {
            String value = lang.getFileConfiguration().getString(message.getPath());

            if (!Strings.isNullOrEmpty(value)) {
                message.setValue(value);
            } else {
                lang.getFileConfiguration().set(message.getPath(), message.getValue());
            }
        }

        lang.save();
    }

    public void send(CommandSender sender) {
        get().send(sender);
    }

    public void sendPrefixed(CommandSender sender) {
        getPrefixed().send(sender);
    }

    public MessageBuilder get() {
        return new MessageBuilder(this.value);
    }

    public MessageBuilder getPrefixed() {
        if (get().isEmpty())
            return new MessageBuilder();

        MessageBuilder message = new MessageBuilder(ItemsPlugin.getInstance().getConsoleOutput().getPrefix() + get().getWorkingMessage().get(0));
        get().getWorkingMessage().subList(0, get().getWorkingMessage().size() - 1).forEach(message::addLine);
        return message;
    }
}