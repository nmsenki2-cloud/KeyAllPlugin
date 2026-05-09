package hu.syntrixsmp.keyall;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KeyAllPlaceholder extends PlaceholderExpansion {

    private final KeyAllPlugin plugin;

    public KeyAllPlaceholder(KeyAllPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "keyall";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SyntrixSMP";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // Ne törölje el PlaceholderAPI restart után
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        KeyAllManager manager = plugin.getManager();

        // %keyall_remaining% - visszaszámláló
        if (identifier.equals("remaining")) {
            if (!manager.isActive()) {
                return "Inaktiv";
            }
            return manager.getRemainingDisplay();
        }

        // %keyall_active% - aktív-e
        if (identifier.equals("active")) {
            return manager.isActive() ? "Igen" : "Nem";
        }

        // %keyall_seconds% - hány másodperc van hátra
        if (identifier.equals("seconds")) {
            return String.valueOf(manager.getSecondsRemaining());
        }

        return null;
    }
}
