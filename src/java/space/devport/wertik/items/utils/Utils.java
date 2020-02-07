package space.devport.wertik.items.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }

    public static String parse(String string, Player player) {
        return parse("", string, player);
    }

    public static String parse(String modifier, String string, Player player) {
        string = string.replace("%player" + modifier + "%", player.getName());
        string = string.replace("%player" + modifier + "Display%", player.getDisplayName());
        //string = string.replace("%player" + modifier + "Suffix%", Main.getChat().getPlayerSuffix(player));
        //string = string.replace("%player" + modifier + "Prefix%", Main.getChat().getPlayerPrefix(player));
        string = string.replace("%player" + modifier + "MaxHP%", String.valueOf(player.getMaxHealth()));
        string = string.replace("%player" + modifier + "HP%", String.valueOf(player.getHealth()));
        string = string.replace("%player" + modifier + "X%", String.valueOf(((int) player.getLocation().getX())));
        string = string.replace("%player" + modifier + "Y%", String.valueOf((int) player.getLocation().getY()));
        string = string.replace("%player" + modifier + "Z%", String.valueOf((int) player.getLocation().getZ()));
        string = string.replace("%player" + modifier + "World%", String.valueOf(player.getLocation().getWorld()));
        string = string.replace("%player" + modifier + "Food%", String.valueOf(player.getFoodLevel()));
        string = string.replace("%player" + modifier + "Level%", String.valueOf(player.getLevel()));

        return string;
    }

    public static List<String> color(List<String> list) {
        List<String> out = new ArrayList<>();
        for (String line : list)
            out.add(color(line));
        return out;
    }

    public static String listToMessage(List<String> list) {
        return String.join("\n", list);
    }

    public static String mapToString(Map<?, ?> map, String splitter, String separator, String ifEmpty) {
        StringBuilder str = ifEmpty == null ? null : new StringBuilder(ifEmpty);

        if (!map.isEmpty()) {
            str = new StringBuilder();
            for (Object key : map.keySet()) {
                str.append(key.toString()).append(separator).append(map.get(key).toString()).append(splitter);
            }
        }

        return str == null ? null : str.toString();
    }

    public static String listToString(List<String> list, String splitter, String ifEmpty) {
        StringBuilder stringList = ifEmpty == null ? new StringBuilder("§cNaN") : new StringBuilder(ifEmpty);

        if (list != null)
            if (!list.isEmpty()) {
                stringList = new StringBuilder(list.get(0));

                for (int i = 1; i < list.size(); i++)
                    stringList.insert(0, list.get(i) + splitter);
            }
        return stringList.toString();
    }

    public static String formatTime(long time, String zero) {
        if (time < 0)
            return color(zero);

        if (time >= 3600)
            return time / 3600 + "h " + (time % 3600) / 60 + "m " + (time % 3600) % 60 + "s";
        else if (time >= 60)
            return time / 60 + "m " + time % 60 + "s";
        else
            return time + "s";
    }

    public static double round(double value, int o) {
        StringBuilder str = new StringBuilder("#.");

        for (int i = 0; i < o; i++) {
            str.append("#");
        }

        DecimalFormat format = new DecimalFormat(str.toString());

        return Double.parseDouble(format.format(value).replace(",", "."));
    }

    // Base64
    public static String itemStackToBase64(ItemStack item) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save every element in the list
            dataOutput.writeObject(item);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IllegalStateException | IOException e) {
            Bukkit.getLogger().warning("§cCould not save the item stack.. i've failed you.. :(");
        }
        return null;
    }

    public static ItemStack itemStackFromBase64(String data) {
        ItemStack item = null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            // Read the serialized inventory
            item = (ItemStack) dataInput.readObject();

            dataInput.close();
            return item;
        } catch (ClassNotFoundException | IOException e) {
            Bukkit.getLogger().warning("§cCould not get the item stack.. i've failed you.. :(");
        }
        return item;
    }
}