package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import space.devport.utils.DevportPlugin;
import space.devport.utils.UsageFlag;
import space.devport.wertik.items.commands.attributes.*;
import space.devport.wertik.items.commands.items.*;
import space.devport.wertik.items.commands.utility.SetName;
import space.devport.wertik.items.commands.utility.SetType;
import space.devport.wertik.items.commands.utility.enchants.AddEnchant;
import space.devport.wertik.items.commands.utility.enchants.ClearEnchants;
import space.devport.wertik.items.commands.utility.enchants.Enchants;
import space.devport.wertik.items.commands.utility.enchants.RemoveEnchant;
import space.devport.wertik.items.commands.utility.extra.ItemExtra;
import space.devport.wertik.items.commands.utility.extra.UnCraft;
import space.devport.wertik.items.commands.utility.extra.UnPlace;
import space.devport.wertik.items.commands.utility.extra.UnStack;
import space.devport.wertik.items.commands.utility.flags.AddFlag;
import space.devport.wertik.items.commands.utility.flags.ClearFlags;
import space.devport.wertik.items.commands.utility.flags.Flags;
import space.devport.wertik.items.commands.utility.flags.RemoveFlag;
import space.devport.wertik.items.commands.utility.lore.AddLore;
import space.devport.wertik.items.commands.utility.lore.ClearLore;
import space.devport.wertik.items.commands.utility.lore.Lore;
import space.devport.wertik.items.commands.utility.lore.RemoveLore;
import space.devport.wertik.items.commands.utility.nbt.AddNBT;
import space.devport.wertik.items.commands.utility.nbt.ClearNBT;
import space.devport.wertik.items.commands.utility.nbt.NBT;
import space.devport.wertik.items.commands.utility.nbt.RemoveNBT;
import space.devport.wertik.items.listeners.CraftListener;
import space.devport.wertik.items.listeners.ItemListener;
import space.devport.wertik.items.system.AttributeManager;
import space.devport.wertik.items.system.CooldownManager;
import space.devport.wertik.items.system.ItemManager;

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

    @Getter
    private final List<String> actionNames = new ArrayList<>(Arrays.asList("right_click", "left_click", "shift_left_click", "shift_right_click"));

    @Getter
    private ItemManager itemManager;
    @Getter
    private AttributeManager attributeManager;
    @Getter
    private CooldownManager cooldownManager;

    @Getter
    private Random random;

    public boolean usePlaceholderAPI = false;

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

        addMainCommand(new ItemsCommand("items")
                .addSubCommand(new Detail(this))
                .addSubCommand(new DropItem(this))
                .addSubCommand(new GiveItem(this))
                .addSubCommand(new ListItems(this))
                .addSubCommand(new LoadItem(this))
                .addSubCommand(new Reload(this))
                .addSubCommand(new RemoveItem(this))
                .addSubCommand(new SaveItem(this)));

        addMainCommand(new AttributesCommand("attributes")
                .addSubCommand(new AddAttribute(this))
                .addSubCommand(new RemoveAttribute(this))
                .addSubCommand(new ListAttributes(this))
                .addSubCommand(new ClearAttributes(this)));

        addMainCommand(new Lore("lore")
                .addSubCommand(new AddLore("add"))
                .addSubCommand(new RemoveLore("remove"))
                .addSubCommand(new ClearLore("clear")));

        addMainCommand(new Flags("flags")
                .addSubCommand(new AddFlag("add"))
                .addSubCommand(new RemoveFlag("remove"))
                .addSubCommand(new ClearFlags("clear")));

        addMainCommand(new Enchants("enchants")
                .addSubCommand(new AddEnchant("add"))
                .addSubCommand(new RemoveEnchant("remove"))
                .addSubCommand(new ClearEnchants("clear")));

        addMainCommand(new ItemExtra("itemextra")
                .addSubCommand(new UnPlace("unplace"))
                .addSubCommand(new UnStack("unstack"))
                .addSubCommand(new UnCraft("uncraft")));

        addMainCommand(new NBT("nbt")
                .addSubCommand(new AddNBT("add"))
                .addSubCommand(new RemoveNBT("remove"))
                .addSubCommand(new ClearNBT("clear")));

        addMainCommand(new SetName("setname"));
        addMainCommand(new SetType("settype"));
        addMainCommand(new space.devport.wertik.items.commands.utility.Detail("detail"));
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
}