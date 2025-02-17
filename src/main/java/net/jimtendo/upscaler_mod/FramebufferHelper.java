package net.jimtendo.upscaler_mod;

import com.mojang.blaze3d.platform.GlConst;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.SimpleFramebuffer;

public class FramebufferHelper {
    public SimpleFramebuffer customFbo;
    private boolean needsResize = true;

    public void updateFramebufferSize(boolean force) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Early exit if upscaling is disabled
        if (UpscalerModClient.CONFIG.algorithm == UpscaleConfig.ScalingAlgorithm.NONE) {
            if (customFbo != null) {
                customFbo.delete();
                customFbo = null;
            }
            return;
        }

        // Only calculate scaling if algorithm is active
        float factor = UpscalerModClient.CONFIG.scalingFactor.factor;
        int newWidth = Math.max((int)(client.getWindow().getFramebufferWidth() * factor), 1);
        int newHeight = Math.max((int)(client.getWindow().getFramebufferHeight() * factor), 1);

        // Create new FBO if none exists
        if (customFbo == null) {
            customFbo = new SimpleFramebuffer(newWidth, newHeight, true, false);
            customFbo.setTexFilter(GlConst.GL_LINEAR);
            needsResize = false;
        }
        // Resize existing FBO if needed
        else if (needsResize || force ||
                customFbo.textureWidth != newWidth ||
                customFbo.textureHeight != newHeight) {
            customFbo.resize(newWidth, newHeight, false);
            needsResize = false;
        }
    }

    public void markForResize() {
        needsResize = true;
    }

    // Cleanup when the mod is unloaded
    public void cleanup() {
        if (customFbo != null) {
            customFbo.delete();
            customFbo = null;
        }
    }
}