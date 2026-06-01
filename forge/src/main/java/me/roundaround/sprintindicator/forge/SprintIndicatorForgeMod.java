package me.roundaround.sprintindicator.forge;

import me.roundaround.trove.forge.TroveForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("sprintindicator")
public final class SprintIndicatorForgeMod {
  public SprintIndicatorForgeMod(FMLJavaModLoadingContext context) {
    TroveForge.bootstrap(context);
    // Mixin-only client HUD mod: nothing else to register.
  }
}
