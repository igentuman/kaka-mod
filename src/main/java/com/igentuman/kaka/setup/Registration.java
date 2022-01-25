package com.igentuman.kaka.setup;

import com.igentuman.kaka.Kaka;
import com.igentuman.kaka.block.KakaBlock;
import com.igentuman.kaka.block.KakaDemonHeadBlock;
import com.igentuman.kaka.effect.KakaEffect;
import com.igentuman.kaka.entity.ThrowKakaBlock;
import com.igentuman.kaka.entity.boss.KakaDemon;
import com.igentuman.kaka.item.KakaItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import static com.igentuman.kaka.Kaka.MODID;

public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    public static final RegistryObject<SoundEvent> FART = SOUND_EVENTS.register("fart", () -> new SoundEvent(new ResourceLocation(Kaka.MODID,"fart")));
    public static final RegistryObject<SoundEvent> KAKADEMON_HIT = SOUND_EVENTS.register("kakademon_hit", () -> new SoundEvent(new ResourceLocation(Kaka.MODID,"kakademon_hit")));
    public static final RegistryObject<SoundEvent> KAKADEMON_SPAWN = SOUND_EVENTS.register("kakademon_spawn", () -> new SoundEvent(new ResourceLocation(Kaka.MODID,"kakademon_spawn")));
    public static final RegistryObject<SoundEvent> KAKADEMON_DIE = SOUND_EVENTS.register("kakademon_die", () -> new SoundEvent(new ResourceLocation(Kaka.MODID,"kakademon_die")));
    public static final RegistryObject<SoundEvent> KAKADEMON_ATTACK = SOUND_EVENTS.register("kakademon_attack", () -> new SoundEvent(new ResourceLocation(Kaka.MODID,"kakademon_attack")));

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);
    public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.DIRT).strength(2f).sound(SoundType.HONEY_BLOCK).speedFactor(0.4f);

    public static final RegistryObject<Block> KAKA_DEMON_HEAD_BLOCK = BLOCKS.register("kaka_demon_head_block", () -> new KakaDemonHeadBlock(BLOCK_PROPERTIES));
    public static final RegistryObject<Item> KAKA_DEMON_HEAD_BLOCK_ITEM = fromBlock(KAKA_DEMON_HEAD_BLOCK);

    public static final RegistryObject<Block> KAKA_BLOCK = BLOCKS.register("kaka_block", () -> new KakaBlock(BLOCK_PROPERTIES));
    public static final RegistryObject<Item> KAKA_BLOCK_ITEM = fromBlock(KAKA_BLOCK);

    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static final RegistryObject<Item> COW_KAKA = registerItem("cow_kaka");
    public static final RegistryObject<Item> SHEEP_KAKA = registerItem("sheep_kaka");
    public static final RegistryObject<Item> HORSE_KAKA = registerItem("horse_kaka");
    public static final RegistryObject<Item> VILAGER_KAKA = registerItem("vilager_kaka");
    public static final RegistryObject<Item> PLAYER_KAKA = registerItem("player_kaka");
    public static final RegistryObject<Item> PIG_KAKA = registerItem("pig_kaka");

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Kaka.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Kaka.MODID);

    public static final RegistryObject<MobEffect> KAKA_EFFECT = EFFECTS.register("kaka", () -> new KakaEffect(MobEffectCategory.HARMFUL, 0xd4ff00));
    public static final RegistryObject<Potion> KAKA_POTION = POTIONS.register("kaka", () -> new Potion(new MobEffectInstance(KAKA_EFFECT.get(), 3600)));

    public static final RegistryObject<EntityType<KakaDemon>> KAKA_DEMON = ENTITIES.register("kaka_demon", () -> EntityType.Builder.of(KakaDemon::new, MobCategory.MONSTER)
            .clientTrackingRange(40)
            .setShouldReceiveVelocityUpdates(false)
            .sized(1.4F, 2.7F)
            .build("kaka_demon"));

    public static final RegistryObject<EntityType<ThrowKakaBlock>> THROW_KAKA_BLOCK = ENTITIES.register("kaka_block", () -> EntityType.Builder.of(ThrowKakaBlock::new, MobCategory.MISC)
            .build("kaka_block"));

    public static final RegistryObject<Item> KAKA_DEMON_EGG = ITEMS.register("kaka_demon", () -> new ForgeSpawnEggItem(KAKA_DEMON, 0x420000, 0x783F04, ITEM_PROPERTIES));

    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        EFFECTS.register(bus);
        POTIONS.register(bus);
        ENTITIES.register(bus);
        SOUND_EVENTS.register(bus);
    }

    public static final Tags.IOptionalNamedTag<Item> KAKA_ITEM = ItemTags.createOptional(new ResourceLocation(Kaka.MODID, "kaka_item"));

    public static RegistryObject<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new KakaItem(ITEM_PROPERTIES));
    }

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ITEM_PROPERTIES));
    }
    @SubscribeEvent
    public static void entityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(Registration.KAKA_DEMON.get(), KakaDemon.prepareAttributes().build());
    }
}
