package dev.nidocraft.net.Gamemanager;

import dev.nidocraft.net.Enums.GameStates;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCache {

    public static GameStates state;
    public static List<Player> players = new ArrayList<>();
    public static Long waitings;
    public static ArrayList<Integer> seconds = new ArrayList<>();
}
