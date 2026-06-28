package hu.syntrixsmp.keyall;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
public class KeyAllManager {
    private final KeyAllPlugin plugin;
    private BukkitTask task;
    private boolean active = false;
    private int secondsRemaining = 0;
    private String remainingDisplay = "Inaktiv";
    public KeyAllManager(KeyAllPlugin plugin) {
        this.plugin = plugin;
    }
    // Automatikus indítás szerver bootkor - broadcast nélkül
    public void autoStart() {
        if (active) return;
        active = true;
        secondsRemaining = plugin.getConfig().getInt("interval", 3600);
        startTimer();
        plugin.getLogger().info("[KeyAll] Automatikusan elindult! Következő kulcs: 1 óra múlva.");
    }
    public void start() {
        if (active) return;
        active = true;
        secondsRemaining = plugin.getConfig().getInt("interval", 3600);
        startTimer();
        broadcast(plugin.getConfig().getString("messages.keyall-started", "&6&l[KeyAll] &eElindult!"));
    }
    public void stop() {
        active = false;
        remainingDisplay = "Inaktiv";
        if (task != null) {
            task.cancel();
            task = null;
        }
        broadcast(plugin.getConfig().getString("messages.keyall-stopped", "&6&l[KeyAll] &eLeallitva."));
    }
    public void giveNow() {
        broadcast(plugin.getConfig().getString("messages.keyall-now", "&6&l[KeyAll] &eAzonnali kiosztás!"));
        giveKeys();
        if (active) {
            secondsRemaining = plugin.getConfig().getInt("interval", 3600);
        }
    }
    private void startTimer() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!active) return;
            if (secondsRemaining <= 0) {
                broadcast(plugin.getConfig().getString("messages.broadcast-key",
                        "&6&l[KeyAll] &eMindenki kapott egy kulcsot!"));
                giveKeys();
                secondsRemaining = plugin.getConfig().getInt("interval", 3600);
            }
            int h = secondsRemaining / 3600;
            int m = (secondsRemaining % 3600) / 60;
            int s = secondsRemaining % 60;
            remainingDisplay = String.format("%d:%02d:%02d", h, m, s);
            secondsRemaining--;
        }, 0L, 20L);
    }
    private void giveKeys() {
        String keyType = plugin.getConfig().getString("key-type", "common_kulcs");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "excellentcrates key give " + player.getName() + " " + keyType);
        }
    }
    private void broadcast(String message) {
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        Bukkit.broadcast(component);
    }
    public boolean isActive() {
        return active;
    }
    public String getRemainingDisplay() {
        return remainingDisplay;
    }
    public int getSecondsRemaining() {
        return secondsRemaining;
    }
}
