package com.github.jummes.morecompost.manager;

import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.morecompost.compostabletable.CompostableTable;
import com.google.common.collect.Lists;

public class CompostablesManager extends ModelManager<CompostableTable> {

    private List<CompostableTable> compostableTables;

    public CompostablesManager(Class<CompostableTable> classObject, String databaseType, JavaPlugin plugin) {
        super(classObject, databaseType, plugin);
        this.compostableTables = database.loadObjects();
    }

    public CompostableTable getHighestPriorityCompostableTable(Player owner) {
        if (owner != null)
            return compostableTables.stream().filter(dropTable -> owner.hasPermission(dropTable.getPermissionString())).min(Comparator.comparingInt(CompostableTable::getPriority))
                    .orElse(getDefaultCompostableTable());
        return getDefaultCompostableTable();
    }

    public void reloadData() {
        this.compostableTables = database.loadObjects();
    }

    private CompostableTable getDefaultCompostableTable() {
        return new CompostableTable("", Integer.MAX_VALUE, Lists.newArrayList(), false);
    }

}
