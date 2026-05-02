package org.yunhai.craft_enchanted_book.init;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.yunhai.craft_enchanted_book.CraftEnchantedBook;
import org.yunhai.craft_enchanted_book.item.EnchantedQuillItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CraftEnchantedBook.MODID);

    // 羽毛笔 - 三个等级
    public static final RegistryObject<Item> QUILL_BASIC = ITEMS.register("quill_basic",
            () -> new EnchantedQuillItem(new Item.Properties().stacksTo(1), 1));
    
    public static final RegistryObject<Item> QUILL_ADVANCED = ITEMS.register("quill_advanced",
            () -> new EnchantedQuillItem(new Item.Properties().stacksTo(1), 2));
    
    public static final RegistryObject<Item> QUILL_MASTER = ITEMS.register("quill_master",
             () -> new EnchantedQuillItem(new Item.Properties().stacksTo(1), 3));
}
