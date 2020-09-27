package space.devport.wertik.items;

import space.devport.utils.DevportPlugin;
import space.devport.utils.text.language.LanguageDefaults;

public class ItemsLanguage extends LanguageDefaults {

    public ItemsLanguage(DevportPlugin plugin) {
        super(plugin);
    }

    @Override
    public void setDefaults() {

        /*
         * General
         **/

        addDefault("List-Splitter", "&f, &7");

        /*
         * General commands
         **/

        addDefault("Commands.Help.Header", "&8&m        &r &e%pluginName% &7v&f%version% &8&m        ");
        addDefault("Commands.Help.Sub-Command-Line", "&e%usage% &8- &7%description%");

        addDefault("No-Permissions", "&cYou don't have permission to do this.");
        addDefault("Only-Players", "&cAction has to be done in-game.");
        addDefault("Player-Offline", "&cThat player is not online.");
        addDefault("Cannot-Help-With-Air", "&cCannot help you with air.");
        addDefault("Reload-Done", "&7Done... reload took &f%time%&7ms.", "Reload does not manipulate with item storage, to load/save them, do '/items load/save'.");
        addDefault("Only-Console", "&cThis command can only be done in console.");
        addDefault("Only-Operator", "&cThis command can only be done by operators.");
        addDefault("Too-Many-Arguments", "&cToo many arguments.", "&cUsage: &7%usage%");
        addDefault("Not-Enough-Arguments", "&cNot enough arguments.", "&cUsage: &7%usage%");
        addDefault("Not-A-Number", "&f%param% &cis not a number.");

        /*
         * Utility commands
         * */

        addDefault("NBT-List", "&7NBT:");
        addDefault("NBT-List-Line", " &8- &7%key% &f= &7%value%");

        addDefault("No-NBT", "&cThis item doesn't have any NBT data.");
        addDefault("No-Key", "&cThis item doesn't have the key &f%key%");
        addDefault("NBT-Added", "&7Added &f%key% &7= &f%value% &7to the item.");
        addDefault("NBT-Removed", "&7Removed &f%key% &7from the item.");
        addDefault("NBT-Cleared", "&7All custom NBT data cleared.");

        addDefault("Lore-List", "&7Lore:");
        addDefault("Lore-List-Line", "&f%index% &8- &r%line%");

        addDefault("Line-Added", "&7Line added to item lore.");
        addDefault("Line-Removed", "&7Line removed from item lore.");
        addDefault("Lore-Cleared", "&7Lore cleared.");
        addDefault("No-Lore", "&cItem doesn't have a lore.");
        addDefault("Index-Out-Of-Bounds", "&cIndex &f%param% &cis out of bounds. Max: &7%max%");

        addDefault("Flags-List", "&7Flags: %flags%");

        addDefault("No-Flags", "&cNo flags.");
        addDefault("Flag-Added", "&7Flag added to item.");
        addDefault("Flag-Removed", "&7Flag removed from item.");
        addDefault("Flags-Cleared", "&7Flags cleared.");
        addDefault("Flag-Invalid", "&cFlag &f%flag% &cis invalid.");
        addDefault("No-Flag", "&cItem doesn't have this flag.");

        addDefault("Enchants-List", "&7Enchants: &f%enchants%");
        addDefault("Enchants-List-Line", "&8- &7%enchantment% &f%level%");

        addDefault("No-Enchants", "&cThis item has no enchantments.");
        addDefault("Enchant-Added", "&7Enchant added to item.");
        addDefault("Enchant-Removed", "&7Enchant removed from item.");
        addDefault("Enchants-Cleared", "&7Enchants cleared from item.");
        addDefault("Invalid-Enchant", "&cEnchantment is invalid.");

        addDefault("Set-Unstackable", "&7Unstackable property set to &f%state%&7.");
        addDefault("Set-Unplaceable", "&7Unplaceable property set to &f%state%&7.");
        addDefault("Set-Uncraftable", "&7Uncraftable property set to &f%state%&7.");

        addDefault("Item-Renamed", "&7Item renamed.");

        addDefault("Invalid-Material", "&cMaterial &f%param% &cis not valid.");
        addDefault("Material-Changed", "&7Material changed to &f%material%");

        /*
         * Item commands
         * */

        addDefault("No-Items-Saved", "&cNo items saved.");
        addDefault("Spawned-At", "&7Item &f%item%&7x&f%amount% &7spawned &8@ &7%location%");
        addDefault("Items-List", "&7Items: %items%");
        addDefault("Item-Updated", "&7Item &f%item% &7updated.");
        addDefault("Item-Removed", "&7Item &f%item% &7removed.");
        addDefault("Items-Loaded", "&7Items loaded from storage.");
        addDefault("Item-Saved", "&7Item &f%item% &7saved to storage.");
        addDefault("Item-Loaded", "&7Item &f%item% &7loaded from storage.");
        addDefault("Item-Not-Valid", "&cItem &f%item% does not exist.");
        addDefault("Item-Given", "&7Gave player &f%player% &7item &e%item%&7x&f%amount%");
        addDefault("Item-Given-Raw", "&7Gave player &f%player% &7raw item &e%item%&7x&f%amount%");

        /*
         * Attribute commands
         * */

        addDefault("Attributes-List", "&7Attributes:");
        addDefault("Attributes-List-Line", "&8- &7%action% &f= &7%attribute%");
        addDefault("No-Attributes", "&cNo attributes.");
        addDefault("Attributes", "&7Attributes: %attributes%");
        addDefault("Attribute-Added", "&7Attribute &f%attribute% &7added to item.");
        addDefault("Attribute-Removed", "&7Attribute &f%attribute% &7removed from item.");
        addDefault("Could-Not-Remove-Attribute", "&cCould not remove attribute.");
        addDefault("Attribute-Invalid-Param", "&cItem doesn't have the attribute &f%param%");
        addDefault("Attribute-Invalid", "&cAttribute &f%attribute% &cis not valid.", "&cValid attributes: &7%valid%");
        addDefault("Attributes-Cleared", "&7Attributes cleared.");
        addDefault("Click-Type-Invalid", "&cAction &f%action% &cis not valid.", "&cValid actions: &7%valid%");

        /*
         * Cooldown
         * */

        addDefault("Cooldown-Expired", "&7Cooldown on attribute %attribute has expired.");
        addDefault("Item-Cooldown", "&7Attribute is still on cooldown, wait &f%time%&7s.");

        /*
         * Use limit
         * */

        addDefault("Item-Use-Limit", "&cYou have reached this items maximum amount of uses.");
        addDefault("Unlimited", "&7Unlimited");
    }
}