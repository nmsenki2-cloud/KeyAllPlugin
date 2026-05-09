package hu.syntrixsmp.keyall;
import org.bukkit.plugin.java.JavaPlugin;
public class KeyAllPlugin extends JavaPlugin {
    private static KeyAllPlugin instance;
    private KeyAllManager manager;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        manager = new KeyAllManager(this);
        // Automatikus indítás
        manager.autoStart();
        // Parancs regisztrálása
        getCommand("keyall").setExecutor(new KeyAllCommand(this));
        // PlaceholderAPI hook
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new KeyAllPlaceholder(this).register();
            getLogger().info("[KeyAll] PlaceholderAPI sikeresen csatlakoztatva!");
        } else {
            getLogger().warning("[KeyAll] PlaceholderAPI nem található! A placeholder nem fog működni.");
        }
        getLogger().info("[KeyAll] Plugin sikeresen betöltve!");
    }
    @Override
    public void onDisable() {
        if (manager != null) {
            manager.stop();
        }
        getLogger().info("[KeyAll] Plugin leállítva.");
    }
    public static KeyAllPlugin getInstance() {
        return instance;
    }
    public KeyAllManager getManager() {
        return manager;
    }
}
