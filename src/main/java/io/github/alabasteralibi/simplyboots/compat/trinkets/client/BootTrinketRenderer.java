package io.github.alabasteralibi.simplyboots.compat.trinkets.client;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import io.github.alabasteralibi.simplyboots.client.BootArmorRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class BootTrinketRenderer implements TrinketRenderer {
    private BootArmorRenderer renderer;

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.hasStackEquipped(EquipmentSlot.FEET)) {
            // TODO: Play with the model to get it to render over vanilla boots.
            // I'm just so tired of messing with rendering.
            return;
        }
        if (this.renderer == null) {
            this.renderer = new BootArmorRenderer();
        }
        renderer.prepForRender(entity, stack, EquipmentSlot.FEET, (BipedEntityModel<?>) contextModel);
        renderer.defaultRender(matrices, renderer.getAnimatable(), vertexConsumers, null, vertexConsumers.getBuffer(RenderLayer.getArmorCutoutNoCull(renderer.getTextureLocation(renderer.getAnimatable()))), 0, tickDelta, light);
    }
}
