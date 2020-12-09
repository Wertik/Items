package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.wertik.items.commands.attributes.AttributesCommand;
import space.devport.wertik.items.commands.items.ItemsCommand;
import space.devport.wertik.items.commands.utility.DetailCommand;
import space.devport.wertik.items.commands.utility.SetNameCommand;
import space.devport.wertik.items.commands.utility.SetTypeCommand;
import space.devport.wertik.items.commands.utility.enchants.EnchantCommand;
import space.devport.wertik.items.commands.utility.extra.ItemExtraCommand;
import space.devport.wertik.items.commands.utility.flags.FlagCommand;
import space.devport.wertik.items.commands.utility.lore.LoreCommand;
import space.devport.wertik.items.commands.utility.nbt.NBTCommand;
import space.devport.wertik.items.listeners.CraftListener;
import space.devport.wertik.items.listeners.ItemListener;
import space.devport.wertik.items.system.attribute.AttributeManager;
import space.devport.wertik.items.system.cooldown.CooldownManager;
import space.devport.wertik.items.system.item.ItemManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemsPlugin extends DevportPlugin {

    /* Global TO-DO */
    // TODO: Allow saving items to different files and folders (like BossShopPro)
    // TODO: Optional "transaction" logger
    // TODO: Add support for WorldGuard - disable some items in a region
    // TODO: Add the ability to re-parse placeholders on an item
    // TODO: Json chat item editing
    // TODO: Add Item Control ( for lobbies, lock an item in place, or just deny "losing it" )
    // TODO: Use item tracking ( you'll be able to update items already given to players )
    // TODO: Redesign commands ( just colors and styling )
    // TODO: Allow multiple attributes on one action

    public static ItemsPlugin getInstance() {
        return getPlugin(ItemsPlugin.class);
    }

    public static final List<String> ACTIONS = new ArrayList<>(Arrays.asList("right_click", "left_click", "shift_left_click", "shift_right_click"));

    @Getter
    private ItemManager itemManager;
    @Getter
    private AttributeManager attributeManager;
    @Getter
    private CooldownManager cooldownManager;

    @Getter
    private Random random;

    @Getter
    private boolean usePlaceholderAPI = false;

    @Override
    public UsageFlag[] usageFlags() {
        return new UsageFlag[]{UsageFlag.COMMANDS, UsageFlag.CONFIGURATION, UsageFlag.LANGUAGE};
    }

    @Override
    public void onPluginEnable() {

        new ItemsLanguage(this);

        random = new Random();

        checkHooks();

        // Load attributes
        attributeManager = new AttributeManager(this);
        attributeManager.load();
        consoleOutput.info("Loaded " + attributeManager.getAttributeCache().size() + " attribute(s)..");

        // Initialize a cooldown handler
        cooldownManager = new CooldownManager(this);

        // Load items
        itemManager = new ItemManager(this);
        itemManager.loadItems();
        consoleOutput.info("Loaded " + itemManager.getItems().size() + " item(s)..");

        new ItemListener(this);
        new CraftListener(this);

        addMainCommand(new ItemsCommand(this));

        addMainCommand(new AttributesCommand(this));

        addMainCommand(new LoreCommand(this));
        addMainCommand(new EnchantCommand(this));
        addMainCommand(new FlagCommand(this));
        addMainCommand(new NBTCommand());
        addMainCommand(new ItemExtraCommand(this));

        addMainCommand(new SetNameCommand());
        addMainCommand(new SetTypeCommand());
        addMainCommand(new DetailCommand());
    }

    @Override
    public void onPluginDisable() {
        itemManager.saveItems();
    }

    @Override
    public void onReload() {
        checkHooks();

        attributeManager.load();
        consoleOutput.info("Loaded " + attributeManager.getAttributeCache().size() + " attribute(s)..");

        cooldownManager.getCooldownCache().clear();
    }

    private void checkHooks() {
        PluginManager pluginManager = getServer().getPluginManager();

        // PlaceholderAPI
        if (pluginManager.getPlugin("PlaceholderAPI") != null && !usePlaceholderAPI) {
            this.usePlaceholderAPI = true;
            consoleOutput.info("Found PlaceholderAPI, using it's placeholders.");
        } else if (pluginManager.getPlugin("PlaceholderAPI") == null && usePlaceholderAPI) {
            this.usePlaceholderAPI = false;
            consoleOutput.info("Couldn't find PlaceholderAPI, disabling placeholder usage.");
        }
    }

    public static void executeConsoleCommand(String command) {
        Bukkit.getScheduler().runTask(getInstance(), () -> Bukkit.dispatchCommand(getInstance().getServer().getConsoleSender(), command));
    }

    public static void executePlayerCommand(Player player, String command) {
        Bukkit.getScheduler().runTask(getInstance(), () -> player.performCommand(command));
    }
}