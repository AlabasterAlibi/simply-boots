package io.github.alabasteralibi.simplyboots;

import dev.emi.trinkets.api.TrinketsApi;
import io.github.alabasteralibi.simplyboots.registry.SimplyBootsItems;
import io.github.alabasteralibi.simplyboots.registry.SimplyBootsTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class SimplyBootsHelpers {
    public static final boolean TRINKETS_LOADED = FabricLoader.getInstance().isModLoaded("trinkets");

    public static boolean wearingBoots(LivingEntity entity, TagKey<Item> tag) {
        return entity.getEquippedStack(EquipmentSlot.FEET).isIn(tag) ||
                wearingTrinketBoots(entity, tag);
    }

    /**
     * Determine whether an entity is wearing the Trinket form of the given tag's boots.
     * Safe to call even when Trinkets is not loaded.
     * @param entity The entity to check for wearing boots.
     * @param tag An item tag specifying what boots to check for.
     * @return Whether entity is wearing any of the boots in tag, as a Trinket.
     */
    public static boolean wearingTrinketBoots(LivingEntity entity, TagKey<Item> tag) {
        if (!SimplyBootsHelpers.TRINKETS_LOADED) {
            return false;
        }

        return TrinketsApi.getTrinketComponent(entity).flatMap(component -> {
            if (component.isEquipped(trinket -> trinket.isIn(tag))) {
                return Optional.of(true);
            } else {
                return Optional.empty();
            }
        }).orElse(false);
    }

    public static boolean wearingLavaImmune(LivingEntity entity) {
        return SimplyBootsHelpers.wearingBoots(entity, SimplyBootsTags.HOT_FLUID_WALKING_BOOTS) ||
                entity.isHolding(SimplyBootsItems.LAVA_CHARM);
    }

    public static boolean isFireImmune(LivingEntity entity) {
        return SimplyBootsHelpers.wearingBoots(entity, SimplyBootsTags.FIRE_RESISTANT_BOOTS) ||
                entity.isHolding(SimplyBootsItems.LAVA_CHARM);
    }

    public static Identifier id(String path) {
        return new Identifier("simplyboots", path);
    }
}
