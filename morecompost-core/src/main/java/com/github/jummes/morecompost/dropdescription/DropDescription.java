package com.github.jummes.morecompost.dropdescription;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.model.Model;
import org.bukkit.block.Block;

@Enumerable.Parent(classArray = {ItemDropDescription.class, ExperienceDropDescription.class, HeadDropDescription.class, NoDropDescription.class})
public abstract class DropDescription implements Model {

    /**
     * Drops the loot represented by the object in the given block
     *
     * @param block to drop loot by
     */
    public abstract void dropLoot(Block block);

    /**
     * Puts the loot represented by the object in the given container
     *
     * @param block to fill
     */
    public abstract void putInContainer(Block block);

}
