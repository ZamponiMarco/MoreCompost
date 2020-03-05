package com.github.jummes.morecompost.manager;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jummes.libs.model.ModelManager;
import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.model.wrapper.ItemStackWrapper;
import com.github.jummes.morecompost.drop.Drop;
import com.github.jummes.morecompost.dropdescription.ItemDropDescription;
import com.github.jummes.morecompost.droptable.DropTable;
import com.google.common.collect.Lists;

public class DropsManager extends ModelManager<DropTable> {

	private List<DropTable> dropTables;

	public DropsManager(Class<DropTable> classObject, String databaseType, JavaPlugin plugin) {
		super(classObject, databaseType, plugin);
		this.dropTables = database.loadObjects();
	}

	public DropTable getDropTableById(String id) {
		return dropTables.stream().filter(dropTable -> dropTable.getPermissionString().equals(id)).findFirst()
				.orElse(null);
	}

	public DropTable getHighestPriorityDropTable(Player owner) {
		if (owner != null)
			return dropTables.stream().filter(dropTable -> owner.hasPermission(dropTable.getPermissionString()))
					.sorted((d1, d2) -> d1.getPriority() - d2.getPriority()).findFirst().orElse(getDefaultDropTable());
		return getDefaultDropTable();
	}

	public void reloadData() {
		this.dropTables = database.loadObjects();
	}

	private DropTable getDefaultDropTable() {
		return new DropTable("", new IntRange(1, 1), Integer.MAX_VALUE, Lists.newArrayList(new Drop(1,
				new ItemDropDescription(new ItemStackWrapper(new ItemStack(Material.BONE_MEAL)), new IntRange(1, 1)))));
	}

}
