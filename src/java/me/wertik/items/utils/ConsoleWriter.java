package me.wertik.items.utils;

import me.wertik.items.Main;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleWriter {

    private boolean debug;
    private String prefix;

    private ConsoleCommandSender console;

    public ConsoleWriter() {
        debug = true;
        prefix = Main.getInstance().getDescription().getPrefix();

        console = Main.getInstance().getServer().getConsoleSender();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void info(String msg) {
        console.sendMessage("§e" + prefix + " §3>> §7" + msg);
    }

    public void warn(String msg) {
        console.sendMessage("§e" + prefix + " §3>> §c" + msg);
    }

    public void err(String msg) {
        console.sendMessage("§e" + prefix + " §3>> §4" + msg);
    }

    public void debug(String msg) {
        if (debug)
            console.sendMessage("§e" + prefix + " §3>> §e" + msg);
    }
}
