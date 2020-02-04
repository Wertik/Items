package me.wertik.items;

import me.wertik.items.commands.*;
import me.wertik.items.handlers.AttributeHandler;
import me.wertik.items.handlers.CooldownHandler;
import me.wertik.items.handlers.ItemHandler;
import me.wertik.items.listeners.ItemListener;
import me.wertik.items.utils.Configuration;
import me.wertik.items.utils.ConsoleOutput;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    private List<String> actionNames;

    public ConsoleOutput cO;

    private ItemHandler itemHandler;
    private AttributeHandler attributeHandler;
    private CooldownHandler cooldownHandler;

    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;

        cO = new ConsoleOutput(this);

        // Load configuration

        config = new Configuration(this, "config");

        cO.setDebug(getConfig().getBoolean("debug"));
        cO.setPrefix(config.getColored("prefix"));

        // Load actions

        actionNames = new ArrayList<>();

        for (Action a : Action.values())
            actionNames.add(a.name().toLowerCase());

        actionNames.forEach(name -> cO.debug(name));

        // Load attributes

        attributeHandler = new AttributeHandler();
        attributeHandler.loadAttributes();

        // Load cooldowns

        cooldownHandler = new CooldownHandler();

        // Load items

        itemHandler = new ItemHandler();
        itemHandler.loadItems();

        cO.info("Classes loaded.");

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        cO.info("Listeners enabled.");

        UtilCommands utilCommands = new UtilCommands();
        UtilTabCompleter utilTabCompleter = new UtilTabCompleter();

        getCommand("items").setExecutor(new ItemsCommand());
        getCommand("items").setTabCompleter(new ItemsTabCompleter());

        List<String> commands = new ArrayList<>(getDescription().getCommands().keySet());

        getCommand("attribute").setExecutor(new AttCommands());
        getCommand("attribute").setTabCompleter(new AttTabCompleter());

        for (int i = 2; i < commands.size(); i++) {
            getCommand(commands.get(i)).setExecutor(utilCommands);
            getCommand(commands.get(i)).setTabCompleter(utilTabCompleter);
        }

        cO.info("Commands loaded.");
    }

    public void reload(CommandSender sender) {
        long start = System.currentTimeMillis();

        cO.info("Reloading..");
        cO.setReloadSender(sender);

        // Save
        itemHandler.saveItems();

        // Load again

        config.reload();

        cO.setDebug(getConfig().getBoolean("debug"));
        cO.setPrefix(config.getColored("prefix"));

        actionNames = new ArrayList<>();

        for (Action a : Action.values())
            actionNames.add(a.name().toLowerCase());

        actionNames.forEach(name -> cO.debug(name));

        cO.info("Actions loaded..");

        attributeHandler.loadAttributes();

        cO.info("Loaded " + attributeHandler.getAttributes().size() + " attribute(s)..");

        cooldownHandler.reload();

        itemHandler.loadItems();

        cO.info("Loaded " + itemHandler.getItems().size() + " item(s)..");

        long stop = System.currentTimeMillis();

        cO.setReloadSender(null);

        sender.sendMessage("§aDone.. reload took " + (stop - start) + "ms.");
    }

    @Override
    public void onDisable() {
        itemHandler.saveItems();
    }

    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }

    public List<String> getActionNames() {
        return actionNames;
    }

    public void saveConfig() {
        config.save();
    }

    public Configuration getCfg() {
        return config;
    }

    public FileConfiguration getConfig() {
        return config.getYaml();
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    public AttributeHandler getAttributeHandler() {
        return attributeHandler;
    }
}
