package net.jimtendo.upscaler_mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.GlConst;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpscaleConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("upscaler_mod.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public enum ScalingAlgorithm {
        NONE,
        BILINEAR
    }
    public enum ScalingFactor { NONE(1.0f), QUALITY(0.67f), BALANCED(0.58f), PERFORMANCE(0.5f),ULTRA_PERFORMANCE(0.33f);

        public final float factor;
        ScalingFactor(float factor) { this.factor = factor; }
    }

    public ScalingAlgorithm algorithm = ScalingAlgorithm.BILINEAR;
    public ScalingFactor scalingFactor = ScalingFactor.QUALITY;

    public static UpscaleConfig load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                return GSON.fromJson(Files.readString(CONFIG_PATH), UpscaleConfig.class);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return new UpscaleConfig();
    }

    public void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) { e.printStackTrace(); }
    }
}