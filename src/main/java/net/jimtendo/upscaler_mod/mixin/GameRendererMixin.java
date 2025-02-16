package net.jimtendo.upscaler_mod.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import net.jimtendo.upscaler_mod.FramebufferHelper;
import net.jimtendo.upscaler_mod.UpscalerModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.opengl.GL30C;
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
        FramebufferHelper fbHelper = UpscalerModClient.framebufferHelper;

        if (fbHelper.customFbo != null) {
            // Save the current bound framebuffer
            int originalFbo = GlStateManager.getBoundFramebuffer();

            // Bind custom FBO as the read framebuffer
            GlStateManager._glBindFramebuffer(GL30C.GL_READ_FRAMEBUFFER, fbHelper.customFbo.fbo);
            // Bind main FBO as the draw framebuffer
            GlStateManager._glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, mainFbo.fbo);

            // Blit both color and depth buffers
            GL30C.glBlitFramebuffer(
                    0, 0, fbHelper.customFbo.textureWidth, fbHelper.customFbo.textureHeight,
                    0, 0, mainFbo.viewportWidth, mainFbo.viewportHeight,
                    GL30C.GL_COLOR_BUFFER_BIT | GL30C.GL_DEPTH_BUFFER_BIT,
                    GL30C.GL_NEAREST
            );

            // Restore the original framebuffer bindings
            GlStateManager._glBindFramebuffer(GL30C.GL_FRAMEBUFFER, originalFbo);

        }
    }
}