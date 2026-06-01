package me.roundaround.sprintindicator.client;

import me.roundaround.sprintindicator.config.SprintIndicatorConfig;
import me.roundaround.trove.client.gui.screen.ConfigScreen;
import me.roundaround.trove.client.gui.screen.PositionEditScreen;
import me.roundaround.trove.client.gui.util.Alignment;
import me.roundaround.trove.client.gui.util.GuiUtil;
import me.roundaround.trove.client.gui.widget.config.SubScreenControl;
import me.roundaround.trove.client.gui.widget.drawable.LabelWidget;
import me.roundaround.trove.config.option.PositionConfigOption;
import me.roundaround.trove.config.value.Position;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

// Sub-screen for editing the indicator offset. PositionEditScreen supplies the
// directional movers, crosshair, back/reset buttons, help text, and arrow-key
// nudging (Shift = larger steps). On top of that this screen renders a live
// preview: a hotbar pinned to the bottom (the real one, with the player's items,
// when editing from inside a world; an empty sample otherwise) with the indicator
// icon drawn on top at the current offset, updating as you nudge or click-and-drag.
// The geometry mirrors GuiMixin exactly (this.width/this.height
// are the same gui-scaled coordinates the HUD draws into), so it is pixel-faithful.
// The controls keep their default bottom-right placement; the help text is pinned to
// the top-left and the current offset value to the bottom-left, all clear of the
// bottom-center hotbar preview.
public final class SprintIndicatorOffsetScreen extends PositionEditScreen {
  private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
  private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
  private static final int HOTBAR_WIDTH = 182;
  private static final int INDICATOR_SIZE = 18;

  private SprintIndicatorOffsetScreen(ConfigScreen parent, PositionConfigOption option) {
    super(Component.translatable("sprintindicator.offset.title"), parent, option);
  }

  public static SubScreenControl.SubScreenFactory<Position, PositionConfigOption> factory() {
    return SprintIndicatorOffsetScreen::new;
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
    super.extractBackground(graphics, mouseX, mouseY, delta);

    int center = this.width / 2;
    int hotbarLeft = center - HOTBAR_WIDTH / 2;
    int hotbarTop = this.height - 22;

    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, hotbarLeft, hotbarTop, HOTBAR_WIDTH, 22);

    LocalPlayer player = this.minecraft.player;
    int selectedSlot = player != null ? player.getInventory().getSelectedSlot() : 0;
    graphics.blitSprite(
        RenderPipelines.GUI_TEXTURED,
        HOTBAR_SELECTION_SPRITE,
        hotbarLeft - 1 + selectedSlot * 20,
        hotbarTop - 1,
        24,
        23
    );

    if (player != null) {
      int seed = 1;
      for (int i = 0; i < 9; i++) {
        ItemStack stack = player.getInventory().getItem(i);
        if (stack.isEmpty()) {
          continue;
        }
        int x = center - 90 + i * 20 + 2;
        int y = this.height - 16 - 3;
        graphics.item(player, stack, x, y, seed++);
        graphics.itemDecorations(this.font, stack, x, y);
      }
    }
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
    super.extractRenderState(graphics, mouseX, mouseY, delta);

    graphics.blitSprite(
        RenderPipelines.GUI_TEXTURED,
        this.previewSprite(),
        this.indicatorX(),
        this.indicatorY(),
        INDICATOR_SIZE,
        INDICATOR_SIZE
    );

    graphics.text(
        this.font,
        Component.literal(this.getValue().toString()),
        GuiUtil.PADDING,
        this.height - GuiUtil.PADDING - this.font.lineHeight,
        GuiUtil.LABEL_COLOR
    );
  }

  @Override
  protected boolean supportsDragging() {
    return true;
  }

  @Override
  protected boolean isInDraggableRegion(double mouseX, double mouseY) {
    int x = this.indicatorX();
    int y = this.indicatorY();
    return mouseX >= x && mouseX < x + INDICATOR_SIZE && mouseY >= y && mouseY < y + INDICATOR_SIZE;
  }

  // Pin the help text to the top-left instead of its default bottom-left, clearing
  // the bottom-center hotbar preview. The back/reset buttons and directional movers
  // keep their default bottom-right placement.
  @Override
  protected void placeHelpLabel(LabelWidget helpLabel) {
    helpLabel.setAlignTextY(Alignment.START);
    super.placeHelpLabel(helpLabel);
  }

  // Matches GuiMixin#afterRenderHotbar: anchored to the off-hand side of the
  // hotbar, then shifted by the configured offset.
  private int indicatorX() {
    Position offset = this.getValue();
    return (this.previewArm() == HumanoidArm.LEFT
        ? (this.width + HOTBAR_WIDTH) / 2 + 6
        : (this.width - HOTBAR_WIDTH) / 2 - INDICATOR_SIZE - 6) + offset.x();
  }

  private int indicatorY() {
    return this.height - 20 + this.getValue().y();
  }

  private HumanoidArm previewArm() {
    LocalPlayer player = this.minecraft.player;
    return (player != null ? player.getMainArm() : HumanoidArm.RIGHT).getOpposite();
  }

  // Prefer the sprint (speed) icon; fall back to the crouch (slowness) icon only
  // when sprint display is off and crouch is on, so the preview shows whatever the
  // user will actually see.
  private Identifier previewSprite() {
    SprintIndicatorConfig config = SprintIndicatorConfig.INSTANCE;
    Holder<MobEffect> effect = !config.sprintEnabled.getValue() && config.crouchEnabled.getValue()
        ? MobEffects.SLOWNESS
        : MobEffects.SPEED;
    return Gui.getMobEffectSprite(effect);
  }
}
