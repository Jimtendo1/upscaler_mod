package net.jimtendo.upscaler_mod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.jimtendo.upscaler_mod.UpscaleConfig;
import net.jimtendo.upscaler_mod.UpscalerMod;
import net.jimtendo.upscaler_mod.UpscalerModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(CallbackInfo ci) {
        UpscalerModClient.framebufferHelper.updateFramebufferSize(false);
    }

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void redirectFramebuffer(CallbackInfo ci) {
        if (UpscalerModClient.framebufferHelper.customFbo != null) {
            UpscalerModClient.framebufferHelper.customFbo.beginWrite(true);
        }
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void restoreFramebuffer(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        Framebuffer mainFbo = client.getFramebuffer();

        if (UpscalerModClient.framebufferHelper.customFbo != null) {
            // Bind main framebuffer
            mainFbo.beginWrite(true);

            // Draw upscaled content with linear filtering
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            UpscalerModClient.framebufferHelper.customFbo.draw(
                    mainFbo.viewportWidth,
                    mainFbo.viewportHeight,
                    false // Keep blending enabled
            );
        }
    }
}