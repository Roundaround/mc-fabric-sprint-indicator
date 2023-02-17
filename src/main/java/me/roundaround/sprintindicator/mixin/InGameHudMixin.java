package me.roundaround.sprintindicator.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
  @Shadow
  private MinecraftClient client;
  @Shadow
  private int scaledWidth;
  @Shadow
  private int scaledHeight;

  @Shadow
  protected abstract PlayerEntity getCameraPlayer();

  @Shadow
  public abstract TextRenderer getTextRenderer();

  @Inject(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"))
  private void afterRenderHotbar(float partialTicks, MatrixStack matrixStack, CallbackInfo info) {
    PlayerEntity basePlayer = getCameraPlayer();
    if (!(basePlayer instanceof ClientPlayerEntity)) {
      return;
    }

    ClientPlayerEntity player = (ClientPlayerEntity) basePlayer;
    if (!player.isSprinting()) {
      return;
    }

    Arm arm = player.getMainArm().getOpposite();

    int x = arm == Arm.LEFT ? (this.scaledWidth + 182) / 2 + 6 : (this.scaledWidth - 182) / 2 - 18 - 6;
    int y = this.scaledHeight - 20;

    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();

    StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
    Sprite sprite = statusEffectSpriteManager.getSprite(StatusEffects.SPEED);
    RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
    InGameHud.drawSprite(matrixStack, x, y, ((InGameHud) (Object) this).getZOffset(), 18, 18, sprite);
  }

  // @formatter:off
  @ModifyArgs(
    method = "renderHotbar",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
      ordinal = 0
    ),
    slice = @Slice(
      from = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;"
      ),
      to = @At(
        value = "INVOKE",
        target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"
      )
    )
  )
  // @formatter:on
  private void modifyArgsToFirstDrawTexture(Args args) {
    PlayerEntity player = getCameraPlayer();
    Arm arm = player.getMainArm().getOpposite();

    if (arm == Arm.LEFT) {
      args.set(1, (int) args.get(1) + 24);
    } else {
      args.set(1, (int) args.get(1) - 24);
    }
  }

  // @formatter:off
  @ModifyArgs(
    method = "renderHotbar",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V",
      ordinal = 1
    ),
    slice = @Slice(
      from = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/option/GameOptions;getAttackIndicator()Lnet/minecraft/client/option/SimpleOption;"
      ),
      to = @At(
        value = "INVOKE",
        target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"
      )
    )
  )
  // @formatter:on
  private void modifyArgsToSecondDrawTexture(Args args) {
    PlayerEntity player = getCameraPlayer();
    Arm arm = player.getMainArm().getOpposite();

    if (arm == Arm.LEFT) {
      args.set(1, (int) args.get(1) + 26);
    } else {
      args.set(1, (int) args.get(1) - 26);
    }
  }
}
