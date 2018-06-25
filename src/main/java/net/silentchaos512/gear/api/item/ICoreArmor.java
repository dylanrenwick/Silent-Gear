package net.silentchaos512.gear.api.item;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gear.api.lib.ItemPartData;
import net.silentchaos512.gear.api.parts.ItemPart;
import net.silentchaos512.gear.api.parts.PartMain;
import net.silentchaos512.gear.api.stats.CommonItemStats;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.init.ModMaterials;
import net.silentchaos512.gear.util.GearData;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public interface ICoreArmor extends ICoreItem {

    Set<ItemStat> RELEVANT_STATS = new LinkedHashSet<>(Arrays.asList(
            CommonItemStats.ARMOR,
            CommonItemStats.MAGIC_ARMOR,
            CommonItemStats.ARMOR_TOUGHNESS,
            CommonItemStats.DURABILITY,
            CommonItemStats.ENCHANTABILITY,
            CommonItemStats.RARITY
    ));

    @Override
    default Set<ItemStat> getRelevantStats(@Nonnull ItemStack stack) {
        return RELEVANT_STATS;
    }

    default PartMain getPrimaryPart(@Nonnull ItemStack stack) {
        ItemPartData data = GearData.getPrimaryPart(stack);
        if (data != null)
            return (PartMain) data.part;
        return ModMaterials.mainWood;
    }

    @Override
    default ItemPart[] getRenderParts(ItemStack stack) {
        return new ItemPart[] {getPrimaryPart(stack)};
    }
}
