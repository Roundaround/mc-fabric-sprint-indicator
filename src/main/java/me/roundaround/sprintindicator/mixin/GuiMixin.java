package me.roundaround.sprintindicator.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {
  @Shadow
  private Player getCameraPlayer() {
    return null;
  }

  @Inject(method = "extractItemHotbar", at = @At(value = "TAIL"))
  private void afterRenderHotbar(GuiGraphicsExtractor context, DeltaTracker tickCounter, CallbackInfo ci) {
    Player basePlayer = this.getCameraPlayer();
    if (!(basePlayer instanceof LocalPlayer player)) {
      return;
    }

    if (!player.isSprinting()) {
      return;
    }

    HumanoidArm arm = player.getMainArm().getOpposite();

    int scaledWidth = context.guiWidth();
    int scaledHeight = context.guiHeight();
    int x = arm == HumanoidArm.LEFT ? (scaledWidth + 182) / 2 + 6 : (scaledWidth - 182) / 2 - 18 - 6;
    int y = scaledHeight - 20;

    Identifier texture = Gui.getMobEffectSprite(MobEffects.SPEED);
    context.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x, y, 18, 18);
  }

  @ModifyArg(
      method = "extractItemHotbar", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite" +
               "(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
  ), index = 2, slice = @Slice(
      from = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;"
      ), to = @At(
      value = "TAIL"
  )
  )
  )
  private int modifyAttackIndicatorBackgroundX(int original) {
    return this.adjustAttackIndicatorX(original);
  }

  @ModifyArg(
      method = "extractItemHotbar", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite" +
               "(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIIIIIII)V"
  ), index = 6, slice = @Slice(
      from = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;"
      ), to = @At(
      value = "TAIL"
  )
  )
  )
  private int modifyAttackIndicatorForegroundX(int original) {
    return this.adjustAttackIndicatorX(original);
  }

  @Unique
  private int adjustAttackIndicatorX(int original) {
    Player player = this.getCameraPlayer();
    if (player == null) {
      return original;
    }
    HumanoidArm arm = player.getMainArm().getOpposite();
    return original + (arm == HumanoidArm.LEFT ? 24 : -24);
  }
}
