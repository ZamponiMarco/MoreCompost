package com.github.jummes.morecompost.dropdescription;

import com.github.jummes.libs.annotation.Enumerable;
import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.util.ItemUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@Getter
@AllArgsConstructor
@Enumerable.Child(name = "&c&lExperience", description = "gui.droptable.experience-description.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg5MDFmNzE0MzRkNTM5MjA3NDc2OTRmNjgyZjVlNTNiOGY3NDQ4M2YyNjljMzg0YzY5MzZiN2Q4NjU4MiJ9fX0=")
@SerializableAs("ExperienceDropDescription")
public class ExperienceDropDescription extends DropDescription {

    private static final String EXPERIENCE_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTg5MDFmNzE0MzRkNTM5MjA3NDc2OTRmNjgyZjVlNTNiOGY3NDQ4M2YyNjljMzg0YzY5MzZiN2Q4NjU4MiJ9fX0=";
    private static final String AMOUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19";

    @Serializable(headTexture = AMOUNT_HEAD, description = "gui.droptable.experience-description.amount")
    private IntRange amount;

    public ExperienceDropDescription() {
        this(new IntRange(1, 1));
    }

    public static ExperienceDropDescription deserialize(Map<String, Object> map) {
        IntRange amount = (IntRange) map.get("amount");
        return new ExperienceDropDescription(amount);
    }

    @Override
    public void dropLoot(Block block) {
        int experienceAmount = new Random().nextInt(amount.getDifference() + 1) + amount.getMin();
        ExperienceOrb orb = (ExperienceOrb) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5, 1, 0.5),
                EntityType.EXPERIENCE_ORB);
        orb.setExperience(experienceAmount);
    }

    @Override
    public void putInContainer(Block block) {
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(Libs.getWrapper().skullFromValue(EXPERIENCE_HEAD),
                "&6&lExperience", new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Experience";
    }

}
