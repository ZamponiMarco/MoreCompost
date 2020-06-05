package com.github.jummes.morecompost.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.morecompost.composter.Composter;
import com.github.jummes.morecompost.core.MoreCompost;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CompostersManager extends ModelManager<Composter> {

    private Set<Composter> composters;

    public CompostersManager(Class<Composter> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin);
        this.composters = new HashSet<>(database.loadObjects());
        composters.forEach(composter -> composter.getComposters().forEach(loc -> {
            Block b = loc.getBlock();
            if (b.getType().equals(Material.COMPOSTER)) {
                b.setMetadata("owner", new FixedMetadataValue(MoreCompost.getInstance(), composter.getId()));
            }
        }));
    }

    public void addBlockToPlayer(UUID id, Block b) {
        Composter composter = getComposterByPlayer(id);
        if (composter == null) {
            composter = new Composter(id, new ArrayList<>());
            composters.add(composter);
        }
        composter.getComposters().add(b.getLocation());
        saveModel(composter);
    }

    public void removeBlockFromPlayer(UUID id, Block b) {
        Composter composter = getComposterByPlayer(id);
        if (composter != null) {
            composter.getComposters().remove(b.getLocation());
            saveModel(composter);
        }
    }

    public void reloadData() {
        this.composters = new HashSet<>(database.loadObjects());
    }

    private Composter getComposterByPlayer(UUID id) {
        return composters.stream().filter(composter -> composter.getId().equals(id)).findFirst().orElse(null);
    }
}
