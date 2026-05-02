package org.yunhai.craft_enchanted_book;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.yunhai.craft_enchanted_book.init.ModItems;
import org.yunhai.craft_enchanted_book.network.ModNetwork;
import org.yunhai.craft_enchanted_book.pattern.PatternRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CraftEnchantedBook.MODID)
public class CraftEnchantedBook {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "craft_enchanted_book";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public CraftEnchantedBook() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        ModItems.ITEMS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        // 注册客户端事件处理器（用于充能动画）
        MinecraftForge.EVENT_BUS.addListener(this::clientTick);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Initialize pattern registry
        PatternRegistry.init();
        
        // Register network messages
        ModNetwork.register();
        
        LOGGER.info("Craft Enchanted Book mod initialized!");
    }

    // Add items to creative tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.QUILL_BASIC.get());
            event.accept(ModItems.QUILL_ADVANCED.get());
            event.accept(ModItems.QUILL_MASTER.get());
        }
    }
    
    // 客户端 tick 事件 - 用于检测长按右键充能
    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        // 这里可以添加长按检测逻辑（如果需要视觉反馈）
        // 由于 Forge 1.20 的限制，我们主要通过音效和物品更新来提供反馈
    }
}
