package me.wertik.items;

import me.wertik.items.commands.*;
import me.wertik.items.handlers.AttributeHandler;
import me.wertik.items.handlers.CooldownHandler;
import me.wertik.items.handlers.ItemHandler;
import me.wertik.items.listeners.ItemListener;
import me.wertik.items.utils.ConsoleWriter;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    private static Main instance;

    private List<String> actionNames;

    public ConsoleWriter cw;

    private ConfigLoader configLoader;
    private ItemHandler itemHandler;
    private AttributeHandler attributeHandler;
    private CooldownHandler cooldownHandler;

    public static Main getInstance() {
        return instance;
    }

    // Todo items.yml -->> data.yml + add cooldown saves

    @Override
    public void onEnable() {
        instance = this;

        cw = new ConsoleWriter();

        configLoader = new ConfigLoader();
        itemHandler = new ItemHandler();
        attributeHandler = new AttributeHandler();
        cooldownHandler = new CooldownHandler();

        actionNames = new ArrayList<>();

        for (Action a : Action.values())
            actionNames.add(a.name().toLowerCase());

        actionNames.forEach(name -> cw.debug(name));

        cw.setDebug(configLoader.getConfig().getBoolean("debug"));

        cw.info("Classes loaded.");

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

        cw.info("Commands loaded.");

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        cw.info("Listeners enabled.");
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

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    public AttributeHandler getAttributeHandler() {
        return attributeHandler;
    }
}
