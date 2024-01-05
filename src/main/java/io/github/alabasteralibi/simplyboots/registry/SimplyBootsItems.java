package io.github.alabasteralibi.simplyboots.registry;

import io.github.alabasteralibi.simplyboots.SimplyBootsHelpers;
import io.github.alabasteralibi.simplyboots.compat.trinkets.SimplyBootsTrinketsDelegate;
import io.github.alabasteralibi.simplyboots.items.BootItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SimplyBootsItems {
    public static final Item HERMES_BOOTS = register("hermes_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item ROCKET_BOOTS = register("rocket_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item SPECTRE_BOOTS = register("spectre_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item LIGHTNING_BOOTS = register("lightning_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item ICE_SKATES = register("ice_skates", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item FROSTSPARK_BOOTS = register("frostspark_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item WATER_WALKING_BOOTS = register("water_walking_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
    public static final Item OBSIDIAN_WATER_WALKING_BOOTS = register("obsidian_water_walking_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().rarity(Rarity.RARE)));
    public static final Item LAVA_WADERS = register("lava_waders", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().fireproof().rarity(Rarity.RARE)));
    public static final Item TERRASPARK_BOOTS = register("terraspark_boots", new BootItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings().fireproof().rarity(Rarity.EPIC)));
    public static final Item LAVA_CHARM = register("lava_charm", new Item(new FabricItemSettings().fireproof().maxCount(1).rarity(Rarity.UNCOMMON)) {
        @Override
        public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
            for (String line : Text.translatable("item.simplyboots.lava_charm.tooltip").getString().split("\n")) {
                tooltip.add(Text.of(line));
            }
        }
    });

    private static Item register(String name, Item item) {
        Registry.register(Registries.ITEM, SimplyBootsHelpers.id(name), item);
        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, SimplyBootsHelpers.id("main_group"))).register(entries -> entries.add(item));

        if (SimplyBootsHelpers.TRINKETS_LOADED) {
            SimplyBootsTrinketsDelegate.registerItem(item);
        }

        return item;
    }
}
