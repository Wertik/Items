package space.devport.wertik.items.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import space.devport.wertik.items.ItemsPlugin;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtil {

    public String listToString(List<String> list, String splitter, String ifEmpty) {
        StringBuilder stringList = ifEmpty == null ? new StringBuilder("§cNaN") : new StringBuilder(ifEmpty);

        if (list != null)
            if (!list.isEmpty()) {
                stringList = new StringBuilder(list.get(0));

                for (int i = 1; i < list.size(); i++)
                    stringList.insert(0, list.get(i) + splitter);
            }
        return stringList.toString();
    }

    public List<String> parsePlaceholders(List<String> list, Player player) {
        return ItemsPlugin.getInstance().isUsePlaceholderAPI() ? list.stream()
                .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                .collect(Collectors.toList()) : list;
    }
}