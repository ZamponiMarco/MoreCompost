package com.github.jummes.morecompost.dropdescription;

import java.util.Map;
import java.util.Random;

import com.github.jummes.libs.annotation.Enumerable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.core.Libs;
import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.model.wrapper.ItemMetaWrapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Enumerable.Child
@Enumerable.Displayable(name = "&c&lHead", description = "gui.droptable.head-description.description", headTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI3YWY5ZTQ0MTEyMTdjN2RlOWM2MGFjYmQzYzNmZDY1MTk3ODMzMzJhMWIzYmM1NmZiZmNlOTA3MjFlZjM1In19fQ==")
@SerializableAs("HeadDropDescription")
public class HeadDropDescription extends DropDescription {

    private static final String ITEM_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI2YTI5ZWE2OGEwYzYxYjFlZGEyZDhhZWMzZTIyMjk3MjczMjNiN2QyZGE2YmMwNGNjMGNkMmRlZjNiNDcxMiJ9fX0====";
    private static final String COUNT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19";
    private static final String META_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4MzM0MTUxYzIzNGY0MTY0NzExM2JlM2VhZGYyODdkMTgxNzExNWJhYzk0NDVmZmJiYmU5Y2IyYjI4NGIwIn19fQ==";

    @Serializable(headTexture = COUNT_HEAD, description = "gui.droptable.head-description.count")
    private IntRange count;
    @Serializable(displayItem = "getGUIItem", description = "gui.droptable.head-description.texture")
    private String texture;
    @Serializable(headTexture = META_HEAD, description = "gui.droptable.head-description.meta")
    private ItemMetaWrapper meta;

    public HeadDropDescription() {
        this(new IntRange(), ITEM_HEAD, new ItemMetaWrapper(new ItemStack(Material.CARROT).getItemMeta()));
    }

    @Override
    public void dropLoot(Block block) {
        ItemStack toDrop = Libs.getWrapper().skullFromValue(texture);
        toDrop.setItemMeta(fuseItemMetaWithWrapper(toDrop.getItemMeta(), meta));
        Random random = new Random();
        toDrop.setAmount(random.nextInt(count.getDifference() + 1) + count.getMin());
        Location loc = block.getLocation().clone().add(0, .3, 0);
        block.getWorld().dropItemNaturally(loc, toDrop);
    }

    @Override
    public void putInContainer(Block block) {
        Container container = (Container) block.getState();
        ItemStack toDrop = Libs.getWrapper().skullFromValue(texture);
        toDrop.setItemMeta(fuseItemMetaWithWrapper(toDrop.getItemMeta(), meta));
        toDrop.setAmount(new Random().nextInt(count.getDifference() + 1) + count.getMin());
        container.getInventory().addItem(toDrop);
    }

    private ItemMeta fuseItemMetaWithWrapper(ItemMeta meta, ItemMetaWrapper metaWrapper) {
        meta.setDisplayName(metaWrapper.getWrapped().getDisplayName());
        meta.setLore(metaWrapper.getWrapped().getLore());
        return meta;
    }

    public static HeadDropDescription deserialize(Map<String, Object> map) {
        IntRange count = (IntRange) map.getOrDefault("count", new IntRange());
        String texture = (String) map.get("texture");
        ItemMetaWrapper meta = (ItemMetaWrapper) map.get("meta");
        return new HeadDropDescription(count, texture, meta);
    }

    @Override
    public ItemStack getGUIItem() {
        ItemStack item = Libs.getWrapper().skullFromValue(texture);
        item.setItemMeta(fuseItemMetaWithWrapper(item.getItemMeta(), meta));
        return item;
    }

    @Override
    public String toString() {
        return meta.getWrapped().getDisplayName().equals("") ? "Player Head" : meta.getWrapped().getDisplayName();
    }

}
