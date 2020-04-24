package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import space.devport.utils.DevportPlugin;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.commands.AttTabCompleter;
import space.devport.wertik.items.commands.AttributesCommand;
import space.devport.wertik.items.commands.UtilTabCompleter;
import space.devport.wertik.items.commands.attributes.AddAttribute;
import space.devport.wertik.items.commands.attributes.ClearAttributes;
import space.devport.wertik.items.commands.attributes.ListAttributes;
import space.devport.wertik.items.commands.attributes.RemoveAttribute;
import space.devport.wertik.items.commands.items.*;
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
import space.devport.wertik.items.handlers.AttributeManager;
import space.devport.wertik.items.handlers.CooldownManager;
import space.devport.wertik.items.handlers.ItemManager;
import space.devport.wertik.items.listeners.ItemListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemsPlugin extends DevportPlugin {

    /* Global TO-DO */
    // TODO: Allow saving items to different files and folders (like BossShopPro)
    // TODO: Add all remaining strings to language
    // TODO: Add sneak click
    // TODO: Full permissions
    // TODO: Optional "transaction" logger
    // TODO: Add support for WorldGuard - disable specific items
    // TODO: Add the ability to re-parse placeholders on item on attribute item use
    // TODO: Add missing tab completion
    // TODO: Clickable chat editing/GUI
    // TODO: Add Item Control (Player won't be able to get rid of a custom item)
    // TODO: Change material with a command
    // TODO: Add item tracking ( manipulate items that were already given to players, basically what MMO Items does )

    @Getter
    private static ItemsPlugin instance;

    @Getter
    private final List<String> actionNames = new ArrayList<>(Arrays.asList("right_click", "left_click", "shift_left_click", "shift_right_click"));
    @Getter
    private final List<String> filteredNBT = new ArrayList<>(Arrays.asList("Enchantments", "Damage", "display"));

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
    public void onPluginEnable() {
        instance = this;
        setInstance(this);

        setReloadMessagePath("Reload-Done");
        getLanguageManager().setSetInternalDefaults(false);

        CommandResult.NO_PERMISSION.setPath("No-Permissions");
        CommandResult.NO_CONSOLE.setPath("Only-Players");
        CommandResult.TOO_MANY_ARGS.setPath("Too-Many-Arguments");
        CommandResult.NOT_ENOUGH_ARGS.setPath("Not-Enough-Arguments");
        CommandResult.NO_PLAYER.setPath("Only-Console");
        CommandResult.NOT_OPERATOR.setPath("Only-Operator");

        new ItemsLanguage();

        random = new Random();

        checkHooks();

        // Load attributes
        attributeManager = new AttributeManager();
        attributeManager.load();
        consoleOutput.info("Loaded " + attributeManager.getAttributeCache().size() + " attribute(s)..");

        // Initialize a cooldown handler
        cooldownManager = new CooldownManager();

        // Load items
        itemManager = new ItemManager();
        itemManager.loadItems();
        consoleOutput.info("Loaded " + itemManager.getItems().size() + " item(s)..");

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        // Utils command executor and tab completer
        UtilTabCompleter utilTabCompleter = new UtilTabCompleter();

        addMainCommand(new ItemsCommand("items")
                .addSubCommand(new Detail("detail"))
                .addSubCommand(new DropItem("drop"))
                .addSubCommand(new GiveItem("give"))
                .addSubCommand(new ListItems("list"))
                .addSubCommand(new LoadItem("load"))
                .addSubCommand(new Reload("reload"))
                .addSubCommand(new RemoveItem("remove"))
                .addSubCommand(new SaveItem("save")));

        addMainCommand(new AttributesCommand("attributes")
                .addSubCommand(new AddAttribute("add"))
                .addSubCommand(new RemoveAttribute("remove"))
                .addSubCommand(new ListAttributes("list"))
                .addSubCommand(new ClearAttributes("clear")));

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

        getCommand("attribute").setTabCompleter(new AttTabCompleter());

        // Utils command executor and tab completer
        List<String> commands = new ArrayList<>(getDescription().getCommands().keySet()).subList(2, getDescription().getCommands().size());
        for (String command : commands) {
            getCommand(command).setTabCompleter(utilTabCompleter);
        }
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

    @Override
    public boolean useLanguage() {
        return true;
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