package com.igentuman.kaka.setup;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.config.CommonConfig;
import com.igentuman.kaka.entity.boss.KakaDemon;
import com.igentuman.kaka.entity.goal.KakaGoal;
import com.igentuman.kaka.item.KakaItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Kaka.MODID)
public class Events {
    private static final Map<UUID, Integer> lastHungerLevels = new HashMap<UUID, Integer>();

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity e = event.getEntity();
        if (e instanceof Cow && CommonConfig.GENERAL.cow_kaka.get())
        {
            int goals = ((Cow) e).goalSelector.getAvailableGoals().toArray().length;
            ((Cow) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.COW_KAKA.get()));
        } else if(e instanceof Sheep && CommonConfig.GENERAL.sheep_kaka.get()) {
            int goals = ((Sheep) e).goalSelector.getAvailableGoals().toArray().length;
            ((Sheep) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.SHEEP_KAKA.get()));
        } else if(e instanceof Horse && CommonConfig.GENERAL.horse_kaka.get()) {
            int goals = ((Horse) e).goalSelector.getAvailableGoals().toArray().length;
            ((Horse) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.HORSE_KAKA.get()));
        } else if(e instanceof Pig && CommonConfig.GENERAL.pig_kaka.get()) {
            int goals = ((Pig) e).goalSelector.getAvailableGoals().toArray().length;
            ((Pig) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.PIG_KAKA.get()));
        } else if(e instanceof Villager && CommonConfig.GENERAL.villager_kaka.get()) {
            int goals = ((Villager) e).goalSelector.getAvailableGoals().toArray().length;
            ((Villager) e).goalSelector.addGoal(goals, new KakaGoal((Mob)e, Registration.VILAGER_KAKA.get()));
        }
        //setting default saturation level for player
        if(e instanceof Player && !e.level.isClientSide) {
            if( ! lastHungerLevels.containsKey(((Player) e).getUUID())) {
                lastHungerLevels.put(((Player) e).getUUID(), ((Player) e).getFoodData().getFoodLevel());
            }
        }
    }

    protected static boolean isKakaItem(Item item)
    {
        return item instanceof KakaItem;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onBoneMealEvent(BonemealEvent event)
    {
        if (event.isCanceled()) return;
        if(CommonConfig.GENERAL.disable_bonemeal_fertilize.get() && !isKakaItem(event.getStack().getItem())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.isCanceled()) return;
        if(!CommonConfig.GENERAL.player_kaka.get()) return;
        Player player = event.player;
        if (!player.level.isClientSide) {
            float diff = lastHungerLevels.get(player.getUUID()) - player.getFoodData().getFoodLevel();
            if(diff > 0 && diff >= CommonConfig.GENERAL.player_kaka_hunger_loss.get()*2) {
                player.spawnAtLocation(Registration.PLAYER_KAKA.get());
                lastHungerLevels.replace(player.getUUID(), player.getFoodData().getFoodLevel());
            } else if(diff < 0) {
                lastHungerLevels.replace(player.getUUID(), player.getFoodData().getFoodLevel());
            }
        }
    }

    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(Registration.KAKA_DEMON.get(), KakaDemon.prepareAttributes().build());
    }
}
