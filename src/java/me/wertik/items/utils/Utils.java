package me.wertik.items.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

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

    public static List<String> parseList(String modifier, List<String> list, Player player) {
        List<String> outPut = new ArrayList<>();
        list.forEach(line -> outPut.add(parse(modifier, line, player)));
        return outPut;
    }

    public static List<String> parseList(List<String> list, Player player) {
        return parseList("", list, player);
    }

    public static String listToString(List<String> list, String splitter, String ifEmpty) {
        String stringList = ifEmpty;
        if (!list.isEmpty()) {
            stringList = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                stringList = list.get(i) + splitter + stringList;
            }
        }
        return stringList;
    }

    public static String hashMapToString(Map<Enchantment, Integer> hashMap, String splitter, String dots, String ifEmpty) {
        String hashString = ifEmpty;
        if (!hashMap.isEmpty()) {
            hashString = new ArrayList<>(hashMap.keySet()).get(0).getName() + dots + new ArrayList<>(hashMap.values()).get(0).toString();
            for (int i = 1; i < hashMap.size(); i++) {
                Enchantment key = new ArrayList<>(hashMap.keySet()).get(i);
                hashString = hashString + splitter + key.getName() + dots + hashMap.get(key).toString();
            }
        }
        return hashString;
    }

    public static List<String> listFromString(String string, String splitter) {
        List<String> list = new ArrayList<>();
        String[] args = string.split(splitter);
        for (String arg : args) {
            list.add(arg);
        }
        return list;
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
