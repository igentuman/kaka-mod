package com.igentuman.kaka.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CommonConfig {
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
        public final ForgeConfigSpec.ConfigValue<Boolean> cow_kaka;
        public final ForgeConfigSpec.ConfigValue<Boolean> pig_kaka;
        public final ForgeConfigSpec.ConfigValue<Boolean> horse_kaka;
        public final ForgeConfigSpec.ConfigValue<Boolean> sheep_kaka;
        public final ForgeConfigSpec.ConfigValue<Boolean> villager_kaka;
        public final ForgeConfigSpec.ConfigValue<Boolean> player_kaka;
        public final ForgeConfigSpec.ConfigValue<Integer> minimal_timespan;

        public General(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            cow_kaka = builder
                    .comment("Allow cow kaka")
                    .define("cow_kaka", true);

            pig_kaka = builder
                    .comment("Allow pig kaka")
                    .define("pig_kaka", true);

            horse_kaka = builder
                    .comment("Allow horse kaka")
                    .define("horse_kaka", true);

            sheep_kaka = builder
                    .comment("Allow sheep kaka")
                    .define("sheep_kaka", true);

            villager_kaka = builder
                    .comment("Allow villager kaka")
                    .define("villager_kaka", true);

            player_kaka = builder
                    .comment("Allow player kaka")
                    .define("player_kaka", true);

            minimal_timespan = builder
                    .comment("Allow player kaka")
                    .define("minimal_timespan", 1000);

            builder.pop();
        }

    }
}
