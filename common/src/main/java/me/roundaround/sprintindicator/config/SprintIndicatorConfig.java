package me.roundaround.sprintindicator.config;

import me.roundaround.trove.config.ConfigPath;
import me.roundaround.trove.config.manage.ModConfigImpl;
import me.roundaround.trove.config.manage.store.GameScopedFileStore;
import me.roundaround.trove.config.option.BooleanConfigOption;
import me.roundaround.trove.config.option.PositionConfigOption;
import me.roundaround.trove.config.value.Position;

public final class SprintIndicatorConfig extends ModConfigImpl implements GameScopedFileStore {
  public static final SprintIndicatorConfig INSTANCE = new SprintIndicatorConfig();

  public final PositionConfigOption offset;
  public final BooleanConfigOption sprintEnabled;
  public final BooleanConfigOption crouchEnabled;
  public final BooleanConfigOption adjustAttackIndicator;

  private SprintIndicatorConfig() {
    super("sprintindicator");

    this.sprintEnabled = BooleanConfigOption.builder(ConfigPath.of("sprintEnabled"))
        .setComment("Whether to show an icon when sprinting.")
        .setDefaultValue(true)
        .build();

    this.crouchEnabled = BooleanConfigOption.builder(ConfigPath.of("crouchEnabled"))
        .setComment("Whether to show an icon when crouching.")
        .setDefaultValue(true)
        .build();

    this.offset = PositionConfigOption.builder(ConfigPath.of("offset"))
        .setComment("An x,y pixel offset for rendering the icons.")
        .setDefaultValue(new Position(0, 0))
        .build();

    this.adjustAttackIndicator = BooleanConfigOption.builder(ConfigPath.of("adjustAttackIndicator"))
        .setComment("Whether to shift the attack indicator to put the sprint/crouch icons closer to the hotbar.")
        .setDefaultValue(true)
        .build();
  }

  @Override
  protected void registerOptions() {
    this.register(this.sprintEnabled);
    this.register(this.crouchEnabled);
    this.register(this.offset);
    this.register(this.adjustAttackIndicator);
  }
}
