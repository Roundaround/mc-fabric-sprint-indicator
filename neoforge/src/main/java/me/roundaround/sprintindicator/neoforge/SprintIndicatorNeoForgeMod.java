package me.roundaround.sprintindicator.neoforge;

import me.roundaround.trove.neoforge.TroveNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod("sprintindicator")
public final class SprintIndicatorNeoForgeMod {
  public SprintIndicatorNeoForgeMod(IEventBus modBus, ModContainer container) {
    TroveNeoForge.bootstrap(modBus, container);
    // Mixin-only client HUD mod: nothing else to register.
  }
}
