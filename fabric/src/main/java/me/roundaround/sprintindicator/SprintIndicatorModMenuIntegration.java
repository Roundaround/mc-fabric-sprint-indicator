package me.roundaround.sprintindicator;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.roundaround.allay.api.Entrypoint;
import me.roundaround.sprintindicator.client.SprintIndicatorConfigScreen;

// @Entrypoint(MOD_MENU) — Allay's bytecode scanner emits this as the
// entrypoints.modmenu array in the generated fabric.mod.json. ModMenu reads it
// at runtime and uses the returned factory to open the config screen from the
// mod-list gear icon. NeoForge/Forge use native config-screen APIs wired in
// their own entrypoints.
@Entrypoint(Entrypoint.MOD_MENU)
public final class SprintIndicatorModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return SprintIndicatorConfigScreen::new;
  }
}
