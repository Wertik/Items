package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;
import space.devport.utils.ConsoleOutput;
import space.devport.utils.DevportUtils;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.messageutil.StringUtil;
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
    // TODO: Test everything properly
    // TODO: Add PlaceholderAPI support
    // TODO: Figure out syncing item cache w file (prob save to cache & to file when updated in-game)

    @Getter
    private static ItemsPlugin instance;

    // Possible actions in lower case
    @Getter
    private final List<String> actionNames = new ArrayList<>(Arrays.asList("right_click", "left_click"));

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

        // Load configuration and basic options
        cfg = new Configuration(this, "config");
        loadOptions();

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

        // Save
        itemHandler.saveItems();

        // Load again
        cfg.reload();
        loadOptions();

        attributeHandler.load();
        consoleOutput.info("Loaded " + attributeHandler.getAttributeCache().size() + " attribute(s)..");

        cooldownHandler.getCooldownCache().clear();

        itemHandler.loadItems();
        consoleOutput.info("Loaded " + itemHandler.getItems().size() + " item(s)..");

        consoleOutput.addListener(sender);

        sender.sendMessage(consoleOutput.getPrefix() + StringUtil.color("&aDone.. reload took " + (System.currentTimeMillis() - start) + "ms."));
    }

    @Override
    public void onDisable() {
        itemHandler.saveItems();
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