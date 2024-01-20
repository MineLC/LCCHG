package lc.chg.commands;

import lc.chg.LCCHG;
import lc.chg.configuration.Translation;
import lc.chg.game.BGFBattle;
import lc.chg.game.GameState;
import lc.chg.game.utils.BGChat;
import lc.core.entidades.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BGConsole implements CommandExecutor {
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player p = (Player)sender;
        Jugador jug = Jugador.getJugador(p);
        if (cmd.getName().equalsIgnoreCase("start")) {
            if (p.hasPermission("chg.forcestart")) {
                if (LCCHG.GAMESTATE != GameState.PREGAME) {
                    msg(p, sender, ChatColor.RED + Translation.GAME_BEGUN.t());
                } else {
                    LCCHG.startgame();
                }
            } else {
                msg(p, sender, ChatColor.RED + Translation.NO_PERMISSION.t());
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("settimeout")) {
            int time = Integer.parseInt(args[0]);
            LCCHG.COUNTDOWN = Integer.valueOf(time * 20);
            msg(null, sender, ChatColor.YELLOW + "Tiempo actualizado a " + time + "segundos");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("fbattle")) {
            if (p.hasPermission("chg.forcefinalbattle")) {
                if (LCCHG.GAMESTATE == GameState.GAME) {
                    if (LCCHG.END_GAME.booleanValue()) {
                        LCCHG.END_GAME = Boolean.valueOf(false);
                        BGFBattle.createBattle();
                        return true;
                    }
                    BGChat.printPlayerChat(p, ChatColor.RED + "No tienes permiso!");
                    return true;
                }
                BGChat.printPlayerChat(p, "El juego no ha comenzado!");
                return true;
            }
            BGChat.printPlayerChat(p, ChatColor.RED + Translation.NO_PERMISSION.t());
            return true;
        }
        return true;
    }

    private static void msg(Player p, CommandSender s, String msg) {
        if (p == null) {
            s.sendMessage(msg);
        } else {
            BGChat.printPlayerChat(p, msg);
        }
    }
}
