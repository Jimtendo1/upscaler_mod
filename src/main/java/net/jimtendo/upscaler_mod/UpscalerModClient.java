package net.jimtendo.upscaler_mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class UpscalerModClient implements ClientModInitializer {
    public static final UpscaleConfig CONFIG = UpscaleConfig.load();
    public static FramebufferHelper framebufferHelper = new FramebufferHelper();

    @Override
    public void onInitializeClient() {
        framebufferHelper = new FramebufferHelper();
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> CONFIG.save());
    }
}