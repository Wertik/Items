package me.wertik.items.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {

    private Material type;
    private byte data = 0;

    private String displayName;

    private int amount;

    private List<String> lore;

    private boolean glow;

    private HashMap<Enchantment, Integer> enchants;

    private List<ItemFlag> flags;

    private HashMap<String, String> NBTData;

    private ItemBuilder save;

    public ItemBuilder(Material type) {
        this.type = type;
        this.amount = 1;

        lore = new ArrayList<>();
        glow = false;
        enchants = new HashMap<>();
        flags = new ArrayList<>();
        NBTData = new HashMap<>();
    }

    @Override
    public ItemBuilder clone() {
        ItemBuilder cloned = new ItemBuilder(this.build());
        return cloned;
    }

    public void save(ItemBuilder b) {
        save = b.clone();
    }

    public ItemBuilder(ItemStack item) {
        this.type = item.getType();

        this.data = (byte) item.getDurability();

        this.amount = item.getAmount();

        this.lore = new ArrayList<>();
        this.enchants = new HashMap<>();
        this.flags = new ArrayList<>();

        this.NBTData = new HashMap<>();

        if (NBTEditor.hasNBT(item)) {
            for (String key : NBTEditor.getNBTKeys(item)) {
                String v = NBTEditor.getNBT(item, key);
                if (v != null)
                    NBTData.put(key, NBTUtils.strip(v));
            }
        }

        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta.hasDisplayName())
                this.displayName = itemMeta.getDisplayName();

            if (itemMeta.hasLore())
                this.lore = itemMeta.getLore();

            if (itemMeta.hasEnchants())
                this.enchants = new HashMap<>(itemMeta.getEnchants());

            this.flags = new ArrayList<>(itemMeta.getItemFlags());
        }
    }

    public ItemBuilder parse(String str, String str1) {
        if (displayName != null)
            displayName = displayName.replace("%" + str + "%", str1);

        if (lore != null)
            if (!lore.isEmpty()) {
                List<String> newLore = new ArrayList<>();
                for (String line : lore)
                    newLore.add(line.replace("%" + str + "%", str1));
                lore = newLore;
            }

        return this;
    }

    public ItemBuilder remove(String str) {
        if (displayName != null)
            displayName = displayName.replace(str, "");

        if (lore != null)
            if (!lore.isEmpty()) {
                List<String> newLore = new ArrayList<>();
                for (String line : lore)
                    if (!line.contains(str))
                        newLore.add(line);
                this.lore = newLore;
            }

        return this;
    }

    public ItemBuilder undo() {
        return save;
    }

    public ItemBuilder clearEnchants() {
        this.enchants = new HashMap<>();
        return this;
    }

    public ItemBuilder clearFlags() {
        this.flags = new ArrayList<>();
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder data(byte data) {
        this.data = data;
        return this;
    }

    public ItemBuilder lore(String[] lore) {
        for (String line : lore)
            this.lore.add(line);
        return this;
    }

    public ItemBuilder addLine(String str) {
        lore.add(str);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag flag) {
        flags.add(flag);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        enchants.put(enchantment, level);
        return this;
    }

    public ItemBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemBuilder addNBT(String key, String value) {
        NBTData.put(key, value);
        return this;
    }

    public ItemBuilder type(Material mat) {
        this.type = mat;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(type, amount, data);

        ItemMeta meta = item.getItemMeta();

        if (!lore.isEmpty()) {
            // Color it
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                lore.set(i, color(line));
            }

            meta.setLore(lore);
        }

        if (displayName != null)
            meta.setDisplayName(color(displayName));

        if (!enchants.isEmpty()) {
            for (Enchantment enchantment : enchants.keySet()) {
                meta.addEnchant(enchantment, enchants.get(enchantment), true);
            }
        }

        if (!flags.isEmpty()) {
            for (ItemFlag flag : flags)
                meta.addItemFlags(flag);
        }

        if (glow) {
            meta.addEnchant(Enchantment.LUCK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        // add NBT
        for (String key : NBTData.keySet())
            item = NBTEditor.writeNBT(item, key.trim(), NBTData.get(key).trim());

        return item;
    }

    private String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public Material type() {
        return type;
    }

    public byte data() {
        return data;
    }

    public String displayName() {
        return displayName;
    }

    public int amount() {
        return amount;
    }

    public List<String> lore() {
        return lore;
    }

    public boolean glow() {
        return glow;
    }

    public HashMap<Enchantment, Integer> enchants() {
        return enchants;
    }

    public List<ItemFlag> flags() {
        return flags;
    }

    public HashMap<String, String> NBTData() {
        return NBTData;
    }

    public ItemBuilder save() {
        return save;
    }
}
