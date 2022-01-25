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
        this.level = mob.level;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        Random r = new Random();
        int low = CommonConfig.GENERAL.minimal_timespan.get();
        int high = low+1000;
        kakaDelay = r.nextInt(high-low) + low;
        kaka = kakaItem;
    }

    public boolean canUse() {
        return true;
    }

    public void start() {
        this.level.broadcastEntityEvent(this.mob, (byte)10);
        this.mob.getNavigation().stop();
    }

    public void tick() {
        if(this.level.isClientSide) {
            return;
        }
        this.counter++;
        if (this.counter > this.kakaDelay) {
            mob.spawnAtLocation(kaka);
            if(ClientConfig.GENERAL.mobs_fart_volume.get() > 0) {
                mob.playSound(Registration.FART.get(),1.0F, ClientConfig.GENERAL.mobs_fart_volume.get());
            }

            counter=0;
        }
    }
}
