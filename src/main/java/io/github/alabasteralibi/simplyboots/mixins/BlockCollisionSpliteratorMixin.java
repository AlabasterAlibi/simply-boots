package io.github.alabasteralibi.simplyboots.mixins;

import io.github.alabasteralibi.simplyboots.registry.SimplyBootsAttributes;
import io.github.alabasteralibi.simplyboots.registry.SimplyBootsTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(BlockCollisionSpliterator.class)
public class BlockCollisionSpliteratorMixin {
    @Shadow
    @Final
    private @Nullable Entity entity;

    @Shadow
    @Final
    private BlockPos.Mutable pos;

    @Shadow
    @Final
    private VoxelShape boxShape;

    // Intercepts the vanilla code for colliding with blocks, making it treat fluids as solid in the right circumstances.
    @Inject(method = "offerBlockShape", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void test(Consumer<? super VoxelShape> action, CallbackInfoReturnable<Boolean> cir, int i, int j, int k, int l, BlockView blockView) {
        if (this.entity == null || !(this.entity instanceof LivingEntity entity)) { return; }
        if (entity.getVelocity().y >= 0 || entity.updateMovementInFluid(FluidTags.LAVA, 0) || entity.updateMovementInFluid(FluidTags.WATER  , 0) || entity.isSneaking()) { return; }
        ItemStack boots = entity.getEquippedStack(EquipmentSlot.FEET);
        if (!boots.isIn(SimplyBootsTags.FLUID_WALKING_BOOTS)) { return; }

        FluidState fluidState = blockView.getFluidState(new BlockPos(i, j, k));
        if (fluidState.isIn(FluidTags.LAVA) && !boots.isIn(SimplyBootsTags.HOT_FLUID_WALKING_BOOTS)) { return; }
        if (fluidState.isEmpty() || !blockView.getFluidState(new BlockPos(i, j + 1, k)).isEmpty()) { return; }

        if ((entity.getY() + (entity.isOnGround() ? SimplyBootsAttributes.getStepHeight(entity) : 0)) - (j + fluidState.getHeight()) >= -1E-6) { // Epsilon
            VoxelShape voxelShape = fluidState.getShape(blockView, this.pos).offset(i, j, k);
            if (VoxelShapes.matchesAnywhere(voxelShape, this.boxShape, BooleanBiFunction.AND)) {
                entity.fallDistance = 0.0F;
                action.accept(voxelShape);
                cir.setReturnValue(true);
            }
        }
    }
}
