package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import space.devport.utils.DevportPlugin;
import space.devport.utils.commands.struct.CommandResult;
import space.devport.wertik.items.commands.*;
import space.devport.wertik.items.commands.attributes.Add;
import space.devport.wertik.items.commands.attributes.Clear;
import space.devport.wertik.items.commands.attributes.Remove;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.handlers.CooldownHandler;
import space.devport.wertik.items.handlers.ItemHandler;
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
    private ItemHandler itemHandler;
    @Getter
    private AttributeHandler attributeHandler;
    @Getter
    private CooldownHandler cooldownHandler;

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
        attributeHandler = new AttributeHandler();
        attributeHandler.load();
        consoleOutput.info("Loaded " + attributeHandler.getAttributeCache().size() + " attribute(s)..");

        // Initialize a cooldown handler
        cooldownHandler = new CooldownHandler();

        // Load items
        itemHandler = new ItemHandler();
        itemHandler.loadItems();
        consoleOutput.info("Loaded " + itemHandler.getItems().size() + " item(s)..");

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        // Utils command executor and tab completer
        UtilCommands utilCommands = new UtilCommands();
        UtilTabCompleter utilTabCompleter = new UtilTabCompleter();

        getCommand("items").setExecutor(new ItemsCommand());

        addMainCommand(new AttributesCommand("attribute")
                .addSubCommand(new Add("add"))
                .addSubCommand(new Remove("remove"))
                .addSubCommand(new space.devport.wertik.items.commands.attributes.List("list"))
                .addSubCommand(new Clear("clear")));

        getCommand("attribute").setTabCompleter(new AttTabCompleter());

        // Utils command executor and tab completer
        List<String> commands = new ArrayList<>(getDescription().getCommands().keySet()).subList(2, getDescription().getCommands().size());

        for (String command : commands) {
            getCommand(command).setExecutor(utilCommands);
            getCommand(command).setTabCompleter(utilTabCompleter);
        }
    }

    @Override
    public void onPluginDisable() {
        itemHandler.saveItems();
    }

    @Override
    public void onReload() {
        checkHooks();

        attributeHandler.load();
        consoleOutput.info("Loaded " + attributeHandler.getAttributeCache().size() + " attribute(s)..");

        cooldownHandler.getCooldownCache().clear();
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