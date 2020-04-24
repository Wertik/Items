package space.devport.wertik.items;

import space.devport.utils.text.language.LanguageDefaults;

public class ItemsLanguage extends LanguageDefaults {

    @Override
    public void setDefaults() {

        /*
         * General commands
         **/

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

        addDefault("Line-Added", "&7Line added to item lore.");
        addDefault("Line-Removed", "&7Line removed from item lore.");
        addDefault("Lore-Cleared", "&7Lore cleared.");
        addDefault("No-Lore", "&cItem doesn't have a lore.");
        addDefault("Index-Out-Of-Bounds", "&cIndex &f%param% &cis out of bounds. Max: &7%max%");
        addDefault("Flags-List", "&7Flags: %flags%");
        addDefault("Flag-Added", "&7Flag added to item.");
        addDefault("Flag-Removed", "&7Flag removed from item.");
        addDefault("Flags-Cleared", "&7Flags cleared.");
        addDefault("Flag-Invalid", "&cFlag &f%flag% &cis invalid.");
        addDefault("No-Flag", "&cItem doesn't have this flag.");
        addDefault("Enchants-List", "&7Enchants: &f%enchants%");
        addDefault("Enchant-Added", "&7Enchant added to item.");
        addDefault("Enchant-Removed", "&7Enchant removed from item.");
        addDefault("Enchants-Cleared", "&7Enchants cleared from item.");
        addDefault("Invalid-Enchant", "&cEnchantment is invalid.");
        addDefault("Item-Renamed", "&7Item renamed.");
        addDefault("Set-Unstackable", "&7Unstackable property set to &f%state%&7.");
        addDefault("Set-Unplaceable", "&7Unplaceable property set to &f%state%&7.");
        addDefault("Invalid-Material", "&cMaterial &f%param% &cis not valid.");
        addDefault("Material-Changed", "&7Material changed to &f%material%");

        /*
         * Item commands
         * */

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