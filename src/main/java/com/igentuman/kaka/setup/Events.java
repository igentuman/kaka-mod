package com.igentuman.kaka.setup;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.entity.goal.KakaGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Kaka.MODID)
public class Events {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity e = event.getEntity();
        if (e instanceof Cow)
        {
            int goals = ((Cow) e).goalSelector.getAvailableGoals().toArray().length;
            ((Cow) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.COW_KAKA.get()));
        } else if(e instanceof Sheep) {
            int goals = ((Sheep) e).goalSelector.getAvailableGoals().toArray().length;
            ((Sheep) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.SHEEP_KAKA.get()));
        } else if(e instanceof Horse) {
            int goals = ((Horse) e).goalSelector.getAvailableGoals().toArray().length;
            ((Horse) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.HORSE_KAKA.get()));
        } else if(e instanceof Pig) {
            int goals = ((Pig) e).goalSelector.getAvailableGoals().toArray().length;
            ((Pig) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.PIG_KAKA.get()));
        } else if(e instanceof Villager) {
            int goals = ((Villager) e).goalSelector.getAvailableGoals().toArray().length;
            ((Villager) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.VILAGER_KAKA.get()));
        }
    }
}
