package io.github.alabasteralibi.simplyboots.items;

import io.github.alabasteralibi.simplyboots.client.BootRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BootItems {
    public static class BaseBootItem extends ArmorItem implements GeoItem {
        // Randomly generated (is that how it's supposed to be done?)
        public static final UUID STEP_BOOST_UUID = UUID.fromString("724ED93B-066A-4199-A6A7-7763AE6EF399");
        public static final EntityAttributeModifier STEP_BOOST_MODIFIER = new EntityAttributeModifier(STEP_BOOST_UUID, "Step height", 0.75, EntityAttributeModifier.Operation.ADDITION);

        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

        public BaseBootItem(ArmorMaterial material, Type type, Settings settings) {
            super(material, type, settings);

            SingletonGeoAnimatable.registerSyncedAnimatable(this);
        }

        @Override
        public void createRenderer(Consumer<Object> consumer) {
            consumer.accept(new RenderProvider() {
                private BootRenderer renderer;

                @Override
                public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                    if (this.renderer == null) {
                        this.renderer = new BootRenderer();
                    }

                    this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                    return this.renderer;
                }
            });
        }

        @Override
        public Supplier<Object> getRenderProvider() {
            return this.renderProvider;
        }

        @Override
        public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
            controllerRegistrar.add(new AnimationController<>(this, "controller", 20, state -> PlayState.STOP));
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return this.cache;
        }
    }

    public static class SpeedyBootItem extends BaseBootItem {
        public SpeedyBootItem(ArmorMaterial material, Type type, Settings settings) {
            super(material, type, settings);
        }
    }

    public static class ExtraSpeedyBootItem extends BaseBootItem {
        public ExtraSpeedyBootItem(ArmorMaterial material, Type type, Settings settings) {
            super(material, type, settings);
        }
    }
}
