package com.igentuman.kaka.entity.goal;

import java.util.EnumSet;
import java.util.Random;

import com.igentuman.kaka.config.ClientConfig;
import com.igentuman.kaka.config.CommonConfig;
import com.igentuman.kaka.entity.sound.Sound;
import com.igentuman.kaka.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class KakaGoal extends Goal {

    private final Mob mob;
    private final Level level;
    private int kakaDelay = 100;
    private int counter = 0;
    private Item kaka;

    public KakaGoal(Mob mob, Item kakaItem) {
        this.mob = mob;
        this.level = mob.level();
        this.setFlags(EnumSet.noneOf(Goal.Flag.class)); // Don't interfere with other goals
        Random r = new Random();
        int low = CommonConfig.GENERAL.minimal_timespan.get();
        int high = low+1000;
        kakaDelay = r.nextInt(high-low) + low;
        kaka = kakaItem;
    }

    public boolean canUse() {
        return !this.level.isClientSide; // Only run on server side
    }

    public boolean canContinueToUse() {
        return !this.level.isClientSide; // Continue running on server side
    }

    public void start() {
        this.level.broadcastEntityEvent(this.mob, (byte)10);
        this.mob.getNavigation().stop();
    }

    public void tick() {
        this.counter++;
        if (this.counter > this.kakaDelay) {
            mob.spawnAtLocation(kaka);
            // Play sound on server side - the sound will be sent to clients automatically
            this.level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), 
                Registration.FART.get(), mob.getSoundSource(), 1.0F, 1.0F);
            
            // Reset counter and set new random delay
            counter = 0;
            Random r = new Random();
            int low = CommonConfig.GENERAL.minimal_timespan.get();
            int high = low + 1000;
            kakaDelay = r.nextInt(high - low) + low;
        }
    }
}
