package io.github.alabasteralibi.simplyboots;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.alabasteralibi.simplyboots.compat.trinkets.SimplyBootsTrinketsDelegateClient;
import io.github.alabasteralibi.simplyboots.components.BootComponents;
import io.github.alabasteralibi.simplyboots.components.LavaBootsComponent;
import io.github.alabasteralibi.simplyboots.components.RocketBootsComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SimplyBootsClient implements ClientModInitializer {
    public static final Identifier ICONS_TEXTURE = SimplyBootsHelpers.id("textures/hud/icons.png");

    @Override
    public void onInitializeClient() {
        if (SimplyBootsHelpers.TRINKETS_LOADED) {
            SimplyBootsTrinketsDelegateClient.registerRenderers();
        }

        HudRenderCallback.EVENT.register(this::setupHudAdditions);
    }

    // Render HUD elements for rocket and lava timers
    private void setupHudAdditions(DrawContext context, float tickDelta) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null || player.isCreative()) {
            return;
        }

        int vehicle_hearts = 0;
        if (player.getVehicle() instanceof LivingEntity vehicle) {
            vehicle_hearts = (int) (vehicle.getMaxHealth() + 0.5F) / 2;
            if (vehicle_hearts > 30) {
                vehicle_hearts = 30;
            }
        }

        int drawWidth = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 + 91;
        int drawHeight = MinecraftClient.getInstance().getWindow().getScaledHeight() - 59;
        drawHeight -= ((int) Math.ceil(vehicle_hearts / 10.0D) - 1) * 10;
        drawHeight -= player.getAir() < player.getMaxAir() ? 10 : 0;  // If air showing, move up

        drawHeight -= renderLavaImmunityBar(context, drawWidth, drawHeight, player) ? 10 : 0;
        drawHeight -= renderRocketBar(context, drawWidth, drawHeight, player) ? 10 : 0;
    }

    /**
     * Renders the lava immunity bar for the player above the hunger shanks.
     *
     * @param context    The DrawContext to draw with.
     * @param drawWidth  The width to draw the right side of the bar at.
     * @param drawHeight The height to draw the bar at.
     * @return Whether the bar was successfully rendered or not.
     */
    private boolean renderLavaImmunityBar(DrawContext context, int drawWidth, int drawHeight, PlayerEntity player) {
        // Don't display if full (or empty) and not in lava (like breath bubbles)
        int lavaTicksLeft = BootComponents.LAVA_BOOTS.get(player).getValue();
        if (lavaTicksLeft == LavaBootsComponent.MAX_VALUE && !player.isSubmergedIn(FluidTags.LAVA)) {
            return false;
        }
        if (lavaTicksLeft == LavaBootsComponent.MIN_VALUE) {
            return false;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ICONS_TEXTURE);
        int iconsToDraw = MathHelper.ceil(lavaTicksLeft * 10 / (double) LavaBootsComponent.MAX_VALUE);
        for (int i = 0; i < iconsToDraw; ++i) {
            context.drawTexture(ICONS_TEXTURE, drawWidth - i * 8 - 9, drawHeight, 0, 0, 9, 9, 18, 9);
        }
        return true;
    }

    /**
     * Renders the remaining rocket boost time above any other bars.
     *
     * @param context    The DrawContext to draw with.
     * @param drawWidth  The width to draw the right side of the bar at.
     * @param drawHeight The height to draw the bar at.
     * @return Whether the rocket bar was successfully rendered or not.
     */
    private boolean renderRocketBar(DrawContext context, int drawWidth, int drawHeight, PlayerEntity player) {
        // Don't display if full or empty
        int rocketTicksLeft = BootComponents.ROCKET_BOOTS.get(player).getValue();
        if (rocketTicksLeft == RocketBootsComponent.MAX_VALUE || player.isOnGround()) {
            return false;
        }
        if (rocketTicksLeft == RocketBootsComponent.MIN_VALUE) {
            return false;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ICONS_TEXTURE);
        int iconsToDraw = MathHelper.ceil(rocketTicksLeft * 10 / (double) RocketBootsComponent.MAX_VALUE);
        for (int i = 0; i < iconsToDraw; ++i) {
            context.drawTexture(ICONS_TEXTURE, drawWidth - i * 8 - 9, drawHeight, 9, 0, 9, 9, 18, 9);
        }
        return true;
    }
}
