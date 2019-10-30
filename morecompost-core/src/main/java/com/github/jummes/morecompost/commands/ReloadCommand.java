package com.github.jummes.morecompost.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.core.MoreCompost;
import com.github.jummes.morecompost.utils.MessageUtils;

public class ReloadCommand extends AbstractCommand {

	private final static String RELOAD_SUCCESS = MessageUtils.color("&6MoreCompost &ahas been reloaded");

	public ReloadCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		super(sender, subCommand, arguments, isSenderPlayer);
	}

	@Override
	protected void execute() {

		MoreCompost.getInstance().getDropsManager().reloadData();
		MoreCompost.getInstance().getCompostablesManager().reloadData();
		MoreCompost.getInstance().getCompostersManager().reloadData();
		MoreCompost.getInstance().getSettingsManager().reloadData();
		MoreCompost.getInstance().getLocalesManager().reloadData();
		sender.sendMessage(RELOAD_SUCCESS);

	}

	@Override
	protected boolean isOnlyPlayer() {
		return false;
	}

	@Override
	protected Permission getPermission() {
		return new Permission("morecompost.commands.reload");
	}

}
