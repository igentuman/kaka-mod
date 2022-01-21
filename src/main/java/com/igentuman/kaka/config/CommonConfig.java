package com.igentuman.kaka.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

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
        public final ForgeConfigSpec.ConfigValue<Integer> player_kaka_hunger_loss;
        public final ForgeConfigSpec.ConfigValue<Boolean> disable_bonemeal_fertilize;
        public final ForgeConfigSpec.ConfigValue<Float> kaka_block_damage;

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

            player_kaka_hunger_loss = builder
                    .comment("How much player need to lose hunger to Kaka")
                    .define("player_kaka_hunger_loss", 3);

            minimal_timespan = builder
                    .comment("Minimal timespan for pooping")
                    .define("minimal_timespan", 1000);

            disable_bonemeal_fertilize = builder
                    .comment("Disable usage of bone meal on crops")
                    .define("disable_bonemeal_fertilize", true);

            kaka_block_damage = builder
                    .comment("Kaka Block damage when step on. (0 - disable damage)")
                    .define("kaka_block_damage", 0.1F);
            builder.pop();
        }
    }
}
