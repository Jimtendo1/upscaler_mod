package net.jimtendo.upscaler_mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class UpscaleSettingsScreen extends Screen {
    private final Screen parent;

    public UpscaleSettingsScreen(Screen parent) {
        super(Text.translatable("Upscaling Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("Scaling: " + UpscalerModClient.CONFIG.scalingFactor.name()),
                        button -> {
                            UpscaleConfig.ScalingFactor[] values = UpscaleConfig.ScalingFactor.values();
                            int next = (UpscalerModClient.CONFIG.scalingFactor.ordinal() + 1) % values.length;
                            UpscalerModClient.CONFIG.scalingFactor = values[next];
                            button.setMessage(Text.translatable("Scaling: " + UpscalerModClient.CONFIG.scalingFactor.name()));
                        })
                .position(this.width / 2 - 100, this.height / 4 + 48)
                .size(200, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("Done"), button -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).position(this.width / 2 - 100, this.height / 4 + 120).size(200, 20).build());
    }
    private void updateScalingFactor() {
        UpscaleConfig.ScalingFactor[] values = UpscaleConfig.ScalingFactor.values();
        int next = (UpscalerModClient.CONFIG.scalingFactor.ordinal() + 1) % values.length;
        UpscalerModClient.CONFIG.scalingFactor = values[next];
        UpscalerModClient.framebufferHelper.markForResize(); // Force resize
        UpscalerModClient.CONFIG.save(); // Immediate save
    }
}