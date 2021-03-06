package io.github.alabasteralibi.simplyboots.mixins;

import io.github.alabasteralibi.simplyboots.SimplyBootsHelpers;
import io.github.alabasteralibi.simplyboots.components.BootComponents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    private final PlayerEntity player = (PlayerEntity) (Object) this;

    // Extinguishes fire at the end of a tick when appropriate.
    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void updateFireImmunity(CallbackInfo ci) {
        if (SimplyBootsHelpers.isFireImmune(player)) {
            player.extinguish();
        }
    }

    // Cancels damage from fire/lava sources when appropriate.
    @Inject(method = "damage", at = @At(value = "HEAD"), cancellable = true)
    private void cancelLavaAndFireDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source == DamageSource.LAVA && BootComponents.LAVA_BOOTS.get(player).getValue() > 0 && SimplyBootsHelpers.wearingLavaImmune(player)) {
            cir.setReturnValue(false);
        }
        if (source != DamageSource.LAVA && source.isFire() && SimplyBootsHelpers.isFireImmune(player)) {
            cir.setReturnValue(false);
        }
    }
}

