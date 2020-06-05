package com.github.jummes.morecompost.drop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.dropdescription.DropDescription;
import com.github.jummes.morecompost.dropdescription.ItemDropDescription;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@SerializableAs("Drop")
public class Drop implements Model {

    private static final String WEIGHT_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjVlOTE1MmVmZDg5MmY2MGQ3ZTBkN2U1MzM2OWUwNDc3OWVkMzExMWUyZmIyNzUyYjZmNGMyNmRmNTQwYWVkYyJ9fX0=";
    private static final String DESCRIPTION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZkNTU0YWQ1ZTBkYzYwMWVmYmI5MjVkMTM0MjRjY2VhNTMyYzgzMWE5MGI5Y2E3M2Q1ZTkzYWI2ZGJjNWRhZiJ9fX0=";

    @Serializable(headTexture = WEIGHT_HEAD, description = "gui.droptable.weight")
    private int weight;
    @Serializable(headTexture = DESCRIPTION_HEAD, description = "gui.droptable.description")
    private DropDescription dropDescription;

    public Drop() {
        this(1, new ItemDropDescription());
    }

    public static Drop deserialize(Map<String, Object> map) {
        int weight = (int) map.get("weight");
        DropDescription dropDescription = (DropDescription) map.get("dropDescription");
        return new Drop(weight, dropDescription);
    }

    @Override
    public ItemStack getGUIItem() {
        ItemStack item = dropDescription.getGUIItem().clone();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore() == null ? new ArrayList<String>() : meta.getLore();
        lore.addAll(Lists.newArrayList(MessageUtils.color("&7Weight » &8" + weight),
                MessageUtils.color("&6&lLeft click &eto modify."), MessageUtils.color("&6&lRight click &eto delete.")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

}
