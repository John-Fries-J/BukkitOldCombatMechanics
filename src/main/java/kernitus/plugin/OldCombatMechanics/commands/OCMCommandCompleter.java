package kernitus.plugin.OldCombatMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides tab completion for OCM commands
 */
public class OCMCommandCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length < 2) {
            completions.addAll(Arrays.stream(OCMCommandHandler.Subcommand.values())
                    .filter(arg -> arg.toString().startsWith(args[0]))
                    .filter(arg -> OCMCommandHandler.checkPermissions(sender, arg))
                    .map(Enum::toString).collect(Collectors.toList()));
        } else {
            if (args[0].equalsIgnoreCase(OCMCommandHandler.Subcommand.test.toString())) {
                if (args.length < 4) {
                    completions.addAll(Bukkit.getOnlinePlayers().stream()
                            .filter(p -> {
                                if (args.length < 3) return true;
                                Player argPlayer = Bukkit.getPlayer(args[1]);
                                return argPlayer != null && argPlayer.getWorld().equals(p.getWorld());
                            })
                            .map(Player::getName)
                            .filter(arg -> arg.startsWith(args[args.length - 1])
                                    && (args.length < 3 || !arg.equalsIgnoreCase(args[1])))
                            .collect(Collectors.toList()));
                }
            } else if (args[0].equalsIgnoreCase(OCMCommandHandler.Subcommand.toggle.toString())) {
                if (args.length < 3) {
                    completions.addAll(Bukkit.getOnlinePlayers().stream()
                            .map(Player::getName)
                            .filter(arg -> arg.startsWith(args[1]))
                            .collect(Collectors.toList()));
                } else {
                    completions.addAll(Stream.of("on", "off").filter(arg -> arg.startsWith(args[2])).collect(Collectors.toList()));
                }
            }
        }

        return completions;
    }
}