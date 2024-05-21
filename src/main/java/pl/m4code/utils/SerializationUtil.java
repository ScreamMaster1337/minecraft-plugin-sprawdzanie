package pl.m4code.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SerializationUtil {


    public static String serializeObject(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(object);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object deserializeObject(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Object object = dataInput.readObject();
            dataInput.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeLocation(Location location) {
        return location
                .getWorld().getName() + ";" + location.getWorld().getName() + ";" + location
                .getX() + ";" + location
                .getY() + ";" + location
                .getZ() + ";" + location
                .getYaw();
    }

    public static Location deserializeLocation(String data) {
        String[] split = data.split(";");
        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5]));
    }
}
