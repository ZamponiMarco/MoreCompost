package com.github.jummes.morecompost.managers;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.droptables.DropTable;
import com.github.jummes.morecompost.droptables.factory.DropTableFactory;
import com.github.jummes.morecompost.utils.MessageUtils;

public class DropsManager implements DataManager {

	private final static String FILENAME = "drops.yml";
	private final static String PERM_PREFIX = "morecompost.drops.";
	private final static Permission DEFAULT_PERM = new Permission(PERM_PREFIX + "default", PermissionDefault.TRUE);
	private final static String ERROR_MSG = MessageUtils
			.color("&cAn error has occurred while loading &6Drops&c, please check your &6drops.yml&c file.");

	private MoreCompost plugin;

	private File dataFile;
	private YamlConfiguration dataYaml;
	private LinkedHashMap<Permission, DropTable> percentages;

	public DropsManager() {
		this.plugin = MoreCompost.getInstance();

		loadDataFile();
		loadDataYaml();
		loadData();
	}

	@Override
	public void loadDataFile() {
		dataFile = new File(plugin.getDataFolder(), FILENAME);
		if (!dataFile.exists()) {
			plugin.saveResource(FILENAME, false);
		}
	}

	@Override
	public void loadDataYaml() {
		dataYaml = YamlConfiguration.loadConfiguration(dataFile);
	}

	@Override
	public void loadData() {
		percentages = new LinkedHashMap<>();
		try {
			dataYaml.getKeys(false).forEach(key -> addDropTable(key));
		} catch (Exception e) {
			Bukkit.getLogger().warning(ERROR_MSG);
		}
		percentages.putIfAbsent(DEFAULT_PERM, DropTableFactory.defaultDropTable(DEFAULT_PERM));

		percentages.get(DEFAULT_PERM).setPriority(Integer.MAX_VALUE);

		percentages = percentages.entrySet().stream()
				.sorted((entry1, entry2) -> entry1.getValue().getPriority() - entry2.getValue().getPriority())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
	}

	@Override
	public YamlConfiguration getDataYaml() {
		return dataYaml;
	}

	@Override
	public File getDataFile() {
		return dataFile;
	}

	private void addDropTable(String key) {
		Permission permission = key.equalsIgnoreCase("default") ? DEFAULT_PERM : new Permission(PERM_PREFIX + key);
		percentages.put(permission, DropTableFactory.buildDropTable(permission, dataYaml.getConfigurationSection(key)));
	}

	public Map<Permission, DropTable> getPercentages() {
		return percentages;
	}

	public Permission getHighestPermission(Player player) {
		try {
			return percentages.keySet().stream().filter(permission -> player.hasPermission(permission)).findFirst()
					.orElse(DEFAULT_PERM);
		} catch (Exception e) {
			return DEFAULT_PERM;
		}
	}

	public DropTable getDropTableById(String dropTableId) {
		return percentages.values().stream().filter(dropTable -> dropTable.getId().equals(dropTableId)).findFirst()
				.orElseGet(() -> DropTableFactory.defaultDropTable(DEFAULT_PERM));
	}

	public void createDefaultDropTable(String name) {
		ConfigurationSection section = getDataYaml().createSection(name);
		section.set("drops", section.createSection("drops"));
	}

	public void getDefaultDrop(String dropTableId, String name) {
		getDataYaml().getConfigurationSection(dropTableId).getConfigurationSection("drops").createSection(name);
	}

}
