package space.devport.wertik.items;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;
import space.devport.utils.DevportUtils;
import space.devport.utils.configutil.Configuration;
import space.devport.utils.ConsoleOutput;
import space.devport.wertik.items.commands.*;
import space.devport.wertik.items.handlers.AttributeHandler;
import space.devport.wertik.items.handlers.CooldownHandler;
import space.devport.wertik.items.handlers.ItemHandler;
import space.devport.wertik.items.listeners.ItemListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {

    public static Main inst;

    // Loaded action names
    @Getter
    private List<String> actionNames = new ArrayList<>();

    public ConsoleOutput cO;

    // Handlers
    @Getter
    private ItemHandler itemHandler;

    @Getter
    private AttributeHandler attributeHandler;

    @Getter
    private CooldownHandler cooldownHandler;

    // config.yml
    @Getter
    private Configuration cfg;

    private void loadOptions() {
        cO.setDebug(getConfig().getBoolean("debug-enabled"));
        cO.setPrefix(cfg.getColored("plugin-prefix"));
    }

    @Override
    public void onEnable() {
        inst = this;

        // Setup ConsoleOutput
        DevportUtils utils = new DevportUtils(this, true);
        cO = utils.getConsoleOutput();

        // Load actions
        Arrays.stream(Action.values()).forEach(a -> actionNames.add(a.name()));

        // Load configuration and basic options
        cfg = new Configuration(this, "config");
        loadOptions();

        // Load attributes
        attributeHandler = new AttributeHandler();
        attributeHandler.load();
        cO.info("Loaded " + attributeHandler.getAttributeCache().size() + " attribute(s)..");

        // Initialize a cooldown handler
        cooldownHandler = new CooldownHandler();

        // Load items
        itemHandler = new ItemHandler();
        itemHandler.load();
        cO.info("Loaded " + itemHandler.getItems().size() + " item(s)..");

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

        cO.info("Commands and Tab Completers initialized..");
        cO.info("Done.");
    }

    public void reload(CommandSender sender) {
        long start = System.currentTimeMillis();

        cO.setCmdSender(sender);
        cO.info("Reloading..");

        // Save
        itemHandler.saveItems();

        // Load again
        cfg.reload();
        loadOptions();

        attributeHandler.load();
        cO.info("Loaded " + attributeHandler.getAttributeCache().size() + " attribute(s)..");

        cooldownHandler.reload();

        itemHandler.load();
        cO.info("Loaded " + itemHandler.getItems().size() + " item(s)..");

        cO.setCmdSender(null);

        sender.sendMessage(cO.getPrefix() + "§aDone.. reload took " + (System.currentTimeMillis() - start) + "ms.");
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