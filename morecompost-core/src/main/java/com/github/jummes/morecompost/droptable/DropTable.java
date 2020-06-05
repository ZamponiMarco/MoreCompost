package com.github.jummes.morecompost.droptable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import com.github.jummes.libs.annotation.Serializable;
import com.github.jummes.libs.model.Model;
import com.github.jummes.libs.model.math.IntRange;
import com.github.jummes.libs.util.ItemUtils;
import com.github.jummes.libs.util.MessageUtils;
import com.github.jummes.morecompost.drop.Drop;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SerializableAs("DropTable")
public class DropTable implements Model {

    private static final String PERMISSION_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RkNjM5NzhlODRlMjA5MjI4M2U5Y2QwNmU5ZWY0YmMyMjhiYjlmMjIyMmUxN2VlMzgzYjFjOWQ5N2E4YTAifX19=";
    private static final String PRIORITY_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMTk3MmYyY2ZhNGQzMGRjMmYzNGU4ZDIxNTM1OGMwYzU3NDMyYTU1ZjZjMzdhZDkxZTBkZDQ0MTkxYSJ9fX0==";
    private static final String ROLLS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI0MjMwMmViZDY1NWY2ZDQyOWMxZTRhZWRlMjFiN2Y1YzRkYjY4YTQwNDVlYmFlYzE3NjMzYTA1MGExYTEifX19=";
    private static final String DROPS_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODllNzAxNjIxNDNjN2NhYTIwZTMwM2VlYTMxNGE5YWVkNWRiOWNjNjg0MzVlNzgzYjNjNTlhZjQzYmY0MzYzNSJ9fX0====";

    private static final String PERM_PREFIX = "morecompost.drops.";

    private static int currentPriority = 10000;

    @Serializable(headTexture = PERMISSION_HEAD, description = "gui.droptable.permission")
    private String permissionString;
    @Serializable(headTexture = ROLLS_HEAD, description = "gui.droptable.rolls")
    private IntRange rolls;
    @Serializable(headTexture = PRIORITY_HEAD, description = "gui.droptable.priority")
    private int priority;
    @Serializable(headTexture = DROPS_HEAD, description = "gui.droptable.drops")
    private List<Drop> drops;

    private Random random;
    private Map<Integer, Drop> weightMap;
    private TreeSet<Integer> sortedWeightSet;

    public DropTable() {
        this("Tier" + String.valueOf(currentPriority), new IntRange(1, 1), currentPriority++, new ArrayList<Drop>());
    }

    public DropTable(String permissionString, IntRange rolls, int priority, List<Drop> drops) {
        this.permissionString = permissionString;
        this.rolls = rolls;
        this.priority = priority;
        this.drops = drops;
        this.random = new Random();
        reloadTables();
    }

    public void dropAllLoot(Block block) {
        int randomRolls = random.nextInt(rolls.getDifference() + 1) + rolls.getMin();
        IntStream.range(0, randomRolls).forEach(i -> dropRandomLoot(block));
    }

    public void fillContainer(Block block) {
        int randomRolls = random.nextInt(rolls.getDifference() + 1) + rolls.getMin();
        IntStream.range(0, randomRolls).forEach(i -> fillContainerRandom(block));
    }

    private Drop getRandomDrop() {
        return weightMap.get(sortedWeightSet.higher(random.nextInt(sortedWeightSet.last())));
    }

    private void dropRandomLoot(Block block) {
        getRandomDrop().getDropDescription().dropLoot(block);
    }

    private void fillContainerRandom(Block block) {
        getRandomDrop().getDropDescription().putInContainer(block);
    }

    private void reloadTables() {
        AtomicInteger integer = new AtomicInteger();
        this.weightMap = drops.stream().collect(Collectors
                .toMap(drop -> integer.accumulateAndGet(drop.getWeight(), (i, j) -> i + j), Functions.identity()));
        this.sortedWeightSet = new TreeSet<>(weightMap.keySet());
    }

    // ---

    @Override
    public void onModify() {
        reloadTables();
    }

    @SuppressWarnings("unchecked")
    public static DropTable deserialize(Map<String, Object> map) {
        String permissionString = (String) map.get("permissionString");
        IntRange rolls = (IntRange) map.get("rolls");
        int priority = (int) map.get("priority");
        currentPriority = Math.max(currentPriority, priority + 1);
        List<Drop> drops = (List<Drop>) map.get("drops");
        return new DropTable(permissionString, rolls, priority, drops);
    }

    @Override
    public ItemStack getGUIItem() {
        return ItemUtils.getNamedItem(new ItemStack(Material.CHEST), "&6&lPermission » &c&l" + permissionString,
                Lists.newArrayList(MessageUtils.color("&7Priority » &8&l" + String.valueOf(priority)),
                        MessageUtils.color("&6&lLeft click &eto modify."),
                        MessageUtils.color("&6&lRight click &eto delete.")));
    }

    public String getPermissionString() {
        return PERM_PREFIX.concat(permissionString);
    }

}
