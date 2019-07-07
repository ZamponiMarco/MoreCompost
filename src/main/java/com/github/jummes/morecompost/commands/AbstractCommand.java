package com.github.jummes.morecompost.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import com.github.jummes.morecompost.utils.MessageUtils;

public abstract class AbstractCommand {

	private static final String NO_PERMISSION = MessageUtils.color("&cYou don't have the permission");
	private static final String ONLY_PLAYER = MessageUtils.color("&cThis command can be used only by a player");
	private static final String INCORRECT_USAGE = MessageUtils.color("&cIncorrect command syntax, type /mc help");

	protected CommandSender sender;
	protected String subCommand;
	protected String[] arguments;
	protected boolean isSenderPlayer;

	public AbstractCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		this.sender = sender;
		this.subCommand = subCommand;
		this.arguments = arguments;
		this.isSenderPlayer = isSenderPlayer;
	}

	protected boolean canSenderTypeExecute() {
		return !isOnlyPlayer() || isSenderPlayer;
	}

	protected boolean hasPermission() {
		return sender.hasPermission(getPermission());
	}

	public void checkExecution() {
		String errorMessage = "";
		errorMessage = !hasPermission() ? NO_PERMISSION : errorMessage;
		errorMessage = !canSenderTypeExecute() ? ONLY_PLAYER : errorMessage;
		if (canSenderTypeExecute() && hasPermission()) {
			execute();
		} else {
			sender.sendMessage(errorMessage);
		}
	}

	protected void incorrectUsage() {
		sender.sendMessage(INCORRECT_USAGE);
	}

	protected abstract void execute();

	protected abstract boolean isOnlyPlayer();

	protected abstract Permission getPermission();

}
