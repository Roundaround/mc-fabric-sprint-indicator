package me.roundaround.sprintindicator;

import me.roundaround.allay.api.Entrypoint;
import me.roundaround.sprintindicator.config.SprintIndicatorConfig;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint(Entrypoint.CLIENT)
public final class SprintIndicatorMod implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // Load the config from disk so GuiMixin reads live values. The ModMenu
    // config screen is wired separately via SprintIndicatorModMenuIntegration.
    // Trove's Fabric core self-bootstraps via its own bundled entrypoint.
    SprintIndicatorConfig.INSTANCE.init();
  }
}
