package io.github.alabasteralibi.simplyboots.compat.trinkets;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import io.github.alabasteralibi.simplyboots.compat.trinkets.client.BootTrinketRenderer;

import static io.github.alabasteralibi.simplyboots.registry.SimplyBootsItems.*;

public class SimplyBootsTrinketsDelegateClient {
    public static void registerRenderers() {
        TrinketRendererRegistry.registerRenderer(HERMES_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ROCKET_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(SPECTRE_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(LIGHTNING_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(ICE_SKATES, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(FROSTSPARK_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(WATER_WALKING_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(OBSIDIAN_WATER_WALKING_BOOTS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(LAVA_WADERS, new BootTrinketRenderer());
        TrinketRendererRegistry.registerRenderer(TERRASPARK_BOOTS, new BootTrinketRenderer());
    }
}
