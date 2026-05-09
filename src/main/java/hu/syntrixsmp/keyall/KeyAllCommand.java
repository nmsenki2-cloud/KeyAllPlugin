package hu.syntrixsmp.keyall;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class KeyAllCommand implements CommandExecutor, TabCompleter {

    private final KeyAllPlugin plugin;

    public KeyAllCommand(KeyAllPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        KeyAllManager manager = plugin.getManager();

        if (!sender.hasPermission("keyall.admin")) {
            send(sender, "&cNincs jogosultságod ehhez!");
            return true;
        }

        // /keyall - bekapcsol vagy állapot
        if (args.length == 0) {
            if (!manager.isActive()) {
                manager.start();
                send(sender, "&a&l✔ &aKeyAll bekapcsolva! Minden 1 órában mindenki kap kulcsot.");
            } else {
                send(sender, "&6[KeyAll] &eÁllapot: &aAktív");
                send(sender, "&7Következő kulcs: &e" + manager.getRemainingDisplay());
            }
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "stop":
                if (manager.isActive()) {
                    manager.stop();
                    send(sender, "&c&l✖ &cKeyAll leállítva!");
                } else {
                    send(sender, "&cA KeyAll már ki van kapcsolva.");
                }
                break;

            case "now":
                manager.giveNow();
                send(sender, "&aKulcs sikeresen kiosztva minden online játékosnak!");
                break;

            case "reload":
                plugin.reloadConfig();
                send(sender, "&aConfig újratöltve!");
                break;

            case "help":
            default:
                send(sender, "&6&l=== KeyAll Parancsok ===");
                send(sender, "&e/keyall &7- Bekapcsolás / állapot");
                send(sender, "&e/keyall stop &7- Leállítás");
                send(sender, "&e/keyall now &7- Azonnali kiosztás");
                send(sender, "&e/keyall reload &7- Config újratöltés");
                send(sender, "&e/keyall help &7- Súgó");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("stop", "now", "reload", "help");
        }
        return List.of();
    }

    private void send(CommandSender sender, String message) {
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        sender.sendMessage(component);
    }
}
