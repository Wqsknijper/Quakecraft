package dev.nidocraft.net.Gamemanager;

import dev.nidocraft.net.Main;
import dev.nidocraft.net.Utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Map;

public class SbManager {

    public static void drawScoreboard(Player p) {
        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard b = m.getNewScoreboard();

        Objective o = b.registerNewObjective("Gold", "");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("§9KweekCraft met Brend");

        o.getScore("§a").setScore(1);
        Integer i = 1;
        for (Map.Entry<Player, Integer> topPlayer : PlayerUtils.getTop()) {
            if (i - 1 != (int) Main.getValueFromPath("max-on-scoreboard")) break;
            i++;
            String topPlayerName = topPlayer.getKey().getName();
            o.getScore("§a").setScore(i);
        }
        o.getScore("§b").setScore(i + 1);
        o.getScore("§bEnd Game: §f" + (System.currentTimeMillis() - GameCache.startTime) / 100).setScore(i + 2);
        o.getScore("§c").setScore(i + 3);
        o.getScore("§bPort = 3001").setScore(i + 4);

        p.setScoreboard(b);
    }

}
