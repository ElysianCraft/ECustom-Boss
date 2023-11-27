package fr.elysiancraft.boss.commands;

import fr.elysiancraft.boss.Boss;
import fr.elysiancraft.boss.boss.AxolotlBoss;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AxolotBossCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Boss.getInstance().addBoss(new AxolotlBoss("Axolot Affam\u00E9", ((Player) sender).getWorld(), ((Player) sender).getLocation()));
        return false;
    }
}