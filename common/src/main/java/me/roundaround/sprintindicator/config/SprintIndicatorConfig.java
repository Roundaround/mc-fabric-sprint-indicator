package me.roundaround.sprintindicator.config;

import me.roundaround.trove.config.ConfigPath;
import me.roundaround.trove.config.manage.ModConfigImpl;
import me.roundaround.trove.config.manage.store.GameScopedFileStore;
import me.roundaround.trove.config.option.BooleanConfigOption;
import me.roundaround.trove.config.option.PositionConfigOption;
import me.roundaround.trove.config.value.Position;

// Trove-backed config for Sprint Indicator. Extends ModConfigImpl for the
// option registry and mixes in GameScopedFileStore so values persist to
// {gameDir}/config/sprintindicator.toml. Every default reproduces the mod's
// pre-config behavior, so existing users see no change until they opt in.
//
// Source imports reference the un-relocated me.roundaround.trove.* FQNs;
// Allay's Shadow step rewrites them to me.roundaround.sprintindicator.trove.*
// when it bundles the library into each loader JAR.
public final class SprintIndicatorConfig extends ModConfigImpl implements GameScopedFileStore {
  public static final SprintIndicatorConfig INSTANCE = new SprintIndicatorConfig();

  // Issue #1: nudge the indicator out from under other HUD mods (e.g. Raised).
  public final PositionConfigOption offset;
  // Toggle the original sprint indicator.
  public final BooleanConfigOption sprintEnabled;
  // Issue #4: crouch/sneak indicator.
  public final BooleanConfigOption crouchEnabled;
  // Keep the vanilla attack-indicator nudged aside to make room for the icon.
  public final BooleanConfigOption adjustAttackIndicator;

  private SprintIndicatorConfig() {
    super("sprintindicator");

    this.sprintEnabled = BooleanConfigOption.builder(ConfigPath.of("sprintEnabled"))
        .setDefaultValue(true)
        .build();

    this.crouchEnabled = BooleanConfigOption.builder(ConfigPath.of("crouchEnabled"))
        .setDefaultValue(true)
        .build();

    this.offset = PositionConfigOption.builder(ConfigPath.of("offset"))
        .setDefaultValue(new Position(0, 0))
        .build();

    this.adjustAttackIndicator = BooleanConfigOption.builder(ConfigPath.of("adjustAttackIndicator"))
        .setDefaultValue(true)
        .build();
  }

  @Override
  protected void registerOptions() {
    register(this.sprintEnabled);
    register(this.crouchEnabled);
    register(this.offset);
    register(this.adjustAttackIndicator);
  }
}
