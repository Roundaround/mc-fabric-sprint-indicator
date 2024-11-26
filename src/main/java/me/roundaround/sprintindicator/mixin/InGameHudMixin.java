package me.roundaround.sprintindicator.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Final
  @Shadow
  private MinecraftClient client;

  @Shadow
  private PlayerEntity getCameraPlayer() {
    return null;
  }

  @Inject(method = "renderHotbar", at = @At(value = "TAIL"))
  private void afterRenderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
    PlayerEntity basePlayer = getCameraPlayer();
    if (!(basePlayer instanceof ClientPlayerEntity player)) {
      return;
    }

    if (!player.isSprinting()) {
      return;
    }

    Arm arm = player.getMainArm().getOpposite();

    int scaledWidth = context.getScaledWindowWidth();
    int scaledHeight = context.getScaledWindowHeight();
    int x = arm == Arm.LEFT ? (scaledWidth + 182) / 2 + 6 : (scaledWidth - 182) / 2 - 18 - 6;
    int y = scaledHeight - 20;

    StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
    Sprite sprite = statusEffectSpriteManager.getSprite(StatusEffects.SPEED);
    context.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, x, y, 18, 18, Colors.WHITE);
  }

  @ModifyArg(
      method = "renderHotbar", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;" +
               "Lnet/minecraft/util/Identifier;IIII)V"
  ), index = 2, slice = @Slice(
      from = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()" +
                   "Lnet/minecraft/client/option/SimpleOption;"
      ), to = @At(
      value = "TAIL"
  )
  )
  )
  private int modifyAttackIndicatorBackgroundX(int original) {
    return this.adjustAttackIndicatorX(original);
  }

  @ModifyArg(
      method = "renderHotbar", at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;" +
               "Lnet/minecraft/util/Identifier;IIIIIIII)V"
  ), index = 6, slice = @Slice(
      from = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()" +
                   "Lnet/minecraft/client/option/SimpleOption;"
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
    PlayerEntity player = getCameraPlayer();
    if (player == null) {
      return original;
    }
    Arm arm = player.getMainArm().getOpposite();
    return original + (arm == Arm.LEFT ? 24 : -24);
  }
}
