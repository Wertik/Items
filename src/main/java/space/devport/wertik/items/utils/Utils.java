package space.devport.wertik.items.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Utils {

    public String mapToString(Map<?, ?> map, String splitter, String separator, String ifEmpty) {
        StringBuilder str = ifEmpty == null ? null : new StringBuilder(ifEmpty);

        if (!map.isEmpty()) {
            str = new StringBuilder();
            for (Object key : map.keySet()) {
                str.append(key.toString()).append(separator).append(map.get(key).toString()).append(splitter);
            }
        }

        return str == null ? null : str.toString();
    }

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
}