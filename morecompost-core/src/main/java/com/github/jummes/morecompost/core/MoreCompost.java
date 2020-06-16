package com.github.jummes.morecompost.core;

import java.io.File;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import com.github.jummes.libs.command.PluginCommandExecutor;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.localization.PluginLocale;
import com.github.jummes.morecompost.command.AboutCommand;
import com.github.jummes.morecompost.command.CompostableListCommand;
import com.github.jummes.morecompost.command.CompostablesCommand;
import com.github.jummes.morecompost.command.DropsCommand;
import com.github.jummes.morecompost.command.HelpCommand;
import com.github.jummes.morecompost.command.InspectCommand;
import com.github.jummes.morecompost.command.PlayerHelpCommand;
import com.github.jummes.morecompost.command.DropPercentageCommand;
import com.github.jummes.morecompost.command.ReloadCommand;
import com.github.jummes.morecompost.compostable.Compostable;
import com.github.jummes.morecompost.compostabletable.CompostableTable;
import com.github.jummes.morecompost.composter.Composter;
import com.github.jummes.morecompost.drop.Drop;
import com.github.jummes.morecompost.dropdescription.DropDescription;
import com.github.jummes.morecompost.dropdescription.ExperienceDropDescription;
import com.github.jummes.morecompost.dropdescription.HeadDropDescription;
import com.github.jummes.morecompost.dropdescription.ItemDropDescription;
import com.github.jummes.morecompost.dropdescription.NoDropDescription;
import com.github.jummes.morecompost.droptable.DropTable;
import com.github.jummes.morecompost.listener.ComposterBreakListener;
import com.github.jummes.morecompost.listener.ComposterDropListener;
import com.github.jummes.morecompost.listener.ComposterPlaceListener;
import com.github.jummes.morecompost.listener.HopperInteractComposterListener;
import com.github.jummes.morecompost.manager.CompostablesManager;
import com.github.jummes.morecompost.manager.CompostersManager;
import com.github.jummes.morecompost.manager.DropsManager;
import com.google.common.collect.Lists;

import lombok.Getter;

@Getter
public class MoreCompost extends JavaPlugin {

    static {
        Libs.registerSerializables();
        ConfigurationSerialization.registerClass(DropTable.class);
        ConfigurationSerialization.registerClass(Drop.class);
        ConfigurationSerialization.registerClass(DropDescription.class);
        ConfigurationSerialization.registerClass(ItemDropDescription.class);
        ConfigurationSerialization.registerClass(ExperienceDropDescription.class);
        ConfigurationSerialization.registerClass(HeadDropDescription.class);
        ConfigurationSerialization.registerClass(NoDropDescription.class);
        ConfigurationSerialization.registerClass(CompostableTable.class);
        ConfigurationSerialization.registerClass(Compostable.class);
        ConfigurationSerialization.registerClass(Composter.class);
    }

    private static final String CONFIG_VERSION = "2.0.3";

    @Getter
    private static MoreCompost instance;

    private DropsManager dropsManager;
    private CompostablesManager compostablesManager;
    private CompostersManager compostersManager;
    private PluginLocale locale;

    public void onEnable() {
        instance = this;
        setUpFolder();
        setUpData();
        setUpCommands();
        registerEvents();
        powerUpServices();
    }

    private void setUpFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File configFile = new File(getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        if (!Objects.equals(getConfig().getString("version"), CONFIG_VERSION)) {
            getLogger().info("config.yml has changed. Old config is stored inside config-"
                    + getConfig().getString("version") + ".yml");
            File outputFile = new File(getDataFolder(), "config-" + getConfig().getString("version") + ".yml");
            FileUtil.copy(configFile, outputFile);
            configFile.delete();
            saveDefaultConfig();
        }
    }

    private void setUpData() {
        locale = new PluginLocale(this, Lists.newArrayList("en-US", "it-IT", "zh-CN"), getConfig().getString("locale"));
        Libs.initializeLibrary(instance, locale);

        compostersManager = new CompostersManager(Composter.class, "yaml", this);
        dropsManager = new DropsManager(DropTable.class, "yaml", this);
        compostablesManager = new CompostablesManager(CompostableTable.class, "yaml", this);
    }

    private void setUpCommands() {
        PluginCommandExecutor executor = new PluginCommandExecutor(HelpCommand.class, "help");
        executor.registerCommand("drops", DropsCommand.class);
        executor.registerCommand("compostables", CompostablesCommand.class);
        executor.registerCommand("inspect", InspectCommand.class);
        executor.registerCommand("reload", ReloadCommand.class);
        executor.registerCommand("about", AboutCommand.class);
        getCommand("mc").setExecutor(executor);
        getCommand("mc").setTabCompleter(executor);
        PluginCommandExecutor composterExecutor = new PluginCommandExecutor(PlayerHelpCommand.class, "help");
        composterExecutor.registerCommand("drops", DropPercentageCommand.class);
        composterExecutor.registerCommand("compostables", CompostableListCommand.class);
        getCommand("composter").setExecutor(composterExecutor);
        getCommand("composter").setTabCompleter(composterExecutor);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new ComposterBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new ComposterPlaceListener(), this);
        Bukkit.getPluginManager().registerEvents(new HopperInteractComposterListener(), this);
        Bukkit.getPluginManager().registerEvents(new ComposterDropListener(), this);
    }

    private void powerUpServices() {
        if (Boolean.parseBoolean(getConfig().getString("updateChecker"))) {
            new UpdateChecker().checkForUpdate();
        }
    }

}
