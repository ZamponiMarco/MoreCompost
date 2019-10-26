package com.github.jummes.morecompost.utils;

import org.bukkit.ChatColor;

public class MessageUtils {

	/**
	 * Returns a string that represents a colored string
	 * 
	 * @param string to be colored
	 * @return colored string
	 */
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static String header(String string) {
		return color(String.format("&e=--- &c%s &e---=\n", string));
	}

	public static String delimiter() {
		return color("&e-----------------------------------------------------");
	}

}
