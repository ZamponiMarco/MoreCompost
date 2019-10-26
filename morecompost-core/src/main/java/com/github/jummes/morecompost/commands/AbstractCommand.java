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

	/**
	 * Constructs a command
	 * 
	 * @param sender         sender of the command
	 * @param subCommand     subCommand
	 * @param arguments      arguments given to subcommand
	 * @param isSenderPlayer whether the sender is a player
	 */
	public AbstractCommand(CommandSender sender, String subCommand, String[] arguments, boolean isSenderPlayer) {
		this.sender = sender;
		this.subCommand = subCommand;
		this.arguments = arguments;
		this.isSenderPlayer = isSenderPlayer;
	}

	/**
	 * Checks if the sender can execute the command
	 * 
	 * @return true if he can
	 */
	protected boolean canSenderTypeExecute() {
		return !isOnlyPlayer() || isSenderPlayer;
	}

	/**
	 * Checks if sender has the permission to execute the command
	 * 
	 * @return true if he has
	 */
	protected boolean hasPermission() {
		return sender.hasPermission(getPermission());
	}

	/**
	 * Checks if the command can be executed, if it can proceeds to execute it
	 * 
	 */
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

	/**
	 * Executes the command
	 * 
	 */
	protected abstract void execute();

	/**
	 * Whether the command can only be executed by players or not
	 * 
	 * @return true if it can only be executed by players
	 */
	protected abstract boolean isOnlyPlayer();

	/**
	 * Returns the permission needed to run this command
	 * 
	 * @return the permission of this command
	 */
	protected abstract Permission getPermission();

}
