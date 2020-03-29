package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.DevportUtils;
import space.devport.utils.configutil.Configuration;
import space.devport.wertik.items.commands.*;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.handlers.CooldownHandler;
import space.devport.wertik.items.handlers.ItemHandler;
import space.devport.wertik.items.listeners.ItemListener;
import space.devport.wertik.items.utils.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemsPlugin extends JavaPlugin {

    /* Global TO-DO */
    // TODO: Allow saving items to different files and folders (like BossShopPro)
    // TODO: Add all remaining strings to language
    // TODO: Add sneak click
    // TODO: Placeholders for use limit, cooldown n shit in rewards
    // TODO: Full permissions
    // TODO: Optional "transaction" logger
    // TODO: Add support for WorldGuard - disable specific items

    @Getter
    private static ItemsPlugin instance;

    // Possible actions in lower case
    @Getter
    private final List<String> actionNames = new ArrayList<>(Arrays.asList("right_click", "left_click"));

    @Getter
    private final List<String> filteredNBT = new ArrayList<>(Arrays.asList("Enchantments", "Damage", "display"));

    @Getter
    public ConsoleOutput consoleOutput;

    @Getter
    private ItemHandler itemHandler;
    @Getter
    private AttributeHandler attributeHandler;
    @Getter
    private CooldownHandler cooldownHandler;

    @Getter
    private Configuration cfg;

    @Getter
    private Random random;

    public boolean usePlaceholderAPI = false;

    private void loadOptions() {
        consoleOutput.setDebug(getConfig().getBoolean("debug-enabled"));
        consoleOutput.setPrefix(cfg.getColoredString("plugin-prefix", ""));
    }

    @Override
    public void onEnable() {
        instance = this;

        random = new Random();

        // Setup ConsoleOutput
        DevportUtils utils = new DevportUtils(this);
        consoleOutput = utils.getConsoleOutput();
        consoleOutput.setUseBukkit(true);

        // Load configuration and basic options
        cfg = new Configuration(this, "config");
        loadOptions();

        checkHooks();

        Language.load();

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

        // Utils command executor and tab completer
        List<String> commands = new ArrayList<>(getDescription().getCommands().keySet());

        getCommand("attribute").setExecutor(new AttCommands());
        getCommand("attribute").setTabCompleter(new AttTabCompleter());

        for (int i = 2; i < commands.size(); i++) {
            getCommand(commands.get(i)).setExecutor(utilCommands);
            getCommand(commands.get(i)).setTabCompleter(utilTabCompleter);
        }

        consoleOutput.info("Commands and Tab Completers initialized..");
        consoleOutput.info("Done.");
    }

    public void reload(CommandSender sender) {
        long start = System.currentTimeMillis();

        consoleOutput.addListener(sender);
        consoleOutput.info("Reloading..");

        // Load again
        cfg.reload();
        loadOptions();

        checkHooks();

        Language.load();

        attributeHandler.load();
        consoleOutput.info("Loaded " + attributeHandler.getAttributeCache().size() + " attribute(s)..");

        cooldownHandler.getCooldownCache().clear();

        consoleOutput.addListener(sender);

        Language.RELOAD.getPrefixed().fill("%time%", "" + (System.currentTimeMillis() - start)).send(sender);
    }

    @Override
    public void onDisable() {
        itemHandler.saveItems();
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

    @Override
    public void saveConfig() {
        cfg.save();
    }

    @Override
    public FileConfiguration getConfig() {
        return cfg.getFileConfiguration();
    }
}