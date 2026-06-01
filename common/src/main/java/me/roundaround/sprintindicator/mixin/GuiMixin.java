package me.roundaround.sprintindicator.mixin;

import me.roundaround.allay.api.MixinEnv;
import me.roundaround.sprintindicator.config.SprintIndicatorConfig;
import me.roundaround.trove.config.value.Position;
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

@MixinEnv(MixinEnv.Env.CLIENT)
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

    SprintIndicatorConfig config = SprintIndicatorConfig.INSTANCE;
    boolean sprinting = config.sprintEnabled.getValue() && player.isSprinting();
    boolean crouching = config.crouchEnabled.getValue() && player.isCrouching();
    if (!sprinting && !crouching) {
      return;
    }

    HumanoidArm arm = player.getMainArm().getOpposite();

    int scaledWidth = context.guiWidth();
    int scaledHeight = context.guiHeight();
    Position offset = config.offset.getValue();
    int x = (arm == HumanoidArm.LEFT ? (scaledWidth + 182) / 2 + 6 : (scaledWidth - 182) / 2 - 18 - 6) + offset.x();
    int y = scaledHeight - 20 + offset.y();
    Identifier texture = Gui.getMobEffectSprite(crouching ? MobEffects.SLOWNESS : MobEffects.SPEED);
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
    SprintIndicatorConfig config = SprintIndicatorConfig.INSTANCE;
    // Only reserve room for the indicator when the user wants the nudge and at
    // least one indicator can occupy the slot. (When the icon is moved far via
    // the offset, turning this off is the escape hatch.)
    if (!config.adjustAttackIndicator.getValue() ||
        !(config.sprintEnabled.getValue() || config.crouchEnabled.getValue())) {
      return original;
    }
    Player player = this.getCameraPlayer();
    if (player == null) {
      return original;
    }
    HumanoidArm arm = player.getMainArm().getOpposite();
    return original + (arm == HumanoidArm.LEFT ? 24 : -24);
  }
}
