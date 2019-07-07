package com.github.jummes.morecompost.drops.factory;

import org.bukkit.configuration.ConfigurationSection;

import com.github.jummes.morecompost.drops.CompostDrop;
import com.github.jummes.morecompost.drops.ExperienceCompostDrop;

public class ExperienceCompostDropFactory implements CompostDropFactory {

	@Override
	public CompostDrop buildCompostDrop(ConfigurationSection drop) {
		int amount = drop.getInt("amount", 5);
		
		return new ExperienceCompostDrop(amount);
	}

}
