package me.roundaround.sprintindicator.forge;

import me.roundaround.sprintindicator.client.SprintIndicatorConfigScreen;
import me.roundaround.sprintindicator.config.SprintIndicatorConfig;
import me.roundaround.trove.forge.TroveForge;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("sprintindicator")
public final class SprintIndicatorForgeMod {
  public SprintIndicatorForgeMod(FMLJavaModLoadingContext context) {
    TroveForge.bootstrap(context);
    SprintIndicatorConfig.INSTANCE.init();

    // Wire the config screen into the mods-list config button. Client-only mod,
    // so referencing the client-side ConfigScreenHandler here is safe.
    context.getContainer().registerExtensionPoint(
        ConfigScreenHandler.ConfigScreenFactory.class,
        () -> new ConfigScreenHandler.ConfigScreenFactory(
            (mc, parent) -> new SprintIndicatorConfigScreen(parent)));
  }
}
