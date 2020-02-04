package me.wertik.items.utils;

import me.wertik.items.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Utils {

    private static Random random = new Random();

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static String locationToString(Location loc) {
        return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
    }

    public static Location stringToLocation(String str) {
        String[] strar = str.split(";");

        Location newLoc = new Location(Bukkit.getWorld(strar[0]), Double.parseDouble(strar[1]), Double.parseDouble(strar[2]), Double.parseDouble(strar[3]));

        return newLoc.clone();
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

    public static ItemBuilder loadBuilder(FileConfiguration yaml, String path) {
        try {
            ConfigurationSection section = yaml.getConfigurationSection(path);

            String type = section.getString("type");
            Material mat = Material.valueOf(type.split(";")[0]);

            byte data = 0;

            if (type.contains(";"))
                if (type.split(";").length > 1)
                    data = Byte.parseByte(type.split(";")[1]);

            ItemBuilder b = new ItemBuilder(mat).data(data);

            if (section.contains("name"))
                b.displayName(section.getString("name"));

            if (section.contains("glow"))
                b.setGlow(section.getBoolean("glow"));

            if (section.contains("lore"))
                b.lore(section.getStringList("lore"));

            return b;
        } catch (Exception e) {
            Main.getInstance().cO.warn("Could not get the Item Builder, i've failed you.. :'(");
            if (Main.getInstance().cO.isDebug())
                e.printStackTrace();
            return null;
        }
    }

    public static List<String> color(List<String> list) {
        List<String> out = new ArrayList<>();
        for (String line : list)
            out.add(color(line));
        return out;
    }

    public static ItemStack loadItem(FileConfiguration yaml, String path) {
        return loadBuilder(yaml, path).build();
    }

    public static String listToMessage(List<String> list) {
        StringBuilder str = new StringBuilder(list.get(0));

        for (int i = 1; i < list.size(); i++)
            str.append("\n").append(list.get(i));

        return str.toString();
    }

    public static List<String> parse(List<String> list, Player player, Player killer, double worth) {
        List<String> out = new ArrayList<>();

        for (String line : list)
            out.add(Utils.parse(line, player, killer, worth));

        return out;
    }

    public static List<String> parse(List<String> list, Player player, double worth) {
        List<String> out = new ArrayList<>();

        for (String line : list)
            out.add(Utils.parse(line, player, worth));

        return out;
    }

    public static String parse(String str, Player player, double worth) {
        str = str.replace("%balanceWorth%", String.valueOf(worth));
        return parse(str, player);
    }

    public static String parse(String str, Player player, Player killer, double worth) {
        str = parse("Killer", str, killer);
        str = parse(str, player);

        str = str.replace("%balanceWorth%", String.valueOf(worth));

        return str;
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
        return stringList == null ? null : stringList.toString();
    }

    public static List<String> stringToList(String string) {
        List<String> list = new ArrayList<>();
        if (string != null)
            for (String str : string.split(","))
                list.add(str.trim());
        return list;
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

    public static List<String> weaponData(ItemStack item) {
        List<String> data = new ArrayList<>();

        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName())
                data.add("  " + item.getItemMeta().getDisplayName() + " (" + item.getType().name() + ")");
            else
                data.add("  " + item.getType().name() + " (" + item.getType().name() + ")");

            if (item.getItemMeta().hasLore())
                for (String line : item.getItemMeta().getLore())
                    data.add("   " + line);

            if (item.getItemMeta().hasEnchants())
                for (Enchantment enchant : item.getEnchantments().keySet())
                    data.add("   " + enchant.toString() + " - " + item.getEnchantments().get(enchant));
        } else
            data.add("  " + item.getType().name() + " (" + item.getType().name() + ")");

        return data;
    }

    /**
     * Calculates a player's total exp based on level and progress to next.
     *
     * @param player the Player
     *
     * @return the amount of exp the Player has
     */
    public static int getExp(Player player) {
        return getExpFromLevel(player.getLevel())
                + Math.round(getExpToNext(player.getLevel()) * player.getExp());
    }

    /**
     * Calculates total experience based on level.
     *
     * "One can determine how much experience has been collected to reach a level using the equations:
     *
     *  Total Experience = [Level]2 + 6[Level] (at levels 0-15)
     *                     2.5[Level]2 - 40.5[Level] + 360 (at levels 16-30)
     *                     4.5[Level]2 - 162.5[Level] + 2220 (at level 31+)"
     *
     * @param level the level
     *
     * @return the total experience calculated
     */
    public static int getExpFromLevel(int level) {
        if (level > 30) {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
        if (level > 15) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return level * level + 6 * level;
    }

    /**
     * Calculates level based on total experience.
     *
     * @param exp the total experience
     *
     * @return the level calculated
     */
    public static double getLevelFromExp(long exp) {
        if (exp > 1395) {
            return (Math.sqrt(72 * exp - 54215) + 325) / 18;
        }
        if (exp > 315) {
            return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
        }
        if (exp > 0) {
            return Math.sqrt(exp + 9) - 3;
        }
        return 0;
    }

    private static int getExpToNext(int level) {
        if (level > 30) {
            return 9 * level - 158;
        }
        if (level > 15) {
            return 5 * level - 38;
        }
        return 2 * level + 7;
    }

    /**
     * Change a Player's exp.
     * <p>
     * This method should be used in place of {@link Player#giveExp(int)}, which does not properly
     * account for different levels requiring different amounts of experience.
     *
     * @param player the Player affected
     * @param exp the amount of experience to add or remove
     */
    public static void changeExp(Player player, int exp) {
        exp += getExp(player);

        if (exp < 0) {
            exp = 0;
        }

        double levelAndExp = getLevelFromExp(exp);

        int level = (int) levelAndExp;
        player.setLevel(level);
        player.setExp((float) (levelAndExp - level));
    }

    public static boolean rollTheDice(double chance) {
        return random.nextDouble() < chance;
    }
}
