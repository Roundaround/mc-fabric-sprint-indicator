package me.roundaround.sprintindicator;

import me.roundaround.allay.api.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint(Entrypoint.CLIENT)
public final class SprintIndicatorMod implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // Mixin-only client HUD mod: the sprint indicator is drawn entirely from
    // GuiMixin. No config, networking, commands, or key bindings to wire here.
    // Trove's Fabric core self-bootstraps via its own bundled entrypoint.
  }
}
