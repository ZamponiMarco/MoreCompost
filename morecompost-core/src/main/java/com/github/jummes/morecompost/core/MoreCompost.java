package com.github.jummes.morecompost.core;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jummes.morecompost.commands.executor.MoreCompostCommandExecutor;
import com.github.jummes.morecompost.listeners.BoneMealSpawnListener;
import com.github.jummes.morecompost.listeners.ComposterBreakListener;
import com.github.jummes.morecompost.listeners.ComposterDropListener;
import com.github.jummes.morecompost.listeners.ComposterPlaceListener;
import com.github.jummes.morecompost.listeners.HopperInteractComposterListener;
import com.github.jummes.morecompost.listeners.InventoryClickListener;
import com.github.jummes.morecompost.listeners.PlayerChatListener;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.managers.CompostersManager;
import com.github.jummes.morecompost.managers.DropsManager;
import com.github.jummes.morecompost.managers.LocalesManager;
import com.github.jummes.morecompost.managers.SettingsManager;
import com.github.jummes.morecompost.settings.Settings;
import com.github.jummes.morecompost.wrapper.VersionWrapper;

public class MoreCompost extends JavaPlugin {

	private static MoreCompost instance;
	
	private VersionWrapper wrapper;
	private DropsManager dropsManager;
	private CompostablesManager compostablesManager;
	private CompostersManager compostersManager;
	private SettingsManager settingsManager;
	private LocalesManager localesManager;


	public void onEnable() {
		instance = this;
		setUpFolder();
		setUpWrapper();
		startUpTasks();
	}
	
	private void setUpFolder() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
	}
	
	private void setUpWrapper() {
		String serverVersion = getServer().getClass().getPackage().getName();
		String version = serverVersion.substring(serverVersion.lastIndexOf('.') + 1);

		try {
			wrapper = (VersionWrapper) Class
					.forName("com.github.jummes.morecompost.wrapper.VersionWrapper_" + version).getConstructor()
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startUpTasks() {
		settingsManager = new SettingsManager();

		if (Boolean.valueOf(settingsManager.getSetting(Settings.METRICS))) {
			new Metrics(this);
		}

		if (Boolean.valueOf(settingsManager.getSetting(Settings.UPDATECHECKER))) {
			new UpdateChecker().checkForUpdate();
		}
		
		compostersManager = new CompostersManager();
		dropsManager = new DropsManager();
		compostablesManager = new CompostablesManager();
		localesManager = new LocalesManager();
		CommandExecutor commandExecutor = new MoreCompostCommandExecutor();
		getCommand("mc").setExecutor(commandExecutor);
		getCommand("mc").setTabCompleter((TabCompleter) commandExecutor);
		Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
		Bukkit.getPluginManager().registerEvents(new ComposterBreakListener(), this);
		Bukkit.getPluginManager().registerEvents(new ComposterPlaceListener(), this);
		Bukkit.getPluginManager().registerEvents(new HopperInteractComposterListener(), this);
		Bukkit.getPluginManager().registerEvents(new ComposterDropListener(), this);
		Bukkit.getPluginManager().registerEvents(new BoneMealSpawnListener(), this);
	}

	public static MoreCompost getInstance() {
		return instance;
	}

	public DropsManager getDropsManager() {
		return dropsManager;
	}

	public CompostablesManager getCompostablesManager() {
		return compostablesManager;
	}

	public CompostersManager getCompostersManager() {
		return compostersManager;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public VersionWrapper getWrapper() {
		return wrapper;
	}

	public LocalesManager getLocalesManager() {
		return localesManager;
	}

}
