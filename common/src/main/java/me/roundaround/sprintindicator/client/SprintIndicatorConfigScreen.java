package me.roundaround.sprintindicator.client;

import me.roundaround.sprintindicator.config.SprintIndicatorConfig;
import me.roundaround.trove.client.gui.screen.ConfigScreen;
import me.roundaround.trove.client.gui.widget.config.ControlRegistry;
import me.roundaround.trove.client.gui.widget.config.SubScreenControl;
import me.roundaround.trove.config.option.PositionConfigOption;
import net.minecraft.client.gui.screens.Screen;

// Thin wrapper around Trove's ConfigScreen, pre-bound to this mod's modId and
// config instance. ConfigScreen auto-builds one row per registered option
// (toggle for the booleans, sub-screen editor for the offset).
public final class SprintIndicatorConfigScreen extends ConfigScreen {
  static {
    // Trove ships default control factories for Boolean/Int/Float/String/enum
    // options, but not PositionConfigOption — opening the screen without this
    // registration panics on the offset row. Bind every PositionConfigOption to
    // the directional nudge sub-screen.
    try {
      ControlRegistry.register(
          PositionConfigOption.class,
          (client, option, width, height) -> new SubScreenControl<>(
              client,
              option,
              width,
              height,
              SubScreenControl.getValueDisplayMessageFactory(),
              SprintIndicatorOffsetScreen.factory()
          )
      );
    } catch (ControlRegistry.RegistrationException ignored) {
      // already registered (e.g. class reloaded) — safe to swallow
    }
  }

  public SprintIndicatorConfigScreen(Screen parent) {
    super(parent, "sprintindicator", SprintIndicatorConfig.INSTANCE);
  }
}
