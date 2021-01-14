package dev.nidocraft.net.Utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MathUtils {

    public static float toDegree(double angle) {
        return (float) Math.toDegrees(angle);
    }

    public static Vector getVector(Entity entity) {
        if (entity instanceof Player) return ((Player) entity).getEyeLocation().toVector();
        else return entity.getLocation().toVector();
    }

    public static void deathMode(Player victim, Player attacker) {
        Vector direction = getVector(victim).subtract(getVector(attacker)).normalize();
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();

        Location changed = attacker.getLocation().clone();
        changed.setYaw(180 - toDegree(Math.atan2(x, z)));
        changed.setPitch(90 - toDegree(Math.acos(y)));

        victim.teleport(changed);
    }
}
