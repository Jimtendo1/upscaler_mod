package net.jimtendo.upscaler_mod;

import com.mojang.blaze3d.platform.GlConst;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;

public class FramebufferHelper {
    public SimpleFramebuffer customFbo;
    private boolean needsResize = true;

    public void updateFramebufferSize(boolean force) {
        MinecraftClient client = MinecraftClient.getInstance();
        float factor = UpscalerModClient.CONFIG.scalingFactor.factor;

        int newWidth = Math.max((int)(client.getWindow().getFramebufferWidth() * factor), 1);
        int newHeight = Math.max((int)(client.getWindow().getFramebufferHeight() * factor), 1);

        if (customFbo == null) {
            customFbo = new SimpleFramebuffer(newWidth, newHeight, true, false);
            customFbo.setTexFilter(GlConst.GL_LINEAR);
            needsResize = false;
        } else if (needsResize || force ||
                customFbo.textureWidth != newWidth ||
                customFbo.textureHeight != newHeight) {
            customFbo.resize(newWidth, newHeight, false);
            needsResize = false;
        }
    }

    public void markForResize() {
        needsResize = true;
    }
}