package com.github.jummes.morecompost.manager;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.morecompost.compostabletable.CompostableTable;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CompostablesManager extends ModelManager<CompostableTable> {

    private List<CompostableTable> compostableTables;

    public CompostablesManager(Class<CompostableTable> classObject, String databaseType, JavaPlugin plugin, Map<String, Object> args) {
        super(classObject, databaseType, plugin, args);
        this.compostableTables = database.loadObjects();
    }

    public CompostableTable getHighestPriorityCompostableTable(Player owner) {
        if (owner != null)
            return compostableTables.stream().filter(compostableTable -> owner.hasPermission(compostableTable.getPermissionString())).min(Comparator.comparingInt(CompostableTable::getPriority))
                    .orElse(getDefaultCompostableTable());
        return getDefaultCompostableTable();
    }

    public void reloadData() {
        this.compostableTables = database.loadObjects();
    }

    private CompostableTable getDefaultCompostableTable() {
        return compostableTables.stream()
                .filter(compostableTable -> compostableTable.getPermissionString().equals("morecompost.compostables.default")).findFirst()
                .orElse(new CompostableTable("", Integer.MAX_VALUE, Lists.newArrayList(), false));
    }

}
