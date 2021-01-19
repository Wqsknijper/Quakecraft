package dev.nidocraft.net.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class SpawnUtils {

    public static List<Location> spawns = new ArrayList<>();

    public static void addSpawn(Location loc){
        spawns.add(loc);
    }

    public static void generateSpawns(int minX, int maxX, int minZ, int maxZ, int minY, int spawnAmount) {
        List<Location> locations = new ArrayList<>();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                int y = Bukkit.getWorld("world").getHighestBlockYAt(x, z);
                if (y > minY) {
                    locations.add(new Location(Bukkit.getWorld("world"), x + 0.5, y + 2, z + 0.5, 0F, 0F));
                }
            }
        }
        for (int i = 1; i <= spawnAmount; i++) {
            spawns.add(locations.get((int) Math.round(Math.random() * (locations.size() - 1))));
        }
    }
}
