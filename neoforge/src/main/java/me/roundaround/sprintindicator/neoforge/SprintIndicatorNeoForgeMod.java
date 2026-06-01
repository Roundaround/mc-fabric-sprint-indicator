package me.roundaround.sprintindicator.neoforge;

import me.roundaround.sprintindicator.client.SprintIndicatorConfigScreen;
import me.roundaround.sprintindicator.config.SprintIndicatorConfig;
import me.roundaround.trove.neoforge.TroveNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("sprintindicator")
public final class SprintIndicatorNeoForgeMod {
  public SprintIndicatorNeoForgeMod(IEventBus modBus, ModContainer container) {
    TroveNeoForge.bootstrap(modBus, container);
    SprintIndicatorConfig.INSTANCE.init();

    // Wire the config screen into the mods-list config button. Client-only mod,
    // so referencing the client-side IConfigScreenFactory here is safe.
    container.registerExtensionPoint(IConfigScreenFactory.class,
        (modContainer, parent) -> new SprintIndicatorConfigScreen(parent));
  }
}
