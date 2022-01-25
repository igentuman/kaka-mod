package com.igentuman.kaka.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    private static boolean loaded = false;
    private static List<Runnable> loadActions = new ArrayList<>();

    public static void setLoaded() {
        if (!loaded)
            loadActions.forEach(Runnable::run);
        loaded = true;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static void onLoad(Runnable action) {
        if (loaded)
            action.run();
        else
            loadActions.add(action);
    }

    public static class General {
        public final ForgeConfigSpec.ConfigValue<Float> player_fart_volume;
        public final ForgeConfigSpec.ConfigValue<Float> mobs_fart_volume;


        public General(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            player_fart_volume = builder
                    .comment("Volume of player fart sound (0 to disable)")
                    .define("player_fart_volume", 0.1F);

            mobs_fart_volume = builder
                    .comment("Volume of mobs fart sound (0 to disable)")
                    .define("mobs_fart_volume", 0.3F);

            builder.pop();
        }
    }
}
