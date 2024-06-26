package me.roundaround.sprintindicator.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

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
    context.setShaderColor(1f, 1f, 1f, 1f);
    context.drawSprite(x, y, 0, 18, 18, sprite);
  }

  @ModifyArgs(
    method = "renderHotbar",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V",
      ordinal = 0
    ),
    slice = @Slice(
      from = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;"
      ),
      to = @At(
        value = "TAIL"
      )
    )
  )
  private void modifyArgsToFirstDrawTexture(Args args) {
    PlayerEntity player = getCameraPlayer();
    if (player == null) {
      return;
    }

    Arm arm = player.getMainArm().getOpposite();

    if (arm == Arm.LEFT) {
      args.set(1, (int) args.get(1) + 24);
    } else {
      args.set(1, (int) args.get(1) - 24);
    }
  }

  @ModifyArgs(
    method = "renderHotbar",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIIIIIII)V",
      ordinal = 0
    ),
    slice = @Slice(
      from = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;"
      ),
      to = @At(
        value = "TAIL"
      )
    )
  )
  private void modifyArgsToSecondDrawTexture(Args args) {
    PlayerEntity player = getCameraPlayer();
    if (player == null) {
      return;
    }

    Arm arm = player.getMainArm().getOpposite();

    if (arm == Arm.LEFT) {
      args.set(5, (int) args.get(5) + 24);
    } else {
      args.set(5, (int) args.get(5) - 24);
    }
  }
}
