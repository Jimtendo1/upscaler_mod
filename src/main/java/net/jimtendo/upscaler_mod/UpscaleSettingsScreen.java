package net.jimtendo.upscaler_mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class UpscaleSettingsScreen extends Screen {
    private final Screen parent;
    private ButtonWidget scalingFactorButton;

    public UpscaleSettingsScreen(Screen parent) {
        super(Text.translatable("Upscaling Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Algorithm Button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.translatable("Algorithm: " + UpscalerModClient.CONFIG.algorithm.name()),
                        button -> {
                            // Cycle algorithm
                            UpscaleConfig.ScalingAlgorithm[] algorithms = UpscaleConfig.ScalingAlgorithm.values();
                            int next = (UpscalerModClient.CONFIG.algorithm.ordinal() + 1) % algorithms.length;
                            UpscalerModClient.CONFIG.algorithm = algorithms[next];
                            button.setMessage(Text.translatable("Algorithm: " + UpscalerModClient.CONFIG.algorithm.name()));

                            // Immediately update scaling factor button state
                            scalingFactorButton.active = (UpscalerModClient.CONFIG.algorithm != UpscaleConfig.ScalingAlgorithm.NONE);
                            UpscalerModClient.framebufferHelper.markForResize();
                        })
                .position(this.width / 2 - 100, this.height / 4 + 24)
                .size(200, 20)
                .build());

        // Scaling Factor Button
        scalingFactorButton = ButtonWidget.builder(
                        Text.translatable("Factor: " + UpscalerModClient.CONFIG.scalingFactor.name()),
                        button -> {
                            UpscaleConfig.ScalingFactor[] values = UpscaleConfig.ScalingFactor.values();
                            int next = (UpscalerModClient.CONFIG.scalingFactor.ordinal() + 1) % values.length;
                            UpscalerModClient.CONFIG.scalingFactor = values[next];
                            button.setMessage(Text.translatable("Factor: " + UpscalerModClient.CONFIG.scalingFactor.name()));
                            UpscalerModClient.framebufferHelper.markForResize();
                        })
                .position(this.width / 2 - 100, this.height / 4 + 48)
                .size(200, 20)
                .build();
        scalingFactorButton.active = (UpscalerModClient.CONFIG.algorithm != UpscaleConfig.ScalingAlgorithm.NONE);
        this.addDrawableChild(scalingFactorButton);

        // Done Button
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("Done"), button -> {
            MinecraftClient.getInstance().setScreen(parent);
        }).position(this.width / 2 - 100, this.height / 4 + 120).size(200, 20).build());
    }
}