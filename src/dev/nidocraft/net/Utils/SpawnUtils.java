package dev.nidocraft.net.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpawnUtils {

    public static HashMap<Integer, Location> spawns = new HashMap<>();

    public static void addSpawn(Integer i, Location loc){
        spawns.put(i, loc);
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
            addSpawn(i, locations.get((int) Math.round(Math.random() * (locations.size() - 1))));
        }
    }

    public static ArrayList getRSpawn(Integer amount){
        ArrayList<Location> r = new ArrayList<>();
        for(int i = 0; i <= amount; i++) {
            r.add(spawns.get(Math.round(Math.random() * (spawns.size() - 1))));
        }
        return r;
    }

}
