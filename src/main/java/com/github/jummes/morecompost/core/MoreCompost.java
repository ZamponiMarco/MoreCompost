package com.github.jummes.morecompost.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jummes.morecompost.commands.executor.MoreCompostCommandExecutor;
import com.github.jummes.morecompost.listeners.BoneMealSpawnListener;
import com.github.jummes.morecompost.listeners.ComposterBreakListener;
import com.github.jummes.morecompost.listeners.ComposterDropListener;
import com.github.jummes.morecompost.listeners.ComposterPlaceListener;
import com.github.jummes.morecompost.listeners.HopperInteractComposterListener;
import com.github.jummes.morecompost.managers.CompostablesManager;
import com.github.jummes.morecompost.managers.CompostersManager;
import com.github.jummes.morecompost.managers.DropsManager;

public class MoreCompost extends JavaPlugin {

	private static MoreCompost instance;

	private DropsManager dropsManager;
	private CompostablesManager compostablesManager;
	private CompostersManager compostersManager;

	public void onEnable() {
		instance = this;

		startUpTasks();
	}

	private void startUpTasks() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		compostersManager = new CompostersManager();
		dropsManager = new DropsManager();
		compostablesManager = new CompostablesManager();
		getCommand("mc").setExecutor(new MoreCompostCommandExecutor());		
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

}
