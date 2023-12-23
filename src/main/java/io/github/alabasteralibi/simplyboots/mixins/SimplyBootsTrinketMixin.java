package io.github.alabasteralibi.simplyboots.mixins;

import com.google.common.collect.Multimap;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.alabasteralibi.simplyboots.registry.SimplyBootsItems;
import io.github.alabasteralibi.simplyboots.registry.SimplyBootsTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static io.github.alabasteralibi.simplyboots.items.BootItems.BaseBootItem.STEP_BOOST_MODIFIER;

@Mixin(SimplyBootsItems.class)
public abstract class SimplyBootsTrinketMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void registerTrinkets(String name, Item item, CallbackInfoReturnable<Item> cir) {
        TrinketsApi.registerTrinket(item, new Trinket() {
            // Prevents equipping boots if a pair is already equipped as armor.
            @Override
            public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
                return !entity.getEquippedStack(EquipmentSlot.FEET).isIn(SimplyBootsTags.BOOTS);
            }

            @Override
            public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
                Multimap<EntityAttribute, EntityAttributeModifier> modifiers = Trinket.super.getModifiers(stack, slot, entity, uuid);
                modifiers.put(StepHeightEntityAttributeMain.STEP_HEIGHT, STEP_BOOST_MODIFIER);
                if (stack.isIn(SimplyBootsTags.SPEEDY_BOOTS)) {
                    modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "Movement speed", 0.075, EntityAttributeModifier.Operation.MULTIPLY_BASE));
                }
                if (stack.isIn(SimplyBootsTags.EXTRA_SPEEDY_BOOTS)) {
                    modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "Movement speed", 0.15, EntityAttributeModifier.Operation.MULTIPLY_BASE));
                }
                return modifiers;
            }
        });
    }
}
