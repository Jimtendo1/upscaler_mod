package net.jimtendo.upscaler_mod.mixin;

import net.jimtendo.upscaler_mod.UpscaleSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class VideoSettingsScreenMixin extends Screen {
    protected VideoSettingsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addUpscaleButton(CallbackInfo ci) {
        this.addDrawableChild(
                ButtonWidget.builder(
                                Text.translatable("Upscaling Settings"),
                                button -> MinecraftClient.getInstance().setScreen(new UpscaleSettingsScreen(this))
                        )
                        .dimensions(
                                this.width / 2 + 160,
                                this.height / 6 - 12,
                                21,
                                20
                        )
                        .build()
        );
    }
}